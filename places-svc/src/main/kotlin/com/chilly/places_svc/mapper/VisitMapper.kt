package com.chilly.places_svc.mapper

import com.chilly.places_svc.model.Visit
import org.chilly.common.dto.VisitDto
import org.springframework.stereotype.Component

@Component
class VisitMapper(
    private val placeMapper: PlaceMapper
) : DtoEntityMapper<Visit, VisitDto> {

    override fun toDto(entity: Visit): VisitDto = VisitDto().apply {
        id = entity.id ?: throw IllegalArgumentException()
        date = entity.date
        place = placeMapper.toDto(entity.place)
    }

    override fun toEntity(dto: VisitDto): Visit = Visit(
        date = dto.date
    ).apply {
        place = placeMapper.toEntity(dto.place)
        id = dto.id
    }
}