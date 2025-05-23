package org.skypro.banking_service.dto;

import lombok.Data;

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

}
