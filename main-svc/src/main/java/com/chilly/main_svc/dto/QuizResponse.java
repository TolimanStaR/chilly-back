package com.chilly.main_svc.dto;

import com.chilly.main_svc.model.QuizType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizResponse {
    QuizType type;
    List<QuestionDto> questions;
}
