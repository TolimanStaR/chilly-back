package com.chilly.main_svc.mapper;

import com.chilly.main_svc.dto.VisitDto;
import com.chilly.main_svc.model.Visit;
import org.springframework.stereotype.Component;

@Component
public class VisitDtoMapper extends BaseDtoMapper<Visit, VisitDto> {
    @Override
    protected Class<Visit> getEntityClass() {
        return Visit.class;
    }

    @Override
    protected Class<VisitDto> getDtoClass() {
        return VisitDto.class;
    }
}
