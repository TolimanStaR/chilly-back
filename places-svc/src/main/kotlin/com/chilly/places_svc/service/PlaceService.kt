package com.chilly.places_svc.service

import com.chilly.places_svc.dto.PlaceDto
import com.chilly.places_svc.mapper.PlaceMapper
import com.chilly.places_svc.repository.PlaceRepository
import org.springframework.stereotype.Service

@Service
class PlaceService(
    private val placeMapper: PlaceMapper,
    private val placeRepository: PlaceRepository
) {

    fun savePlaces(placeDtoList: List<PlaceDto>) {
        placeRepository.deleteAll()
        placeDtoList.map(placeMapper::toEntity)
            .let(placeRepository::saveAll)

    }

    fun getAllPlaces() = placeRepository.findAll()
            .map(placeMapper::toDto)

    fun getPlacesByIds(ids: List<Long>) = placeRepository.findAllByIdIn(ids)
            .map(placeMapper::toDto)
}
