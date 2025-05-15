package org.skypro.banking_service.telegramBot.telegramBotSystem;


import org.skypro.banking_service.model.dto.RecommendationDto;
import org.skypro.banking_service.model.dto.RecommendationResponse;
import org.skypro.banking_service.repositories.h2.repository.UserTransactionRepository;
import org.skypro.banking_service.service.RecommendationService;
import org.skypro.banking_service.telegramBot.Repository.TelegramUserRepository;
import org.skypro.banking_service.telegramBot.dto.InfoDTO;
import org.skypro.banking_service.telegramBot.dto.UserFullName;
import org.skypro.banking_service.telegramBot.model.TelegramUser;
import org.skypro.banking_service.telegramBot.service.RuleStatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private static final Logger logger = LoggerFactory.getLogger(TelegramBot.class);

    private final UserTransactionRepository userTransactionRepository;

    private final RecommendationService recommendationService;

    private final RuleStatService ruleStatService;

    private final InfoDTO infoDTO;

    private final TelegramUserRepository telegramUserRepository;

    private final String botUsername;

    private final String botToken;


    public TelegramBot(
            UserTransactionRepository userTransactionRepository,
            RecommendationService recommendationService,
            RuleStatService ruleStatService,
            TelegramUserRepository telegramUserRepository,
            InfoDTO infoDTO,
            @Value("${telegram.bot.username}") String botUsername,
            @Value("${telegram.bot.token}") String botToken
    ) {
        this.userTransactionRepository = userTransactionRepository;
        this.recommendationService = recommendationService;
        this.ruleStatService = ruleStatService;
        this.telegramUserRepository = telegramUserRepository;
        this.infoDTO = infoDTO;
        this.botUsername = botUsername;
        this.botToken = botToken;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println("Получен апдейт: " + update);
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText().trim();
            Long chatId = update.getMessage().getChatId();
            String username = update.getMessage().getFrom().getUserName();

            registerUserIfNotExists(chatId, username);
            handleCommand(text, chatId);
        }
    }

    private void registerUserIfNotExists(Long chatId, String username) {
        telegramUserRepository.findById(chatId)
                .orElseGet(() -> telegramUserRepository.save(new TelegramUser(chatId, username)));
    }

    private void handleCommand(String text, Long chatId) {
        if (text.equals("/start")) {
            sendMessage(chatId, """
                    Привет!
                    
                    Я бот по рекомендациям банковских продуктов. Вот что я умею:
                    
                    /recommend username — получить персональные рекомендации
                    /rule/stats — статистика
                    /management/info — версия сервиса
                    /management/clear-caches — сбросить кэш
                    """);
        } else if (text.equals("/rule/stats")) {
            String stats = ruleStatService.getAllStats().toString();
            sendMessage(chatId, "Статистика:\n" + stats);

        } else if (text.equals("/management/info")) {
            String name = infoDTO.name();
            String version = infoDTO.version();
            sendMessage(chatId, "Сервис: " + name + "\nВерсия: " + version);

        } else if (text.equals("/management/clear-caches")) {
            recommendationService.clearCache();
            sendMessage(chatId, "Кэш очищен.");

        } else if (text.startsWith("/recommend ")) {
            String username = text.substring("/recommend ".length()).trim();
            handleRecommendationCommand(chatId, username);

        } else {
            sendMessage(chatId, "Я не знаю такой команды. Попробуй /recommend.");
        }
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        executeSafely(message);
    }

    private void executeSafely(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            logger.error("Ошибка при отправке сообщения в Telegram. chatId={}, текст='{}'",
                    message.getChatId(), message.getText(), e);
        }
    }

    private void handleRecommendationCommand(Long chatId, String username) {
        Optional<UserFullName> optionalUser = userTransactionRepository.findUserFullNameByUsername(username);

        if (optionalUser.isEmpty()) {
            sendMessage(chatId, "Пользователь не найден.");
            return;
        }

        UserFullName user = optionalUser.get();

        RecommendationResponse response = recommendationService.getRecommendations(user.id());

        if (response.getRecommendations() == null || response.getRecommendations().isEmpty()) {
            sendMessage(chatId, "Новых рекомендаций нет.");
            return;
        }

        StringBuilder message = new StringBuilder("Здравствуйте, ")
                .append(user.firstName())
                .append(" ")
                .append(user.lastName())
                .append("!\n\n")
                .append("Новые продукты для вас:\n");

        for (RecommendationDto dto : response.getRecommendations()) {
            message.append("• ").append(dto.getName()).append(" — ").append(dto.getText()).append("\n");
        }

        sendMessage(chatId, message.toString());

    }

}
