package org.skypro.banking_service.telegramBot.service.impl;

import org.skypro.banking_service.telegramBot.Repository.TelegramUserRepository;
import org.skypro.banking_service.telegramBot.model.TelegramUser;
import org.skypro.banking_service.telegramBot.service.TelegramUserService;
import org.springframework.stereotype.Service;

@Service
public class TelegramUserServiceImpl implements TelegramUserService {

    private final TelegramUserRepository repository;

    public TelegramUserServiceImpl(TelegramUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean isFirstTime(Long chatId) {
        return !repository.existsByChatId(chatId);
    }

    @Override
    public void register(Long chatId, String username) {
        if (!repository.existsByChatId(chatId)) {
            repository.save(new TelegramUser(chatId, username));
        }
    }
}
