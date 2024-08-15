package com.chilly.main_svc.controller;

import com.chilly.main_svc.dto.QuestionWithAnswers;
import com.chilly.main_svc.dto.QuizResponse;
import com.chilly.main_svc.dto.QuizSaveRequest;
import com.chilly.main_svc.model.QuizType;
import com.chilly.main_svc.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Questions", description = "Question related API")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @Operation(
            summary = "saves all questions for quiz",
            description = "Takes questions and answers, saves them into database, all previous questions will be deleted"
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void saveQuiz(
            @RequestParam QuizType type,
            @RequestBody QuizSaveRequest request
    ) {
        questionService.saveQuiz(type, request);
    }

    @Operation(summary = "retrieve all questions with answers for quiz selected by query parameter")
    @GetMapping
    public QuizResponse getQuiz(@RequestParam QuizType type) {
        return questionService.getQuiz(type);
    }

    @Operation(summary = "modify question by id, provide new answers, or change Text")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void modifyQuestion(
            @PathVariable("id") Long id,
            @RequestBody QuestionWithAnswers newQuestion
    ) {
        questionService.modifyQuestion(id, newQuestion);
    }

}
