package com.chilly.main_svc.controller;

import com.chilly.main_svc.dto.PlaceDto;
import com.chilly.main_svc.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Recommendations", description = "Recommendations related API")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/recs")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @Operation(summary = "returns predicted places for user")
    @GetMapping
    public List<PlaceDto> getRecommendations(
            @RequestHeader(value = "UserId", required = false) Long userId
    ) {
        return recommendationService.getRecommendations(userId);
    }
}
