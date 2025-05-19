package org.skypro.banking_service.service.ruleSystem.statickRulesSystem.rules;

import org.skypro.banking_service.dto.RecommendationDTO;

import java.util.Optional;
import java.util.UUID;

public interface StaticRule {

    // Проверка подходит ли банковский продукт со статическими правилами заданному клиенту.
    Optional<RecommendationDTO> checkOutStaticRule(UUID userId);
}

