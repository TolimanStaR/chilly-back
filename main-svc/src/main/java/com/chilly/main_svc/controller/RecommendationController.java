package com.chilly.main_svc.controller;

import com.chilly.main_svc.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.chilly.common.dto.PlaceDto;
import org.chilly.common.dto.PredictionInput;
import org.chilly.common.exception.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Recommendations", description = "Recommendations related API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @Operation(summary = "returns predicted places for user")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("recs")
    public List<PlaceDto> getRecommendations(
            @RequestHeader(value = "UserId", required = false) Long userId
    ) {
        return recommendationService.getRecommendations(userId);
    }

    // this end-point is a surrogate because python service doesn't expose documentation to swagger
    // actual call to /api/prediction is redirected to rec-svc at gateway
    @Operation(summary = "direct call to recommendation service")
    @SecurityRequirement(name = "Api key")
    @PostMapping("prediction")
    public List<Long> directCallToRecommendations(@RequestBody PredictionInput input) {
        throw new AccessDeniedException("this controller should not be called");
    }
}
