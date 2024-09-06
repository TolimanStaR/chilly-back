package com.chilly.main_svc.service;

import com.chilly.main_svc.dto.PlaceDto;
import com.chilly.main_svc.mapper.PlaceDtoMapper;
import com.chilly.main_svc.model.Place;
import com.chilly.main_svc.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceDtoMapper placeMapper;
    private final PlaceRepository placeRepository;

    public void savePlaces(List<PlaceDto> placeDtoList) {
        placeRepository.deleteAll();
        List<Place> models = placeDtoList.stream().map(placeMapper::toModel).toList();
        placeRepository.saveAll(models);
    }

    public List<PlaceDto> getAllPlaces() {
        return placeRepository.findAll()
                .stream()
                .map(placeMapper::toDto)
                .toList();
    }
}
