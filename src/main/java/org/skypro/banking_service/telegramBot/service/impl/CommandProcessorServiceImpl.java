package org.skypro.banking_service.telegramBot.service.impl;

import org.skypro.banking_service.telegramBot.service.CommandProcessorService;
import org.skypro.banking_service.telegramBot.service.MessageSender;
import org.skypro.banking_service.telegramBot.service.RecommendationService;
import org.springframework.stereotype.Service;

@Service
public class CommandProcessorServiceImpl implements CommandProcessorService {

    private final MessageSender messageSender;
    private final RecommendationService recommendationService;

    public CommandProcessorServiceImpl(
            MessageSender messageSender,
            RecommendationService recommendationService) {
        this.messageSender = messageSender;
        this.recommendationService = recommendationService;
    }

    @Override
    public void processCommand(String text, Long chatId) {
        if (text.equals("/start")) {
            handleStartCommand(chatId);
        } else if (text.startsWith("/recommend ")) {
            handleRecommendCommand(chatId, text.substring("/recommend ".length()).trim());
        } else {
            messageSender.sendMessage(chatId, "Я не знаю такой команды. Попробуй /recommend username.");
        }
    }

    private void handleStartCommand(Long chatId) {
        messageSender.sendMessage(chatId, """
                Привет!
                
                Я бот банка «Стар». Я могу для вас подготовить список банковских продуктов. Для этого воспользуйтесь командой:
                
                /recommend username — получить персональные рекомендации
                """);
    }

    private void handleRecommendCommand(Long chatId, String username) {
        recommendationService.handleRecommendationCommand(chatId, username);
    }
}

