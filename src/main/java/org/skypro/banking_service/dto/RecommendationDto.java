package org.skypro.banking_service.dto;


import lombok.Data;

import java.util.UUID;

@Data
public class RecommendationDto {

    private String name;
    private String id;
    private String text;

    public RecommendationDto(String name, String id, String text) {
        this.name = name;
        this.id = id;
        this.text = text;
    }

}
