package org.skypro.banking_service.telegramBot.Repository;

import org.skypro.banking_service.telegramBot.model.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TelegramUserRepository extends JpaRepository<TelegramUser, Long> {
    boolean existsByChatId(Long chatId);
}
