package com.chilly.places_svc.dto

data class PlaceDto(
    val id: Long,
    val name: String,
    var address: String? = null,
    var website: String? = null,
    var yPage: String? = null,
    var rating: Double? = null,
    var images: List<String>? = null,
    var phone: String? = null,
    var social: List<String>? = null,
    var openHours: List<String>? = null
)
