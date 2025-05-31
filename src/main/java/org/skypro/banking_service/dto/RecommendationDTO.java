package org.skypro.banking_service.dto;

import lombok.Data;
import org.skypro.banking_service.model.Recommendation;

@Data
public class RecommendationDTO {

    private String name;
    private String id;
    private String text;

    public RecommendationDTO(String name, String id, String text) {
        this.name = name;
        this.id = id;
        this.text = text;
    }

    /**
     * Статический метод, который преобразует сущность {@link Recommendation} в DTO {@link RecommendationDTO}.
     * Используется для возврата клиенту только нужных данных.
     */
    public static RecommendationDTO convertToDto(Recommendation recommendation) {
        return new RecommendationDTO(
                recommendation.getProductName(),
                recommendation.getProductId().toString(),
                recommendation.getProductText()
        );
    }

}
