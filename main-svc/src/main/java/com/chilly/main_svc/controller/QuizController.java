package com.chilly.main_svc.controller;

import com.chilly.main_svc.dto.QuizAnswerDto;
import com.chilly.main_svc.dto.QuizAnswersDto;
import com.chilly.main_svc.service.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Quizzes", description = "Quiz related API")
@SecurityRequirement(name = "Bearer Authentication")
@RestController()
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @Operation(summary = "saves user's answers for quiz questions")
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void submitAnswers(
            @Parameter(description = "this header is calculated automatically, it should not be set")
            @RequestHeader(value = "UserId", required = false) Long userId,
            @RequestBody QuizAnswersDto answersDto
    ) {
        quizService.submitAnswers(userId, answersDto);
    }

    @Operation(summary = "change answer for question by its id")
    @PutMapping("/question")
    @ResponseStatus(HttpStatus.OK)
    public void modifyQuizAnswer(
            @RequestHeader("UserId") Long userId,
            @RequestBody QuizAnswerDto answerDto
    ) {
        quizService.modifyAnswer(userId, answerDto);
    }
}
