@file:Suppress("UNUSED")
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

    @Operation(summary = "replaces info for provided places")
    @SecurityRequirement(name = "Bearer Authentication")
    @Transactional
    @PutMapping
    fun editPlacesInfo(@RequestBody placeDtoList: List<PlaceDto>): Int = placeService.editPlaceInfo(placeDtoList)

    @Operation(summary = "get paginated places sorted by distance from provided point")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("nearby")
    fun getNearbyPlaces(
        @RequestParam latitude: Double,
        @RequestParam longitude: Double,
        @RequestParam(required = false, defaultValue = "0") page: Int,
        @RequestParam(required = false, defaultValue = "10") size: Int
    ): List<PlaceDto> = placeService.findNearbyPlaces(latitude, longitude, page, size)

    /* Internal Requests */

    @PostMapping("new/internal")
    @ResponseStatus(HttpStatus.CREATED)
    @Hidden
    @Operation(summary = "add one place with owner assigned")
    fun addPlaceInternal(
        @RequestBody data: PlaceDto
    ) {
        placeService.addPlaceInternal(data)
    }

    @GetMapping("owned_by/{id}/internal")
    fun getPlacesByOwnerId(
        @PathVariable("id") ownerId: Long
    ): List<PlaceDto> {
        return placeService.getOwnedPlaces(ownerId)
    }

    @Operation(summary = "find all places from list of ids")
    @SecurityRequirement(name = "Api key")
    @PostMapping("ids")
    @Hidden
    fun getPlacesByIdsInternal(@RequestBody ids: List<Long>) = placeService.getPlacesByIds(ids)
}
