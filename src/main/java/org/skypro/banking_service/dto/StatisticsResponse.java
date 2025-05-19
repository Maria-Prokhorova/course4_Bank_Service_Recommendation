package org.skypro.banking_service.dto;

import java.util.List;

public record StatisticsResponse(
        List<StatisticsDTO> stats) {
}
