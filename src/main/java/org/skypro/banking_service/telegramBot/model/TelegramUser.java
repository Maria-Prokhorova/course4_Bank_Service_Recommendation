package org.skypro.banking_service.telegramBot.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "telegram_user")
public class TelegramUser {

    @Id
    private Long chatId;

    private String userName;

    public TelegramUser(Long chatId, String userName) {
        this.chatId = chatId;
        this.userName = userName;
    }

    public TelegramUser() {

    }
}
