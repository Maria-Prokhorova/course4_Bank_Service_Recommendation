package org.skypro.banking_service.telegramBot.telegramBotSystem;

import org.skypro.banking_service.telegramBot.service.CommandProcessorService;
import org.skypro.banking_service.telegramBot.service.UserRegistrationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final String botUsername;
    private final String botToken;
    private final CommandProcessorService commandProcessor;
    private final UserRegistrationService userRegistrationService;

    public TelegramBot(
            @Value("${telegram.bot.username}") String botUsername,
            @Value("${telegram.bot.token}") String botToken,
            CommandProcessorService commandProcessor,
            UserRegistrationService userRegistrationService) {
        this.botUsername = botUsername;
        this.botToken = botToken;
        this.commandProcessor = commandProcessor;
        this.userRegistrationService = userRegistrationService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!isValidUpdate(update)) {
            return;
        }
            String text = update.getMessage().getText().trim();
            Long chatId = update.getMessage().getChatId();
            String userName = update.getMessage().getFrom().getUserName();

        userRegistrationService.registerUserIfNotExists(chatId, userName);
        commandProcessor.processCommand(text, chatId);

    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    private boolean isValidUpdate(Update update) {
        return update.hasMessage() && update.getMessage().hasText();
    }
}