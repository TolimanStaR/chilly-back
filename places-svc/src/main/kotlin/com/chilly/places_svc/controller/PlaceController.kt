package com.chilly.places_svc.controller

import com.chilly.places_svc.service.PlaceService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.chilly.common.dto.PlaceDto
import org.springframework.http.HttpStatus
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

@Tag(name = "Places", description = "Places related API")
@RestController
@RequestMapping("/api/places")
class PlaceController(
    private val placeService: PlaceService,
) {
    @Operation(summary = "save all listed places")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "Bearer Authentication")
    @Transactional
    fun savePlaces(@RequestBody placeDtoList: List<PlaceDto>) {
        placeService.savePlaces(placeDtoList)
    }

    @GetMapping
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "lists all known places")
    fun allPlaces() = placeService.getAllPlaces()

    @Operation(summary = "find all places from list of ids")
    @SecurityRequirement(name = "Api key")
    @PostMapping("ids")
    fun getPlacesByIds(@RequestBody ids: List<Long>) = placeService.getPlacesByIds(ids)
}
