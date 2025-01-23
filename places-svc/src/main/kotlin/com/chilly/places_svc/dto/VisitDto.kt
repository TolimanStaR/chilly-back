package com.chilly.places_svc.dto

import java.util.*

data class VisitDto(
    val id: Long,
    val date: Date,
    val place: PlaceDto,
)
