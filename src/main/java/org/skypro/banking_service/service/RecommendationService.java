package org.skypro.banking_service.service;

import org.skypro.banking_service.dto.RecommendationResponse;

public interface RecommendationService {

    RecommendationResponse getRecommendations(String userId);
}
