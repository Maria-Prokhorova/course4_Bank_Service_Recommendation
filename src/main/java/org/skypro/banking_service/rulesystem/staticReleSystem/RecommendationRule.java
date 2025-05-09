package org.skypro.banking_service.rulesystem.staticReleSystem;

import org.skypro.banking_service.dto.RecommendationDto;

import java.util.Optional;
import java.util.UUID;

public interface RecommendationRule {

    Optional<RecommendationDto> checkOut(UUID userId);
}

