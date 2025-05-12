package org.skypro.banking_service.ruleSystem.statickRulesSystem;

import org.skypro.banking_service.dto.RecommendationDto;
import org.skypro.banking_service.repositories.h2.repository.UserTransactionRepository;

import java.util.Optional;
import java.util.UUID;

public interface RecommendationRule {

    Optional<RecommendationDto> checkOut(UUID userId);
}

