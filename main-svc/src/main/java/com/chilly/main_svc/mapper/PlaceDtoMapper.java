package com.chilly.main_svc.mapper;

import com.chilly.main_svc.dto.PlaceDto;
import com.chilly.main_svc.model.Place;
import org.springframework.stereotype.Component;

@Component
public class PlaceDtoMapper extends BaseDtoMapper<Place, PlaceDto>{

    @Override
    protected Class<Place> getEntityClass() {
        return Place.class;
    }

    @Override
    protected Class<PlaceDto> getDtoClass() {
        return PlaceDto.class;
    }
}
