package com.chilly.main_svc.service;

import com.chilly.main_svc.mapper.UserDtoModelMapper;
import com.chilly.main_svc.model.QuizAnswer;
import com.chilly.main_svc.model.QuizType;
import com.chilly.main_svc.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chilly.common.dto.PlaceDto;
import org.chilly.common.dto.PredictionInput;
import org.chilly.common.dto.QuizAnswerForRecDto;
import org.chilly.common.exception.CallFailedException;
import org.chilly.common.exception.EmptyDataException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendationService {
    private final UserService userService;
    private final UserDtoModelMapper userMapper;
    private final WebClient webClient;

    private static final ParameterizedTypeReference<List<Long>> LONG_LIST_TYPE_REF = new ParameterizedTypeReference<>() {};
    private static final ParameterizedTypeReference<List<PlaceDto>> PLACE_DTO_LIST_TYPE_REF = new ParameterizedTypeReference<>() {};

    public List<PlaceDto> getRecommendations(Long userId) {
        PredictionInput input = gatherInput(userId);
        log.info("built prediction input: {}", input);
        List<Long> placesIds = callCatching(() -> callPredictionService(input), "recommendations unavailable");
        log.info("predicted ids: {}", placesIds);
        return callCatching(() -> callPlacesService(placesIds), "places unavailable");
    }

    private PredictionInput gatherInput(Long userId) {
        User user = userService.findUserOrException(userId);
        Set<QuizAnswer> userAnswers = user.getQuizAnswers();

        return PredictionInput.builder()
                .user(userMapper.toDto(user))
                .shortQuizAnswers(filterAndMapAnswersOrException(userAnswers, QuizType.SHORT))
                .baseQuizAnswers(filterAndMapAnswersOrException(userAnswers, QuizType.BASE))
                .build();
    }

    private List<Long> callPredictionService(PredictionInput input) {
        return webClient.post()
                .uri("http://rec-svc/api/prediction")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(input)
                .retrieve().bodyToMono(LONG_LIST_TYPE_REF).block();
    }

    private List<PlaceDto> callPlacesService(List<Long> ids) {
        return webClient.post()
                .uri("http://places-svc/api/places/ids")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ids)
                .retrieve().bodyToMono(PLACE_DTO_LIST_TYPE_REF).block();
    }

    private <T> T callCatching(Supplier<T> call, String message) {
        try {
            return call.get();
        } catch (Exception e) {
            throw new CallFailedException(message);
        }
    }

    private Predicate<QuizAnswer> hasQuizType(QuizType type) {
        return answer -> answer.getQuestion().getQuizType() == type;
    }

    private QuizAnswerForRecDto quizAnswerToDto(QuizAnswer answer) {
        return new QuizAnswerForRecDto(answer.getAnswer().getBody(), answer.getQuestion().getIndex());
    }

    private List<QuizAnswerForRecDto> filterAndMapAnswersOrException(Set<QuizAnswer> answers, QuizType type) {
        List<QuizAnswerForRecDto> result = answers.stream()
                .filter(hasQuizType(type))
                .map(this::quizAnswerToDto)
                .toList();
        if (result.isEmpty()) {
            throw new EmptyDataException(String.format("%s quiz is not filled", type.name()));
        }
        return result;
    }

}
