package com.chilly.places_svc.controller

import com.chilly.places_svc.service.VisitService
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.chilly.common.dto.SaveVisitRequest
import org.chilly.common.dto.VisitDto
import org.springframework.web.bind.annotation.*

@Tag(name = "Visits", description = "Visit related API")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/visits")
class VisitController(
    private val visitService: VisitService
) {

    @PostMapping
    fun saveVisit(
        @RequestHeader("UserId") userId: Long,
        @RequestBody request: SaveVisitRequest
    ) {
        visitService.saveVisit(userId, request)
    }

    @GetMapping
    fun getVisited(@RequestHeader("UserId") userId: Long): List<VisitDto> {
        return visitService.getVisited(userId)
    }

    @DeleteMapping("{id}")
    fun deleteVisit(@RequestHeader("UserId") userId: Long, @PathVariable("id") visitId: Long) {
        visitService.deleteVisit(userId, visitId)
    }
}