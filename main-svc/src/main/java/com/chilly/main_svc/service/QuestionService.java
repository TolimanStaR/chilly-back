package com.chilly.main_svc.service;

import com.chilly.main_svc.mapper.QuestionDtoMapper;
import com.chilly.main_svc.model.Answer;
import com.chilly.main_svc.model.Question;
import com.chilly.main_svc.model.QuizType;
import com.chilly.main_svc.repository.AnswerRepository;
import com.chilly.main_svc.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chilly.common.dto.QuestionDto;
import org.chilly.common.dto.QuestionWithAnswers;
import org.chilly.common.dto.QuizResponse;
import org.chilly.common.dto.QuizSaveRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionService {
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final QuestionDtoMapper questionMapper;


    public void saveQuiz(QuizType quizType, QuizSaveRequest request) {
        questionRepository.deleteAllByQuizType(quizType);
        request.getQuestions().forEach(data -> saveQuestionWithAnswers(data, quizType));
    }

    public QuizResponse getQuiz(QuizType type) {
        List<QuestionDto> questionList = questionRepository.findByQuizType(type)
                .stream()
                .map(questionMapper::toDto)
                .toList();
        return new QuizResponse(type.toString(), questionList);
    }

    public void modifyQuestion(Long id, QuestionWithAnswers newQuestion) {
        Question question = questionRepository.findById(id)
                .orElseThrow();
        
        question.setBody(newQuestion.getQuestionText());
        question.setIndex(newQuestion.getQuestionIndex());
        question.setAnswers(saveAnswers(newQuestion.getAnswers()));
    }

    void saveQuestionWithAnswers(QuestionWithAnswers questionWithAnswers, QuizType type) {
        Question question = saveQuestion(questionWithAnswers, type);
        List<Answer> answers = questionWithAnswers.getAnswers().stream()
                .map(answerText -> buildAnswer(question, answerText))
                .toList();
        answerRepository.saveAll(answers);
    }

    Question saveQuestion(QuestionWithAnswers question, QuizType type) {
        return questionRepository.save(
                Question.builder()
                        .quizType(type)
                        .body(question.getQuestionText())
                        .index(question.getQuestionIndex())
                        .build()
        );
    }

    Answer buildAnswer(Question question, String answerText) {
        return Answer.builder()
                .body(answerText)
                .question(question)
                .build();

    }

    Answer buildAnswerWithNoQuestion(String answerText) {
        return Answer.builder()
                .body(answerText)
                .build();
    }

    Set<Answer> saveAnswers(List<String> answerTexts) {
        List<Answer> answers = answerTexts.stream()
                .map(this::buildAnswerWithNoQuestion)
                .toList();

        return Set.copyOf(answerRepository.saveAll(answers));
    }
}
