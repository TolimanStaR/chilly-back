package com.chilly.places_svc.mapper

import com.chilly.places_svc.model.Place
import org.chilly.common.dto.PlaceDto
import org.springframework.stereotype.Component

@Component
class PlaceMapper : DtoEntityMapper<Place, PlaceDto> {

    override fun toDto(entity: Place): PlaceDto = PlaceDto().apply {
        id = entity.id
        name = entity.name
        address = entity.address
        website = entity.website
        yPage = entity.yPage
        rating = entity.rating
        images = entity.images
        phone = entity.phone
        social = entity.social
        openHours = entity.openHours
        latitude = entity.latitude
        longitude = entity.longitude
        ownerId = entity.ownerId
    }

    override fun toEntity(dto: PlaceDto): Place = Place(
        id = dto.id,
        name = dto.name,
        address = dto.address,
        website = dto.website,
        yPage = dto.yPage,
        rating = dto.rating,
        images = dto.images,
        phone = dto.phone,
        social = dto.social,
        openHours = dto.openHours,
        latitude = dto.latitude,
        longitude = dto.longitude,
        ownerId = dto.ownerId
    )
}