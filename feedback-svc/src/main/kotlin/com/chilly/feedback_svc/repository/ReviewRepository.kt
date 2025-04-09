package com.chilly.feedback_svc.repository

import com.chilly.feedback_svc.model.Review
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ReviewRepository : JpaRepository<Review, Long> {

    @Query("select r from review r where r.placeId = ?1 order by r.timestamp DESC")
    fun findByPlaceIdOrderByTimestampDesc(placeId: Long, pageable: Pageable): List<Review>


    @Query("select r from review r where r.placeId = ?1 and r.userId = ?2")
    fun findByPlaceIdAndUserId(placeId: Long, userId: Long): Review?


}