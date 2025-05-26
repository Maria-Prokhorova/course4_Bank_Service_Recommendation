package org.skypro.banking_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Статистика по срабатыванию рекомендаций")
public record StatisticsDTO(

        @Schema(description = "ID правила")
        UUID ruleId,

        @Schema(description = "Счетчик")
        Long count) {
}

