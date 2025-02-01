package com.chilly.places_svc.repository

import com.chilly.places_svc.model.Visit
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface VisitRepository : JpaRepository<Visit, Long> {
    fun findAllByUserId(userId: Long): List<Visit>
}