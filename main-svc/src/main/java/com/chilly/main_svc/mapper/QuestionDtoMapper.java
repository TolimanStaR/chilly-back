package com.chilly.main_svc.mapper;

import com.chilly.main_svc.dto.QuestionDto;
import com.chilly.main_svc.model.Question;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class QuestionDtoMapper {

    private final ModelMapper mapper = new ModelMapper();

    public QuestionDto toDto(Question model) {
        return mapper.map(model, QuestionDto.class);
    }
}
