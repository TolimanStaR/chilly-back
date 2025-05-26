package com.chilly.main_svc.controller;

import com.chilly.main_svc.model.QuizType;
import com.chilly.main_svc.service.QuestionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.chilly.common.dto.QuestionDto;
import org.chilly.common.dto.QuestionWithAnswers;
import org.chilly.common.dto.QuizResponse;
import org.chilly.common.dto.QuizSaveRequest;
import org.chilly.common.exception.NoSuchEntityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QuestionController.class)
class QuestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuestionService questionService;

    @Autowired
    private ObjectMapper objectMapper;

    private QuizSaveRequest quizSaveRequest;
    private QuizResponse quizResponse;
    private QuestionWithAnswers questionWithAnswers;

    @BeforeEach
    void setUp() {
        // Prepare test data
        questionWithAnswers = new QuestionWithAnswers(
                "What is the capital of France?",
                1,
                List.of("Paris", "London", "Berlin", "Madrid")
        );

        quizSaveRequest = new QuizSaveRequest(
                List.of(
                        questionWithAnswers,
                        new QuestionWithAnswers(
                                "What is the largest planet in our solar system?",
                                2,
                                List.of("Jupiter", "Saturn", "Earth", "Mars")
                        )
                )
        );

        // Create QuestionDto objects for the response
        List<QuestionDto> questionDtos = List.of(
                new QuestionDto(1L, "What is the capital of France?", null),
                new QuestionDto(2L, "What is the largest planet in our solar system?", null)
        );

        quizResponse = new QuizResponse("BASE", questionDtos);
    }

    @Test
    void saveQuiz_ShouldCallServiceAndReturnCreatedStatus() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/questions")
                        .param("type", "BASE")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(quizSaveRequest)))
                .andExpect(status().isCreated());

        // Verify that service method was called with correct parameters
        verify(questionService).saveQuiz(eq(QuizType.BASE), any(QuizSaveRequest.class));
    }

    @Test
    void getQuiz_ShouldReturnQuizResponse() throws Exception {
        // Arrange
        when(questionService.getQuiz(QuizType.BASE)).thenReturn(quizResponse);

        // Act & Assert
        mockMvc.perform(get("/api/questions")
                        .param("type", "BASE"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type", is("BASE")))
                .andExpect(jsonPath("$.questions", hasSize(2)))
                .andExpect(jsonPath("$.questions[0].id", is(1)))
                .andExpect(jsonPath("$.questions[0].body", is("What is the capital of France?")))
                .andExpect(jsonPath("$.questions[1].id", is(2)))
                .andExpect(jsonPath("$.questions[1].body", is("What is the largest planet in our solar system?")));

        verify(questionService).getQuiz(QuizType.BASE);
    }

    @Test
    void modifyQuestion_ShouldCallServiceAndReturnOkStatus() throws Exception {
        // Act & Assert
        mockMvc.perform(put("/api/questions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(questionWithAnswers)))
                .andExpect(status().isOk());

        // Verify that service method was called with correct parameters
        verify(questionService).modifyQuestion(eq(1L), any(QuestionWithAnswers.class));
    }

    @Test
    void saveQuiz_ShouldHandleInvalidQuizType() throws Exception {
        // Act & Assert - Invalid quiz type
        mockMvc.perform(post("/api/questions")
                        .param("type", "INVALID_TYPE")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(quizSaveRequest)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(questionService);
    }

    @Test
    void saveQuiz_ShouldHandleInvalidRequestBody() throws Exception {
        // Act & Assert - Invalid JSON
        mockMvc.perform(post("/api/questions")
                        .param("type", "BASE")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{invalid json}"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(questionService);
    }

    @Test
    void getQuiz_ShouldHandleInvalidQuizType() throws Exception {
        // Act & Assert - Invalid quiz type
        mockMvc.perform(get("/api/questions")
                        .param("type", "INVALID_TYPE"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(questionService);
    }

    @Test
    void getQuiz_ShouldHandleMissingParameter() throws Exception {
        // Act & Assert - Missing required parameter
        mockMvc.perform(get("/api/questions"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(questionService);
    }

    @Test
    void modifyQuestion_ShouldHandleNonExistentQuestion() throws Exception {
        // Arrange - Mock service to throw exception for non-existent question
        doThrow(new NoSuchEntityException("Question not found"))
                .when(questionService).modifyQuestion(eq(999L), any(QuestionWithAnswers.class));

        // Act & Assert
        mockMvc.perform(put("/api/questions/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(questionWithAnswers)))
                .andExpect(status().isNotFound()); // Default error handling for NoSuchElementException

        verify(questionService).modifyQuestion(eq(999L), any(QuestionWithAnswers.class));
    }

    @Test
    void modifyQuestion_ShouldHandleInvalidRequestBody() throws Exception {
        // Act & Assert - Invalid JSON
        mockMvc.perform(put("/api/questions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{invalid json}"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(questionService);
    }

    @Test
    void modifyQuestion_ShouldHandleNonNumericId() throws Exception {
        // Act & Assert - Non-numeric ID
        mockMvc.perform(put("/api/questions/abc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(questionWithAnswers)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(questionService);
    }
}
