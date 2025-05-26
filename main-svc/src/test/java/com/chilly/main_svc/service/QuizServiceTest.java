package com.chilly.main_svc.service;

import com.chilly.main_svc.model.*;
import com.chilly.main_svc.repository.AnswerRepository;
import com.chilly.main_svc.repository.QuestionRepository;
import com.chilly.main_svc.repository.QuizRepository;
import org.chilly.common.dto.QuizAnswerDto;
import org.chilly.common.dto.QuizAnswersDto;
import org.chilly.common.exception.NoSuchEntityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuizServiceTest {

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private UserService userService;

    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QuizService quizService;

    @Captor
    private ArgumentCaptor<List<QuizAnswer>> quizAnswersCaptor;

    private User testUser;
    private Question baseQuestion1;
    private Question baseQuestion2;
    private Answer answer1;
    private Answer answer2;
    private QuizAnswer baseQuizAnswer1;
    private QuizAnswer baseQuizAnswer2;
    private QuizAnswersDto quizAnswersDto;

    @BeforeEach
    void setUp() {
        // Create test user
        testUser = User.builder()
                .id(1L)
                .firstname("John")
                .lastname("Doe")
                .email("john.doe@example.com")
                .phoneNumber("+1234567890")
                .quizAnswers(new LinkedHashSet<>())
                .build();

        // Create test questions
        baseQuestion1 = Question.builder()
                .id(1L)
                .quizType(QuizType.BASE)
                .body("Base question 1?")
                .index(1)
                .answers(new LinkedHashSet<>())
                .build();

        baseQuestion2 = Question.builder()
                .id(2L)
                .quizType(QuizType.BASE)
                .body("Base question 2?")
                .index(2)
                .answers(new LinkedHashSet<>())
                .build();

        Question shortQuestion = Question.builder()
                .id(3L)
                .quizType(QuizType.SHORT)
                .body("Short question?")
                .index(1)
                .answers(new LinkedHashSet<>())
                .build();

        // Create test answers
        answer1 = Answer.builder()
                .id(1L)
                .body("Answer 1")
                .question(baseQuestion1)
                .build();

        answer2 = Answer.builder()
                .id(2L)
                .body("Answer 2")
                .question(baseQuestion2)
                .build();

        Answer answer3 = Answer.builder()
                .id(3L)
                .body("Answer 3")
                .question(shortQuestion)
                .build();

        // Add answers to questions
        baseQuestion1.getAnswers().add(answer1);
        baseQuestion2.getAnswers().add(answer2);
        shortQuestion.getAnswers().add(answer3);

        // Create quiz answers
        baseQuizAnswer1 = QuizAnswer.builder()
                .id(1L)
                .user(testUser)
                .question(baseQuestion1)
                .answer(answer1)
                .build();

        baseQuizAnswer2 = QuizAnswer.builder()
                .id(2L)
                .user(testUser)
                .question(baseQuestion2)
                .answer(answer2)
                .build();

        QuizAnswer shortQuizAnswer = QuizAnswer.builder()
                .id(3L)
                .user(testUser)
                .question(shortQuestion)
                .answer(answer3)
                .build();

        // Add quiz answers to user
        testUser.getQuizAnswers().add(baseQuizAnswer1);
        testUser.getQuizAnswers().add(baseQuizAnswer2);
        testUser.getQuizAnswers().add(shortQuizAnswer);

        // Create DTO objects for testing
        List<QuizAnswerDto> quizAnswerDtoList = List.of(
                new QuizAnswerDto(1L, 1L),
                new QuizAnswerDto(2L, 2L)
        );

        quizAnswersDto = new QuizAnswersDto("BASE", quizAnswerDtoList);
    }

    @Test
    void submitAnswers_ShouldDeleteOldAnswersAndSaveNewOnes() {
        // Arrange
        when(userService.findUserOrException(1L)).thenReturn(testUser);
        when(questionRepository.findById(1L)).thenReturn(Optional.of(baseQuestion1));
        when(questionRepository.findById(2L)).thenReturn(Optional.of(baseQuestion2));
        when(answerRepository.findById(1L)).thenReturn(Optional.of(answer1));
        when(answerRepository.findById(2L)).thenReturn(Optional.of(answer2));

        // Act
        quizService.submitAnswers(1L, quizAnswersDto);

        // Assert
        // Verify old BASE answers were deleted
        verify(quizRepository).deleteAll(List.of(baseQuizAnswer1, baseQuizAnswer2));

        // Verify new answers were saved
        verify(quizRepository).saveAll(quizAnswersCaptor.capture());

        List<QuizAnswer> capturedAnswers = quizAnswersCaptor.getValue();
        assertEquals(2, capturedAnswers.size());

        // Check first answer
        QuizAnswer capturedAnswer1 = capturedAnswers.get(0);
        assertEquals(testUser, capturedAnswer1.getUser());
        assertEquals(baseQuestion1, capturedAnswer1.getQuestion());
        assertEquals(answer1, capturedAnswer1.getAnswer());

        // Check second answer
        QuizAnswer capturedAnswer2 = capturedAnswers.get(1);
        assertEquals(testUser, capturedAnswer2.getUser());
        assertEquals(baseQuestion2, capturedAnswer2.getQuestion());
        assertEquals(answer2, capturedAnswer2.getAnswer());
    }

    @Test
    void submitAnswers_ShouldThrowExceptionWhenUserNotFound() {
        // Arrange
        when(userService.findUserOrException(999L))
                .thenThrow(new NoSuchEntityException("No user with id =999"));

        // Act & Assert
        NoSuchEntityException exception = assertThrows(
                NoSuchEntityException.class,
                () -> quizService.submitAnswers(999L, quizAnswersDto)
        );
        assertEquals("No user with id =999", exception.getMessage());

        // Verify no interactions with repositories
        verifyNoInteractions(quizRepository);
        verifyNoInteractions(questionRepository);
        verifyNoInteractions(answerRepository);
    }

    @Test
    void submitAnswers_ShouldThrowExceptionWhenQuestionNotFound() {
        // Arrange
        when(userService.findUserOrException(1L)).thenReturn(testUser);
        when(questionRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        NoSuchEntityException exception = assertThrows(
                NoSuchEntityException.class,
                () -> quizService.submitAnswers(1L, quizAnswersDto)
        );
        assertEquals("No question with id =1", exception.getMessage());

        // Verify old answers were deleted but no new answers were saved
        verify(quizRepository).deleteAll(any());
        verify(quizRepository, never()).saveAll(any());
    }

    @Test
    void submitAnswers_ShouldThrowExceptionWhenAnswerNotFound() {
        // Arrange
        when(userService.findUserOrException(1L)).thenReturn(testUser);
        when(questionRepository.findById(any())).thenReturn(Optional.of(baseQuestion1));
        when(answerRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        NoSuchEntityException exception = assertThrows(
                NoSuchEntityException.class,
                () -> quizService.submitAnswers(1L, quizAnswersDto)
        );
        assertEquals("No answer with id = 1", exception.getMessage());

        // Verify old answers were deleted but no new answers were saved
        verify(quizRepository).deleteAll(any());
        verify(quizRepository, never()).saveAll(any());
    }

    @Test
    void modifyAnswer_ShouldUpdateExistingAnswer() {
        // Arrange
        QuizAnswerDto modificationDto = new QuizAnswerDto(1L, 2L); // Question 1, new Answer 2

        when(userService.findUserOrException(1L)).thenReturn(testUser);
        when(answerRepository.findById(2L)).thenReturn(Optional.of(answer2));

        // Act
        quizService.modifyAnswer(1L, modificationDto);

        // Assert
        // Verify answer was updated
        assertEquals(answer2, baseQuizAnswer1.getAnswer());
    }

    @Test
    void modifyAnswer_ShouldNotUpdateIfQuestionIdDoesNotMatch() {
        // Arrange
        QuizAnswerDto modificationDto = new QuizAnswerDto(999L, 2L); // Non-matching question ID

        when(userService.findUserOrException(1L)).thenReturn(testUser);

        // Act
        quizService.modifyAnswer(1L, modificationDto);

        // Assert
        // Verify answer was not updated
        assertEquals(answer1, baseQuizAnswer1.getAnswer());

        // Verify answerRepository was never called
        verifyNoInteractions(answerRepository);
    }

    @Test
    void modifyAnswer_ShouldThrowExceptionWhenUserNotFound() {
        // Arrange
        QuizAnswerDto modificationDto = new QuizAnswerDto(1L, 2L);

        when(userService.findUserOrException(999L))
                .thenThrow(new NoSuchEntityException("No user with id =999"));

        // Act & Assert
        NoSuchEntityException exception = assertThrows(
                NoSuchEntityException.class,
                () -> quizService.modifyAnswer(999L, modificationDto)
        );
        assertEquals("No user with id =999", exception.getMessage());

        // Verify no interactions with repositories
        verifyNoInteractions(answerRepository);
    }

    @Test
    void modifyAnswer_ShouldThrowExceptionWhenAnswerNotFound() {
        // Arrange
        QuizAnswerDto modificationDto = new QuizAnswerDto(1L, 999L); // Valid question, non-existent answer

        when(userService.findUserOrException(1L)).thenReturn(testUser);
        when(answerRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        NoSuchEntityException exception = assertThrows(
                NoSuchEntityException.class,
                () -> quizService.modifyAnswer(1L, modificationDto)
        );
        assertEquals("No answer with id = 999", exception.getMessage());
    }

    @Test
    void submitAnswers_ShouldHandleEmptyAnswersList() {
        // Arrange
        QuizAnswersDto emptyDto = new QuizAnswersDto("BASE", List.of());

        when(userService.findUserOrException(1L)).thenReturn(testUser);

        // Act
        quizService.submitAnswers(1L, emptyDto);

        // Assert
        // Verify old answers were deleted
        verify(quizRepository).deleteAll(List.of(baseQuizAnswer1, baseQuizAnswer2));

        // Verify empty list was saved
        verify(quizRepository).saveAll(quizAnswersCaptor.capture());
        assertTrue(quizAnswersCaptor.getValue().isEmpty());
    }

    @Test
    void modifyAnswer_ShouldDoNothingIfNoMatchingQuizAnswer() {
        // Arrange
        User userWithNoAnswers = User.builder()
                .id(2L)
                .quizAnswers(new LinkedHashSet<>())
                .build();

        QuizAnswerDto modificationDto = new QuizAnswerDto(1L, 2L);

        when(userService.findUserOrException(2L)).thenReturn(userWithNoAnswers);

        // Act
        quizService.modifyAnswer(2L, modificationDto);

        // Assert
        // Verify no interactions with answer repository
        verifyNoInteractions(answerRepository);
    }
}
