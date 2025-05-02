package org.chilly.business.users.repository

import org.chilly.business.users.model.PlaceAddRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface PlaceRequestRepository : JpaRepository<PlaceAddRequest, Long> {
    @Query("SELECT r FROM place_request r WHERE r.status = 'CREATED'")
    fun findAllPendingRequests(): List<PlaceAddRequest>
}
