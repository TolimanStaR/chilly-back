package com.chilly.places_svc.model

import com.chilly.places_svc.mapper.ListToStringConverter
import jakarta.persistence.*

@Entity(name = "place")
@Table(name = "places")
class Place(
    @Id
    @Column(name = "id", nullable = false)
    val id: Long,

    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "address", nullable = true)
    var address: String? = null,

    @Column(name = "website", nullable = true)
    var website: String? = null,

    @Column(name = "latitude", nullable = false)
    var latitude: Double = 0.0,

    @Column(name = "longitude", nullable = false)
    var longitude: Double = 0.0,

    @Column(name = "y_page", nullable = true)
    var yPage: String? = null,

    @Column(name = "rating", nullable = true)
    var rating: Double? = null,

    @Column(name = "images", nullable = true)
    @Convert(converter = ListToStringConverter::class)
    var images: List<String>? = null,

    @Column(name = "phone", nullable = true)
    var phone: String? = null,

    @Column(name = "social", nullable = true)
    @Convert(converter = ListToStringConverter::class)
    var social: List<String>? = null,

    @Column(name = "open_hours", nullable = true)
    @Convert(converter = ListToStringConverter::class)
    var openHours: List<String>? = null,
) {

    @OneToMany(mappedBy = "place", orphanRemoval = true)
    var visits: MutableSet<Visit> = mutableSetOf()
}