package com.chilly.feedback_svc.service

import com.chilly.feedback_svc.mapper.ReviewMapper
import com.chilly.feedback_svc.repository.ReviewRepository
import org.chilly.common.dto.ReviewDto
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class ReviewsService(
    private val reviewRepository: ReviewRepository,
    private val reviewMapper: ReviewMapper
) {
    fun createReview(reviewDto: ReviewDto, userId: Long) {
        reviewMapper.toEntity(reviewDto)
            .apply { this.userId = userId }
            .let(reviewRepository::save)
    }

    fun getReviewsForPlace(placeId: Long, page: Int, size: Int): List<ReviewDto> =
        reviewRepository.findByPlaceIdOrderByTimestampDesc(placeId, PageRequest.of(page, size))
            .map(reviewMapper::toDto)
}