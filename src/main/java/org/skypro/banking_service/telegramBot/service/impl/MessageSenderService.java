package org.skypro.banking_service.telegramBot.service.impl;

import org.skypro.banking_service.telegramBot.service.MessageSender;
import org.skypro.banking_service.telegramBot.telegramBotSystem.TelegramBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class MessageSenderService implements MessageSender {

    private static final Logger logger = LoggerFactory.getLogger(MessageSenderService.class);
    private final TelegramBot telegramBot;

    @Lazy
    public MessageSenderService(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @Override
    public void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        executeSafely(message);
    }

    private void executeSafely(SendMessage message) {
        try {
            telegramBot.execute(message);
        } catch (TelegramApiException e) {
            logger.error("Ошибка при отправке сообщения в Telegram. chatId={}, текст='{}'",
                    message.getChatId(), message.getText(), e);
        }
    }
}

