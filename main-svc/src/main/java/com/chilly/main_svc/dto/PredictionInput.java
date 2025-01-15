package com.chilly.main_svc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PredictionInput {
    private UserDto user;
    private List<QuizAnswerForRecDto> shortQuizAnswers;
    private List<QuizAnswerForRecDto> baseQuizAnswers;
}
