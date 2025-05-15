package org.skypro.banking_service.telegramBot.dto;

import java.util.List;

public record RuleStatsResponse(
        List<RuleStatsDTO> stats) {

}
