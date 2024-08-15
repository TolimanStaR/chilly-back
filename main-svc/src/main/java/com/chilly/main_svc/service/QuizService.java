package com.chilly.main_svc.service;

import com.chilly.main_svc.dto.QuizAnswerDto;
import com.chilly.main_svc.dto.QuizAnswersDto;
import com.chilly.main_svc.exception.UserNotFoundException;
import com.chilly.main_svc.model.*;
import com.chilly.main_svc.repository.AnswerRepository;
import com.chilly.main_svc.repository.QuestionRepository;
import com.chilly.main_svc.repository.QuizRepository;
import com.chilly.main_svc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    public void submitAnswers(Long userId, QuizAnswersDto answersDto) {
        User user = findUserOrException(userId);

        Set<QuizAnswer> newAnswers = user.getQuizAnswers()
                .stream()
                .filter(hasOtherType(answersDto.getType()))
                .collect(Collectors.toSet());

        newAnswers.addAll(convertAnswersToEntities(answersDto.getAnswers()));
        user.setQuizAnswers(newAnswers);
    }

    public void modifyAnswer(Long userId, QuizAnswerDto answerDto) {
        User user = findUserOrException(userId);

        Set<QuizAnswer> newAnswers = user.getQuizAnswers().stream()
                .map(answerByIdReplacer(answerDto))
                .collect(Collectors.toSet());
        user.setQuizAnswers(newAnswers);
    }

    private Predicate<QuizAnswer> hasOtherType(QuizType type) {
        return answer -> answer.getQuestion().getQuizType() != type;
    }

    private List<QuizAnswer> convertAnswersToEntities(List<QuizAnswerDto> dtoList) {
        List<QuizAnswer> answerList = dtoList.stream()
                .map(this::buildQuizAnswer)
                .toList();

        return quizRepository.saveAll(answerList);
    }

    private QuizAnswer buildQuizAnswer(QuizAnswerDto dto) {
        Question question = questionRepository.findById(dto.getQuestionId())
                .orElseThrow();
        Answer answer = answerRepository.findById(dto.getAnswerId())
                .orElseThrow();
        return QuizAnswer.builder()
                .question(question)
                .answer(answer)
                .build();
    }

    private User findUserOrException(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("no user with id " + id));
    }

    private Answer findAnswerOrException(Long id) {
        return answerRepository.findById(id)
                .orElseThrow();
    }

    private Function<QuizAnswer, QuizAnswer> answerByIdReplacer(QuizAnswerDto answerDto) {
        return answer -> {
            if (doNotMatch(answer, answerDto)) {
                return answer;
            }
            Answer newAnswer = findAnswerOrException(answerDto.getAnswerId());
            return QuizAnswer.builder()
                    .question(answer.getQuestion())
                    .answer(newAnswer)
                    .build();
        };
    }

    private boolean doNotMatch(QuizAnswer answer, QuizAnswerDto answerDto) {
        return !Objects.equals(answer.getQuestion().getId(), answerDto.getQuestionId());
    }
}
