package org.chilly.business.users.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.transaction.Transactional
import org.chilly.business.users.service.RequestService
import org.chilly.common.dto.PlaceDto
import org.chilly.common.dto.PlaceRequestDto
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/business/place_requests")
@Tag(name = "Business Place Requests", description = "Place-adding request related API")
class PlaceRequestController(
    private val service: RequestService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    @Operation(summary = "Create a request to Add place to the system")
    fun createRequest(
        @RequestHeader("UserId") userId: Long,
        @RequestBody request: PlaceDto
    ) {
        service.createRequest(userId, request)
    }

    @GetMapping
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Get list of all existing requests to add place for business user")
    fun getAllCompanyRequests(
        @RequestHeader("UserId") userId: Long
    ): List<PlaceRequestDto> {
        return service.getRequests(userId)
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "change information in the request specified by id")
    fun changeRequest(
        @RequestHeader("UserId") userId: Long,
        @PathVariable("id") requestId: Long,
        @RequestBody data: PlaceDto
    ) {
        return service.changeRequest(userId, requestId, data)
    }

    @DeleteMapping("{id}")
    @Transactional
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "delete the request specified by id")
    fun deleteRequest(
        @RequestHeader("UserId") userId: Long,
        @PathVariable("id") requestId: Long,
    ) {
        return service.deleteRequest(userId, requestId)
    }
}