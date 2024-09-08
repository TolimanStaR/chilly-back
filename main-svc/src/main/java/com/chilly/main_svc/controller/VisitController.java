package com.chilly.main_svc.controller;

import com.chilly.main_svc.dto.PlaceDto;
import com.chilly.main_svc.dto.SaveVisitRequest;
import com.chilly.main_svc.dto.VisitDto;
import com.chilly.main_svc.service.VisitService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Visits", description = "Visit related API")
@SecurityRequirement(name = "Bearer Authentication")
@RestController()
@RequestMapping("/api/visits")
@RequiredArgsConstructor
public class VisitController {

    private final VisitService visitService;

    @PostMapping
    public void saveVisit(@RequestHeader("UserId") Long userId, @RequestBody SaveVisitRequest request) {
        visitService.saveVisit(userId, request);
    }

    @GetMapping
    public List<VisitDto> getVisited(@RequestHeader("UserId") Long userId) {
        return visitService.getVisited(userId);
    }

    @DeleteMapping("{id}")
    public void deleteVisit(@RequestHeader("UserId") Long userId, @PathVariable("id") Long visitId) {
        visitService.deleteVisit(userId, visitId);
    }
}
