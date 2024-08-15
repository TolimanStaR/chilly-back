package com.chilly.main_svc.controller;

import com.chilly.main_svc.dto.QuizAnswerDto;
import com.chilly.main_svc.dto.QuizAnswersDto;
import com.chilly.main_svc.service.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Quizzes", description = "Quiz related API")
@SecurityRequirement(name = "Bearer Authentication")
@RestController()
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
public class QuizController {

    @Value("${http.headers.user-id}")
    private String userIdHeader;

    private final QuizService quizService;

    @Operation(summary = "saves user's answers for quiz questions")
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void submitAnswers(
            @RequestHeader("UserId") Long userId,
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
