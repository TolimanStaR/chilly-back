package org.chilly.business.users.model

import jakarta.persistence.*
import org.chilly.business.users.mapper.InstantToLongConverter
import org.chilly.business.users.mapper.ListToStringConverter
import org.hibernate.Hibernate
import java.time.Instant

@Entity(name = "place_request")
@Table(name = "place_requests")
class PlaceAddRequest(
    @Column(name = "timestamp", nullable = false)
    @Convert(converter = InstantToLongConverter::class)
    var timestamp: Instant,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    var status: RequestStatus,

    @Column(name = "reason", nullable = true)
    var reason: String? = null,

    // attributes for places
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
    @Id
    @SequenceGenerator(name = "place_request_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "place_request_sequence")
    @Column(name = "id")
    var id: Long? = null

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    lateinit var owner: BusinessUser

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as PlaceAddRequest

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
}