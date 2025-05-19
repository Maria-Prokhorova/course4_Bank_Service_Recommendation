package org.skypro.banking_service.dto;

import java.util.UUID;

public record StatisticsDTO(
        UUID ruleId,
        Long count) {
}
