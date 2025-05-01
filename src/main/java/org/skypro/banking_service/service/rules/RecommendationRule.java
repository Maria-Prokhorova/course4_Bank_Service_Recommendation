package org.skypro.banking_service.service.rules;


import org.skypro.banking_service.dto.RecommendationDto;

import java.util.Optional;

public interface RecommendationRule {

    Optional<RecommendationDto> checkOut(String userId);
}

