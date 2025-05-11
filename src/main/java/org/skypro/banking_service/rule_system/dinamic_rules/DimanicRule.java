package org.skypro.banking_service.rule_system.dinamic_rules;

import org.skypro.banking_service.dto.RecommendationDto;

import java.util.List;
import java.util.UUID;

public interface DimanicRule {

    List<RecommendationDto> checkOutDinamicRule(UUID userId);
}
