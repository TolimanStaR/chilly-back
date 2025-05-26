package com.chilly.main_svc.service;

import com.chilly.main_svc.mapper.QuestionDtoMapper;
import com.chilly.main_svc.model.Answer;
import com.chilly.main_svc.model.Question;
import com.chilly.main_svc.model.QuizType;
import com.chilly.main_svc.repository.AnswerRepository;
import com.chilly.main_svc.repository.QuestionRepository;
import org.chilly.common.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {

    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private QuestionDtoMapper questionMapper;

    @InjectMocks
    private QuestionService questionService;

    @Captor
    private ArgumentCaptor<Question> questionCaptor;

    @Captor
    private ArgumentCaptor<List<Answer>> answersCaptor;

    private Question testQuestion;
    private Answer testAnswer;
    private QuestionWithAnswers testQuestionWithAnswers;
    private QuestionDto testQuestionDto;

    @BeforeEach
    void setUp() {
        testQuestion = Question.builder()
                .id(1L)
                .quizType(QuizType.BASE)
                .body("Test question?")
                .index(1)
                .build();

        testAnswer = Answer.builder()
                .id(1L)
                .body("Test answer")
                .question(testQuestion)
                .build();

        testQuestionWithAnswers = new QuestionWithAnswers(
                "Test question?",
                1,
                List.of("Test answer 1", "Test answer 2")
        );

        List<AnswerDto> answerDtos = List.of(
                new AnswerDto(1L, "Test answer 1"),
                new AnswerDto(2L, "Test answer 2")
        );

        testQuestionDto = new QuestionDto(1L, "Test question?", answerDtos);
    }

    @Test
    void saveQuiz_ShouldDeleteExistingQuestionsAndSaveNewOnes() {
        // Arrange
        QuizSaveRequest request = new QuizSaveRequest(List.of(testQuestionWithAnswers));
        QuizType quizType = QuizType.BASE;

        // Mock behavior
        when(questionRepository.save(any(Question.class))).thenReturn(testQuestion);
        when(answerRepository.saveAll(anyList())).thenReturn(List.of(testAnswer));

        // Act
        questionService.saveQuiz(quizType, request);

        // Assert
        verify(questionRepository).deleteAllByQuizType(quizType);
        verify(questionRepository).save(any(Question.class));
        verify(answerRepository).saveAll(anyList());
    }

    @Test
    void getQuiz_ShouldReturnQuizResponseWithQuestionDtos() {
        // Arrange
        List<Question> questions = List.of(testQuestion);
        when(questionRepository.findByQuizType(QuizType.BASE)).thenReturn(questions);
        when(questionMapper.toDto(any(Question.class))).thenReturn(testQuestionDto);

        // Act
        QuizResponse response = questionService.getQuiz(QuizType.BASE);

        // Assert
        assertNotNull(response);
        assertEquals("BASE", response.getType());
        assertEquals(1, response.getQuestions().size());
        verify(questionRepository).findByQuizType(QuizType.BASE);
        verify(questionMapper).toDto(testQuestion);
    }

    @Test
    void modifyQuestion_ShouldUpdateQuestionAndAnswers() {
        // Arrange
        Long questionId = 1L;
        QuestionWithAnswers newQuestion = new QuestionWithAnswers(
                "Updated question",
                2,
                List.of("New answer 1", "New answer 2")
        );

        Set<Answer> savedAnswers = new HashSet<>(List.of(
                Answer.builder().id(2L).body("New answer 1").build(),
                Answer.builder().id(3L).body("New answer 2").build()
        ));

        when(questionRepository.findById(questionId)).thenReturn(Optional.of(testQuestion));
        when(answerRepository.saveAll(anyList())).thenReturn(new ArrayList<>(savedAnswers));

        // Act
        questionService.modifyQuestion(questionId, newQuestion);

        // Assert
        verify(questionRepository).findById(questionId);

        // Verify question was updated
        assertEquals("Updated question", testQuestion.getBody());
        assertEquals(2, testQuestion.getIndex());
        assertEquals(savedAnswers, testQuestion.getAnswers());

        // Verify answers were saved
        verify(answerRepository).saveAll(anyList());
    }

    @Test
    void saveQuestionWithAnswers_ShouldSaveQuestionAndItsAnswers() {
        // Arrange
        when(questionRepository.save(any(Question.class))).thenReturn(testQuestion);

        // Act
        questionService.saveQuestionWithAnswers(testQuestionWithAnswers, QuizType.BASE);

        // Assert
        verify(questionRepository).save(questionCaptor.capture());
        verify(answerRepository).saveAll(answersCaptor.capture());

        Question capturedQuestion = questionCaptor.getValue();
        assertEquals(QuizType.BASE, capturedQuestion.getQuizType());
        assertEquals("Test question?", capturedQuestion.getBody());
        assertEquals(1, capturedQuestion.getIndex());

        List<Answer> capturedAnswers = answersCaptor.getValue();
        assertEquals(2, capturedAnswers.size());
        assertEquals("Test answer 1", capturedAnswers.get(0).getBody());
        assertEquals("Test answer 2", capturedAnswers.get(1).getBody());
        assertEquals(testQuestion, capturedAnswers.get(0).getQuestion());
        assertEquals(testQuestion, capturedAnswers.get(1).getQuestion());
    }

    @Test
    void saveQuestion_ShouldSaveAndReturnQuestion() {
        // Arrange
        when(questionRepository.save(any(Question.class))).thenReturn(testQuestion);

        // Act
        Question result = questionService.saveQuestion(testQuestionWithAnswers, QuizType.BASE);

        // Assert
        verify(questionRepository).save(questionCaptor.capture());
        Question capturedQuestion = questionCaptor.getValue();

        assertEquals(QuizType.BASE, capturedQuestion.getQuizType());
        assertEquals("Test question?", capturedQuestion.getBody());
        assertEquals(1, capturedQuestion.getIndex());
        assertEquals(testQuestion, result);
    }

    @Test
    void buildAnswer_ShouldCreateAnswerWithQuestionReference() {
        // Act
        Answer result = questionService.buildAnswer(testQuestion, "Test answer");

        // Assert
        assertEquals("Test answer", result.getBody());
        assertEquals(testQuestion, result.getQuestion());
    }

    @Test
    void buildAnswerWithNoQuestion_ShouldCreateAnswerWithoutQuestionReference() {
        // Act
        Answer result = questionService.buildAnswerWithNoQuestion("Test answer");

        // Assert
        assertEquals("Test answer", result.getBody());
        assertNull(result.getQuestion());
    }

    @Test
    void saveAnswers_ShouldSaveAndReturnAnswersAsSet() {
        // Arrange
        List<String> answerTexts = List.of("Answer 1", "Answer 2");
        List<Answer> savedAnswers = List.of(
                Answer.builder().id(1L).body("Answer 1").build(),
                Answer.builder().id(2L).body("Answer 2").build()
        );

        when(answerRepository.saveAll(anyList())).thenReturn(savedAnswers);

        // Act
        Set<Answer> result = questionService.saveAnswers(answerTexts);

        // Assert
        verify(answerRepository).saveAll(answersCaptor.capture());
        List<Answer> capturedAnswers = answersCaptor.getValue();

        assertEquals(2, capturedAnswers.size());
        assertEquals("Answer 1", capturedAnswers.get(0).getBody());
        assertEquals("Answer 2", capturedAnswers.get(1).getBody());

        assertEquals(2, result.size());
        assertThat(result).containsExactlyInAnyOrderElementsOf(savedAnswers);
    }

    @Test
    void modifyQuestion_ShouldThrowNoSuchElementException_WhenQuestionNotFound() {
        // Arrange
        Long nonExistentId = 999L;
        when(questionRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () ->
                questionService.modifyQuestion(nonExistentId, testQuestionWithAnswers)
        );

        verify(questionRepository).findById(nonExistentId);
        verifyNoMoreInteractions(answerRepository);
    }
}
