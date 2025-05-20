package org.skypro.banking_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Результат выполнения метода по получению статистики")
public record StatisticsResponse(
        List<StatisticsDTO> stats) {
}
