package org.skypro.banking_service.telegramBot.service;

public interface CommandProcessorService {
    void processCommand(String text, Long chatId);
}
