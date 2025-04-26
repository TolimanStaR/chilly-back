package org.chilly.business.users.repository

import org.chilly.business.users.model.PlaceAddRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PlaceRequestRepository : JpaRepository<PlaceAddRequest, Long>
