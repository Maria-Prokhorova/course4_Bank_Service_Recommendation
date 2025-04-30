package org.skypro.banking_service.dto;


import lombok.Data;

import java.util.List;

@Data
public class RecommendationResponse {
    private String userId;
    private List<RecommendationDto> recommendations;

    public RecommendationResponse() {
    }

    public RecommendationResponse(String userId, List<RecommendationDto> recommendations) {
        this.userId = userId;
        this.recommendations = recommendations;
    }
}