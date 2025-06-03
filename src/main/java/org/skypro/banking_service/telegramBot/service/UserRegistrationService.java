package org.skypro.banking_service.telegramBot.service;

public interface UserRegistrationService {

    void registerUserIfNotExists(Long chatId, String username);
}
