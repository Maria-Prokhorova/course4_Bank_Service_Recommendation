package org.skypro.banking_service.telegramBot.dto;

import java.util.UUID;

public record RuleStatsDTO(
        UUID ruleId,
        Long count) {
}
