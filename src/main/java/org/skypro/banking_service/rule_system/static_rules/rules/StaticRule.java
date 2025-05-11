package org.skypro.banking_service.rule_system.static_rules.rules;

import org.skypro.banking_service.dto.RecommendationDto;

import java.util.Optional;
import java.util.UUID;

public interface StaticRule {

    Optional<RecommendationDto> checkOutStaticRule(UUID userId);
}

