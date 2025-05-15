package org.skypro.banking_service.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Schema(description = "Ответ с рекомендациями")
public class RecommendationResponse {

    @Schema(description = "ID пользователя", example = "cd515076-5d8a-44be-930e-8d4fcb79f42d")
    private UUID userId;

    @Schema(description = "Список рекомендаций")
    private List<RecommendationDto> recommendations;

    public RecommendationResponse() {
    }

    public RecommendationResponse(UUID userId, List<RecommendationDto> recommendations) {
        this.userId = userId;
        this.recommendations = recommendations;
    }
}