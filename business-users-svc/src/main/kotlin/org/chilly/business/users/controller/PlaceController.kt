package org.chilly.business.users.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.chilly.business.users.service.PlaceService
import org.chilly.common.dto.PlaceDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/business/places")
@Tag(name = "Business Places Requests", description = "Places related API")
class PlaceController(
    private val placeService: PlaceService
) {

    @GetMapping
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "get places registered by current user")
    fun getOwnedPlaces(
        @RequestHeader("UserId") userId: Long
    ): List<PlaceDto> {
        return placeService.getOwnedPlaces(userId)
    }

}