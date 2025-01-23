package com.chilly.places_svc.dto

import java.util.*

data class SaveVisitRequest(
    val placeId: Long,
    val timestamp: Date? = null
)
