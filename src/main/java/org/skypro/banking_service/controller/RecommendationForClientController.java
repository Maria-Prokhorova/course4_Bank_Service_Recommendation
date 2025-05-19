package org.skypro.banking_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.skypro.banking_service.dto.RecommendationResponse;
import org.skypro.banking_service.service.RecommendationForClientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Рекомендации для клиентов банка.", description = "Позволяет получить список рекомендаций банковских продуктов " +
        "для интересующего клиента.")
@RestController
@RequestMapping("/recommendation")
public class RecommendationForClientController {

    private final RecommendationForClientService recommendationForClientService;

    public RecommendationForClientController(RecommendationForClientService recommendationForClientService) {
        this.recommendationForClientService = recommendationForClientService;
    }

    @Operation(summary = "Получение списка рекомендаций для клиента банка.", description = "В ответе возвращается список" +
            " рекомендаций по банковским продуктам.")
    @GetMapping("/{userId}")
    public RecommendationResponse getRecommendations(@Parameter(
            description = "ID клиента, для которого хотим подобрать рекомендации банковских продуктов.",
            required = true)
                                                     @PathVariable UUID userId) {
        return recommendationForClientService.getRecommendationsForClient(userId);
    }
}
