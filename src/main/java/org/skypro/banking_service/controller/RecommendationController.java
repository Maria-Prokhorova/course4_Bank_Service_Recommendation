package org.skypro.banking_service.controller;

import org.skypro.banking_service.dto.RecommendationResponse;
import org.skypro.banking_service.service.RecommendationForClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@RestController
@RequestMapping("/recommendation")
public class RecommendationController {

    private final RecommendationForClientService recommendationForClientService;

    public RecommendationController(RecommendationForClientService recommendationForClientService) {
        this.recommendationForClientService = recommendationForClientService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<RecommendationResponse> getRecommendations(@PathVariable UUID userId) {
        return ResponseEntity.ok(recommendationForClientService.getRecommendations(userId));
    }
}
