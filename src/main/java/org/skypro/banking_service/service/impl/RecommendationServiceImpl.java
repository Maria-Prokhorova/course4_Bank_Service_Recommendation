package org.skypro.banking_service.service.impl;

import org.skypro.banking_service.dto.RecommendationDto;
import org.skypro.banking_service.dto.RecommendationResponse;
import org.skypro.banking_service.exception.UserNotFoundException;
import org.skypro.banking_service.repository.RecommendationRepository;
import org.skypro.banking_service.service.RecommendationService;
import org.skypro.banking_service.rulesystem.RecommendationRule;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    private final RecommendationRepository repository;
    private final List<RecommendationRule> listRules;

    public RecommendationServiceImpl(RecommendationRepository repository, List<RecommendationRule> listRules) {
        this.repository = repository;
        this.listRules = listRules;
    }

    @Override
    public RecommendationResponse getRecommendations(UUID userId) {
        validateUserExists(userId);

        List<RecommendationDto> recommendations = collectRecommendation(userId);

        return new RecommendationResponse(userId, recommendations);
    }

    private void validateUserExists(UUID userId) {
        if (!repository.userExists(userId)) {
            throw new UserNotFoundException(userId);
        }
    }

    private List<RecommendationDto> collectRecommendation(UUID userId) {
        List<RecommendationDto> recommend = new ArrayList<>();
        for (RecommendationRule rule : listRules) {
            rule.checkOut(userId).ifPresent(recommend::add);
        }
        return recommend;
    }

}
