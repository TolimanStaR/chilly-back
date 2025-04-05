package com.chilly.feedback_svc.model

import com.chilly.feedback_svc.mapper.LocalDateTimeToLongConverter
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity(name = "review")
@Table(name = "reviews")
class Review(

    @Column(name = "place_id", nullable = false)
    var placeId: Long,

    @Column(name = "user_id", nullable = false)
    var userId: Long,

    @Column(name = "rating", nullable = false)
    var rating: Float,

    @Column(name = "comment_text", nullable = true)
    var commentText: String? = null,

    @Column(name = "timestamp", nullable = false)
    @Convert(converter = LocalDateTimeToLongConverter::class)
    var timestamp: LocalDateTime
) {

    @Id
    @SequenceGenerator(name = "review_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "review_sequence")
    @Column(name = "id")
    var id: Long? = null
}