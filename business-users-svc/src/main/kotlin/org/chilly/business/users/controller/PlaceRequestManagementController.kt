package org.chilly.business.users.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.transaction.Transactional
import org.chilly.business.users.service.RequestService
import org.chilly.common.dto.PendingRequestDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/business/place_requests")
@Tag(name = "Business Place Requests Management", description = "Place request management related API used by administrators")
class PlaceRequestManagementController(
    private val service: RequestService
) {
    @PostMapping("{id}/approve")
    @Transactional
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "approve request specified by id")
    fun approveRequest(
        @PathVariable("id") requestId: Long,
    ) {
        service.approveRequest(requestId)
    }

    @PostMapping("{id}/decline")
    @Transactional
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "decline request specified by id")
    fun declineRequest(
        @PathVariable("id") requestId: Long,
        @RequestParam reason: String,
    ) {
        service.declineRequest(requestId, reason)
    }

    @GetMapping("pending")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "allows moderators to get all pending requests")
    fun getPendingRequests(): List<PendingRequestDto> {
        return service.getPendingRequests()
    }
}