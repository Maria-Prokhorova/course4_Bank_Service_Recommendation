package org.skypro.banking_service.service.dinamic_system;

import org.skypro.banking_service.model.Recommendation;

import java.util.List;
import java.util.UUID;

public interface DinamicRuleService {

    Recommendation addRecommendationByRule(Recommendation recommendation);

    List<Recommendation> getAllRecommendationByRule();

    void deleteRecommendationByRule(UUID id);
}
