package org.skypro.banking_service.telegramBot.service.impl;

import org.skypro.banking_service.telegramBot.model.TelegramUser;
import org.skypro.banking_service.telegramBot.repository.TelegramUserRepository;
import org.skypro.banking_service.telegramBot.service.UserRegistrationService;
import org.springframework.stereotype.Service;

@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {

    private final TelegramUserRepository repository;

    public UserRegistrationServiceImpl(TelegramUserRepository repository) {
        this.repository = repository;
    }

    /**
     * Метод регистрирует нового пользователя в БД.
     *
     * @param chatId   - id чата.
     * @param username - имя пользователя.
     */
    @Override
    public void registerUserIfNotExists(Long chatId, String username) {
        if (!repository.existsByChatId(chatId)) {
            repository.save(new TelegramUser(chatId, username));
        }
    }
}
