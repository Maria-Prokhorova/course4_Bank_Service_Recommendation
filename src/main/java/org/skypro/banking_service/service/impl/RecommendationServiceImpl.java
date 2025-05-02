package org.skypro.banking_service.service.impl;

import org.skypro.banking_service.dto.RecommendationDto;
import org.skypro.banking_service.dto.RecommendationResponse;
import org.skypro.banking_service.exception.UserNotFoundException;
import org.skypro.banking_service.repository.RecommendationRepository;
import org.skypro.banking_service.service.RecommendationService;
import org.skypro.banking_service.service.rules.RecommendationRule;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    private final RecommendationRepository recommendationRepository;
    private final List<RecommendationRule> rules;

    public RecommendationServiceImpl(RecommendationRepository recommendationRepository, List<RecommendationRule> rules) {
        this.recommendationRepository = recommendationRepository;
        this.rules = rules;
    }

    @Override
    public RecommendationResponse getRecommendations(UUID userId) {
        validateUserExists(userId);

        List<RecommendationDto> recommendations = collectRecommendations(userId);
        return new RecommendationResponse(userId, recommendations);
    }

    private void validateUserExists(UUID userId) {
        if (!recommendationRepository.userExists(userId)) {
            throw new UserNotFoundException(userId);
        }
    }

    private List<RecommendationDto> collectRecommendations(UUID userId) {
        List<RecommendationDto> result = new ArrayList<>();

        for (RecommendationRule rule : rules) {
            rule.checkOut(userId).ifPresent(result::add);
        }
        return result;
    }
}
