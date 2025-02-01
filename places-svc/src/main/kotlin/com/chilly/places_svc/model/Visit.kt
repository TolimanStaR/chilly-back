package com.chilly.places_svc.model

import jakarta.persistence.*
import org.hibernate.Hibernate
import java.util.*

@Entity(name = "visit")
@Table(name = "visits")
class Visit (
    @Column(name = "date", nullable = false)
    var date: Date
) {
    @Column(name = "user_id", nullable = false)
    var userId: Long = -1

    @ManyToOne
    @JoinColumn(name = "place_id", nullable = false)
    lateinit var place: Place

    @Id
    @SequenceGenerator(name = "visit_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "visit_sequence")
    @Column(name = "id")
    var id: Long? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Visit

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
}