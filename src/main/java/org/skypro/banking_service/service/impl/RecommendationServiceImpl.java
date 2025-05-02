package org.skypro.banking_service.service.impl;

import org.skypro.banking_service.dto.RecommendationDto;
import org.skypro.banking_service.dto.RecommendationResponse;
import org.skypro.banking_service.exception.UserNotFoundException;
import org.skypro.banking_service.repository.RecommendationRepository;
import org.skypro.banking_service.service.RecommendationService;
import org.skypro.banking_service.service.rules.RecommendationRule;
import org.springframework.stereotype.Service;

import java.util.*;

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
        if (!repository.userExists(userId)) {
            throw new UserNotFoundException(userId);
        } else {
            List<Optional<RecommendationDto>> recommend = new ArrayList<>();
            listRules.forEach(recommendationRule ->
                    recommend.add(recommendationRule.checkOut(userId))
            );
            return new RecommendationResponse(userId, recommend);
        }
    }
}
