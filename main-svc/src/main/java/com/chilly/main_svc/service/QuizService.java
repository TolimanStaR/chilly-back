package com.chilly.main_svc.service;

import com.chilly.main_svc.dto.QuizAnswerDto;
import com.chilly.main_svc.dto.QuizAnswersDto;
import com.chilly.main_svc.exception.NoSuchEntityException;
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
import java.util.function.Consumer;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    public void submitAnswers(Long userId, QuizAnswersDto answersDto) {
        User user = findUserOrException(userId);
        List<QuizAnswer> answersToRemove = user.getQuizAnswers().stream()
                .filter(hasSameType(answersDto.getType()))
                .toList();

        quizRepository.deleteAll(answersToRemove);
        saveNewAnswers(answersDto.getAnswers(), user);
    }

    public void modifyAnswer(Long userId, QuizAnswerDto answerDto) {
        User user = findUserOrException(userId);
        user.getQuizAnswers().forEach(checkIdAndModify(answerDto));
    }

    private Predicate<QuizAnswer> hasSameType(QuizType type) {
        return answer -> answer.getQuestion().getQuizType() == type;
    }

    private void saveNewAnswers(List<QuizAnswerDto> dtoList, User user) {
        List<QuizAnswer> answerList = dtoList.stream()
                .map(dto -> buildQuizAnswer(dto, user))
                .toList();

        quizRepository.saveAll(answerList);
    }

    private QuizAnswer buildQuizAnswer(QuizAnswerDto dto, User user) {
        Question question = findQuestionByIdOrException(dto.getQuestionId());
        Answer answer = findAnswerByIdOrException(dto.getAnswerId());

        return QuizAnswer.builder()
                .user(user)
                .question(question)
                .answer(answer)
                .build();
    }

    private Consumer<QuizAnswer> checkIdAndModify(QuizAnswerDto answerDto) {
        return quizAnswer -> {
            if (doNotMatch(quizAnswer, answerDto)) {
                return;
            }
            quizAnswer.setAnswer(findAnswerByIdOrException(answerDto.getAnswerId()));
        };
    }

    private User findUserOrException(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("no user with id " + id));
    }

    private Answer findAnswerByIdOrException(Long id) {
        return answerRepository.findById(id)
                .orElseThrow(() -> new NoSuchEntityException("No answer with id = " + id));
    }

    private Question findQuestionByIdOrException(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new NoSuchEntityException("No question with id =" + id));
    }

    private boolean doNotMatch(QuizAnswer answer, QuizAnswerDto answerDto) {
        return !Objects.equals(answer.getQuestion().getId(), answerDto.getQuestionId());
    }
}
