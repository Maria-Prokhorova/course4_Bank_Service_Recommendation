package org.skypro.banking_service.telegramBot.service;

public interface TelegramUserService {

    // Метод проверки существования чата в БД
    boolean isFirstTime(Long chatId);

    // Метод регистрирует пользователя в БД
    void register(Long chatId, String username);
}
