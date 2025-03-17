package com.chilly.places_svc.service

import com.chilly.places_svc.mapper.PlaceMapper
import com.chilly.places_svc.model.Place
import com.chilly.places_svc.repository.PlaceRepository
import org.chilly.common.dto.PlaceDto
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import kotlin.reflect.KMutableProperty

@Service
class PlaceService(
    private val placeMapper: PlaceMapper,
    private val placeRepository: PlaceRepository
) {

    fun replacePlaces(placeDtoList: List<PlaceDto>) {
        placeRepository.deleteAll()
        placeDtoList.map(placeMapper::toEntity)
            .let(placeRepository::saveAll)
    }

    fun getAllPlaces() = placeRepository.findAll()
            .map(placeMapper::toDto)

    fun getPlacesByIds(ids: List<Long>) = placeRepository.findAllByIdIn(ids)
            .map(placeMapper::toDto)

    fun editPlaceInfo(places: List<PlaceDto>): Int =
        places.mapNotNull(::changePlaceUsingDto)
            .let(placeRepository::saveAll)
            .size

    private fun changePlaceUsingDto(dto: PlaceDto): Place? {
        val saved = dto.id?.let(placeRepository::findByIdOrNull) ?: return null
        val updated = listOf(
            checkField(dto.name, saved::name),
            checkField(dto.images, saved::images),
            checkField(dto.address, saved::address),
            checkField(dto.rating, saved::rating),
            checkField(dto.website, saved::website),
            checkField(dto.social, saved::social),
            checkField(dto.yPage, saved::yPage),
            checkField(dto.phone, saved::phone),
            checkField(dto.openHours, saved::openHours),
            checkField(dto.latitude, saved::latitude),
            checkField(dto.longitude, saved::longitude)
        ).any()
        return if (updated) saved else null
    }

    private fun <T> checkField(dtoValue: T?, property: KMutableProperty<T>): Boolean {
        if (dtoValue == null) return false
        val fromEntity = property.getter.call()
        return if (dtoValue != fromEntity) {
            property.setter.call(dtoValue)
            true
        } else
            false
    }
}
