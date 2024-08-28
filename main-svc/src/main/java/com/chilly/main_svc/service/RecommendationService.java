package com.chilly.main_svc.service;

import com.chilly.main_svc.dto.PlaceDto;
import com.chilly.main_svc.dto.PredictionInput;
import com.chilly.main_svc.dto.QuizAnswerDto;
import com.chilly.main_svc.exception.UserNotFoundException;
import com.chilly.main_svc.mapper.PlaceDtoMapper;
import com.chilly.main_svc.mapper.UserDtoModelMapper;
import com.chilly.main_svc.model.Place;
import com.chilly.main_svc.model.QuizAnswer;
import com.chilly.main_svc.model.QuizType;
import com.chilly.main_svc.model.User;
import com.chilly.main_svc.repository.PlaceRepository;
import com.chilly.main_svc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    private final UserDtoModelMapper userMapper;
    private final PlaceDtoMapper placeMapper;
    private final WebClient webClient;

    private static final ParameterizedTypeReference<List<Long>> LONG_LIST_TYPE_REF = new ParameterizedTypeReference<>() {};

    public List<PlaceDto> getRecommendations(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("no user with id = " + userId));

        Set<QuizAnswer> userAnswers = user.getQuizAnswers();
        List<QuizAnswerDto> shortAnswers = filterAndMapAnswers(userAnswers, QuizType.SHORT);
        List<QuizAnswerDto> baseAnswers = filterAndMapAnswers(userAnswers, QuizType.BASE);

        PredictionInput input = PredictionInput.builder()
                .user(userMapper.toDto(user))
                .shortQuizAnswers(shortAnswers)
                .baseQuizAnswers(baseAnswers)
                .build();

        List<Long> placesIds = callPredictionService(input);

        List<Place> places = placeRepository.findAllByIdIn(placesIds);
        return places.stream()
                .map(placeMapper::toDto)
                .toList();
    }

    private Predicate<QuizAnswer> hasQuizType(QuizType type) {
        return answer -> answer.getQuestion().getQuizType() == type;
    }

    private QuizAnswerDto quizAnswerToDto(QuizAnswer answer) {
        return new QuizAnswerDto(answer.getQuestion().getId(), answer.getId());
    }

    private List<QuizAnswerDto> filterAndMapAnswers(Set<QuizAnswer> answers, QuizType type) {
        return answers.stream()
                .filter(hasQuizType(type))
                .map(this::quizAnswerToDto)
                .toList();
    }

    private List<Long> callPredictionService(PredictionInput input) {
        return webClient.post()
                .uri("http://rec-svc/api/prediction")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(input)
                .retrieve()
                .bodyToMono(LONG_LIST_TYPE_REF)
                .block();
    }

}
