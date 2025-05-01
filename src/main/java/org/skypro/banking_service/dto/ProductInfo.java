package org.skypro.banking_service.dto;


import lombok.Data;

@Data
public class ProductInfo {
    private String id;
    private String name;
    private String description;


    public ProductInfo(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
