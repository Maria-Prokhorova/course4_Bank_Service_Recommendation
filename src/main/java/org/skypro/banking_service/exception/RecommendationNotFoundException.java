package org.skypro.banking_service.exception;

public class RecommendationNotFoundException extends RuntimeException{
    public RecommendationNotFoundException(String message) {
        super(message);
    }
}
