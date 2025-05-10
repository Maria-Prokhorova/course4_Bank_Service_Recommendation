package org.skypro.banking_service.service;

import org.skypro.banking_service.model.Recommendations;

import java.util.List;
import java.util.UUID;

public interface DinamicRulesService {

    Recommendations addRecommendationByRule(Recommendations recommendation);

    List<Recommendations> getAllRecommendationByRule();

    void deleteRecommendationByRule(UUID id);
}
