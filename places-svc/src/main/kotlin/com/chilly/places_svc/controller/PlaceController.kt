package com.chilly.places_svc.controller

import com.chilly.places_svc.service.PlaceService
import io.swagger.v3.oas.annotations.Hidden
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
    @Operation(summary = "clear all places and then save all listed places")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "Bearer Authentication")
    @Transactional
    fun replacePlaces(@RequestBody placeDtoList: List<PlaceDto>) {
        placeService.replacePlaces(placeDtoList)
    }

    @GetMapping
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "lists all known places")
    fun allPlaces() = placeService.getAllPlaces()

    // TODO: Use query parameter for ids use method for both users and admins
    @Operation(summary = "find all places from list of ids")
    @SecurityRequirement(name = "Api key")
    @PostMapping("ids")
    @Hidden
    fun getPlacesByIds(@RequestBody ids: List<Long>) = placeService.getPlacesByIds(ids)

    @Operation(summary = "replaces info for provided places")
    @SecurityRequirement(name = "Bearer Authentication")
    @Transactional
    @PutMapping
    fun editPlacesInfo(@RequestBody placeDtoList: List<PlaceDto>): Int = placeService.editPlaceInfo(placeDtoList)
}
