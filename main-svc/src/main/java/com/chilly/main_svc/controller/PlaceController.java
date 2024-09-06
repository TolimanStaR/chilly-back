package com.chilly.main_svc.controller;

import com.chilly.main_svc.dto.PlaceDto;
import com.chilly.main_svc.service.PlaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Places", description = "Places related API")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/places")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;


    @Operation(summary = "save all listed places")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public void savePlaces(@RequestBody List<PlaceDto> placeDtoList) {
        placeService.savePlaces(placeDtoList);
    }

    @Operation(summary = "lists all known places")
    @GetMapping
    public List<PlaceDto> getAllPlaces() {
        return placeService.getAllPlaces();
    }
}
