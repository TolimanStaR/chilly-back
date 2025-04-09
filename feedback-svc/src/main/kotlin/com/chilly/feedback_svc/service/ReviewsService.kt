package com.chilly.feedback_svc.service

import com.chilly.feedback_svc.mapper.ReviewMapper
import com.chilly.feedback_svc.model.Review
import com.chilly.feedback_svc.repository.ReviewRepository
import org.chilly.common.dto.AddReviewRequest
import org.chilly.common.dto.ReviewDto
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class ReviewsService(
    private val reviewRepository: ReviewRepository,
    private val reviewMapper: ReviewMapper
) {
    fun updateOrCreateReview(request: AddReviewRequest, userId: Long): Boolean {
        var modified = false
        val review = reviewRepository.findByPlaceIdAndUserId(request.placeId, userId)
            ?.let {
                modified = true
                modifyReview(it, request)
            }
            ?: createNewReview(request, userId)

        reviewRepository.save(review)
        return !modified
    }

    fun getReviewsForPlace(placeId: Long, page: Int, size: Int): List<ReviewDto> =
        reviewRepository.findByPlaceIdOrderByTimestampDesc(placeId, PageRequest.of(page, size))
            .map(reviewMapper::toDto)

    private fun createNewReview(request: AddReviewRequest, creatorId: Long): Review = Review(
        placeId = request.placeId,
        userId = creatorId,
        rating = request.rating,
        commentText = request.commentText,
        timestamp = Instant.now()
    )

    private fun modifyReview(entity: Review, request: AddReviewRequest): Review = entity.apply {
        rating = request.rating
        commentText = request.commentText
        timestamp = Instant.now()
    }
}