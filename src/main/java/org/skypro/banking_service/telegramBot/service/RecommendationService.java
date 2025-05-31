package org.skypro.banking_service.telegramBot.service;

public interface RecommendationService {
    void handleRecommendationCommand(Long chatId, String userName);
}
