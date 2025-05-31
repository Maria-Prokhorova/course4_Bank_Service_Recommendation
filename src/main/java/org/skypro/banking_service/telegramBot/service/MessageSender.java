package org.skypro.banking_service.telegramBot.service;

public interface MessageSender {
    void sendMessage(Long chatId, String text);
}
