package com.chilly.feedback_svc.controller

import com.chilly.feedback_svc.service.ReviewsService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.chilly.common.dto.AddReviewRequest
import org.chilly.common.dto.ReviewDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Suppress("UNUSED")
@Tag(name = "Reviews", description = "Reviews related API")
@RestController
@RequestMapping("/api/reviews")
class ReviewsController(
    private val reviewsService: ReviewsService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "save review for a given place from a given user")
    fun createReview(
        @RequestBody request: AddReviewRequest,
        @RequestHeader("UserId") userId: Long
    ): ResponseEntity<Unit> {
        return reviewsService.updateOrCreateReview(request, userId)
            .let { created ->
                val status = if (created) HttpStatus.CREATED else HttpStatus.OK
                ResponseEntity.status(status).build()
            }
    }

    @GetMapping("place/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "get paginated reviews for a place with specified id")
    fun getReviewsForPlacePaged(
        @PathVariable(name = "id") placeId: Long,
        @RequestParam(required = false, defaultValue = "0") page: Int,
        @RequestParam(required = false, defaultValue = "10") size: Int
    ): List<ReviewDto> {
        return reviewsService.getReviewsForPlace(placeId, page, size)
    }
}