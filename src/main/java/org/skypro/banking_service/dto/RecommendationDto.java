package org.skypro.banking_service.dto;


import lombok.Data;

@Data
public class RecommendationDto {

    private String id;
    private String name;
    private String text;

    public RecommendationDto() {
    }

    public RecommendationDto(String id, String name, String text) {
        this.id = id;
        this.name = name;
        this.text = text;
    }
}
