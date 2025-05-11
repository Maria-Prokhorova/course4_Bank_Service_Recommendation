package org.skypro.banking_service.service;

import org.skypro.banking_service.model.Recommendation;

import java.util.List;
import java.util.UUID;

public interface RecommendationWithRulesService {

    Recommendation addRecommendationByRule(Recommendation recommendation);

    List<Recommendation> getAllRecommendationByRule();

    void deleteRecommendationByRule(UUID id);
}
