package org.skypro.banking_service.telegramBot.service.impl;

import org.skypro.banking_service.telegramBot.repository.TelegramUserRepository;
import org.skypro.banking_service.telegramBot.model.TelegramUser;
import org.skypro.banking_service.telegramBot.service.TelegramUserService;
import org.springframework.stereotype.Service;

@Service
public class TelegramUserServiceImpl implements TelegramUserService {

    private final TelegramUserRepository repository;

    public TelegramUserServiceImpl(TelegramUserRepository repository) {
        this.repository = repository;
    }

    /**
     * Метод проверяет существование чата в БД.
     *
     * @param chatId - id чата.
     * @return булевое значение: true или false.
     */
    @Override
    public boolean isFirstTime(Long chatId) {
        return !repository.existsByChatId(chatId);
    }

    /**
     * Метод регистрирует нового пользователя в БД.
     *
     * @param chatId   - id чата.
     * @param username - имя пользователя.
     */
    @Override
    public void register(Long chatId, String username) {
        if (!repository.existsByChatId(chatId)) {
            repository.save(new TelegramUser(chatId, username));
        }
    }
}
