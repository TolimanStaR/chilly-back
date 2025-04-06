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
    fun updateOrCreateReview(reviewDto: ReviewDto, userId: Long): Boolean {
        val existingReview = reviewRepository.findByPlaceIdAndUserId(reviewDto.placeId, userId)
            ?: return createNewReview(reviewDto, userId)

        existingReview.apply {
            rating = reviewDto.rating
            commentText = reviewDto.commentText
            timestamp = reviewMapper.toEntity(reviewDto).timestamp
        }
        reviewRepository.save(existingReview)
        return false
    }

    fun getReviewsForPlace(placeId: Long, page: Int, size: Int): List<ReviewDto> =
        reviewRepository.findByPlaceIdOrderByTimestampDesc(placeId, PageRequest.of(page, size))
            .map(reviewMapper::toDto)

    private fun createNewReview(dto: ReviewDto, userId: Long): Boolean =
        reviewMapper.toEntity(dto)
            .apply { this.userId = userId }
            .let(reviewRepository::save)
            .let { true }
}