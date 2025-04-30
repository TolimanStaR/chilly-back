package com.chilly.places_svc.repository

import com.chilly.places_svc.model.Place
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface PlaceRepository : JpaRepository<Place, Long> {

    fun findAllByIdIn(ids: List<Long>): List<Place>

    @Query(
        "SELECT * FROM places p " +
            "ORDER BY ST_DistanceSphere(ST_MakePoint(p.longitude, p.latitude), ST_MakePoint(:lon, :lat)) ASC",
        nativeQuery = true
    )
    fun findNearByPlaces(@Param("lat") latitude: Double, @Param("lon") longitude: Double, pageable: Pageable): List<Place>

    fun findAllByOwnerId(ownerId: Long): List<Place>

    @Query("SELECT NEXTVAL('place_sequence')", nativeQuery = true)
    fun getNextIdFromSequence(): Long

}

fun PlaceRepository.saveCheckId(place: Place): Place {
    if (place.id == null) {
        place.id = getNextIdFromSequence()
    }
    return save(place)
}

fun PlaceRepository.saveAllCheckId(places: Iterable<Place>): List<Place> {
    places.forEach { place ->
        if (place.id == null) {
            place.id = getNextIdFromSequence()
        }
    }
    return saveAll(places)
}