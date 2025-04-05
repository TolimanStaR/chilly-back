package com.chilly.feedback_svc.mapper

import com.chilly.feedback_svc.model.Review
import org.chilly.common.dto.ReviewDto
import org.springframework.stereotype.Component

@Component
class ReviewMapper(
    private val dateTimeToLongConverter: LocalDateTimeToLongConverter
) : DtoEntityMapper<Review, ReviewDto> {

    override fun toDto(entity: Review): ReviewDto = ReviewDto().apply {
        id = entity.id!!
        placeId = entity.placeId
        userId = entity.userId
        commentText = entity.commentText
        rating = entity.rating
        timestamp = dateTimeToLongConverter.convertToDatabaseColumn(entity.timestamp)
    }

    override fun toEntity(dto: ReviewDto): Review = Review(
        placeId = dto.placeId,
        userId = dto.userId,
        commentText = dto.commentText,
        rating = dto.rating,
        timestamp = dateTimeToLongConverter.convertToEntityAttribute(dto.timestamp)
    ).apply {
        id = dto.id
    }

}