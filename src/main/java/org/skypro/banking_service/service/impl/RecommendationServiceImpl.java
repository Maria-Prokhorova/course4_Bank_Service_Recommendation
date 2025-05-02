package org.skypro.banking_service.service.impl;

import org.skypro.banking_service.dto.RecommendationDto;
import org.skypro.banking_service.dto.RecommendationResponse;
import org.skypro.banking_service.exception.UserNotFoundException;
import org.skypro.banking_service.repository.RecommendationRepository;
import org.skypro.banking_service.service.RecommendationService;
import org.skypro.banking_service.service.rules.RecommendationRule;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    private final RecommendationRepository repository;
    private final RecommendationRule rules;

    public RecommendationServiceImpl(RecommendationRepository repository, RecommendationRule rules) {
        this.repository = repository;
        this.rules = rules;
    }


    @Override
    public RecommendationResponse getRecommendations(UUID userId) {
        if (!repository.userExists(userId)) {
            throw new UserNotFoundException(userId);
        }
        else {
            Optional<RecommendationDto> recommend = rules.checkOut(userId);
            RecommendationResponse response = new RecommendationResponse(userId, recommend);
        }
        return null;
    }
}
