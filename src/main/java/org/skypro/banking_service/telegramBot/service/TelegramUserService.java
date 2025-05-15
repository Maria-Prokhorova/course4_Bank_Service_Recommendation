package org.skypro.banking_service.telegramBot.service;

public interface TelegramUserService {

    boolean isFirstTime(Long chatId);

    void register(Long chatId, String username);
}
