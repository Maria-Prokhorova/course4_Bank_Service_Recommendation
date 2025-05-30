package org.skypro.banking_service.telegramBot.service.impl;

import org.skypro.banking_service.dto.RecommendationDTO;
import org.skypro.banking_service.dto.RecommendationResponse;
import org.skypro.banking_service.repositories.h2.repository.UserTransactionRepository;
import org.skypro.banking_service.service.RecommendationForClientService;
import org.skypro.banking_service.telegramBot.dto.UserFullName;
import org.skypro.banking_service.telegramBot.service.RecommendationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecommendationServiceImpl implements RecommendationService {
    private final UserTransactionRepository userTransactionRepository;
    private final RecommendationForClientService recommendationForClientService;
    private final MessageSenderService messageSender;

    public RecommendationServiceImpl(
            UserTransactionRepository userTransactionRepository,
            RecommendationForClientService recommendationForClientService,
            MessageSenderService messageSender) {
        this.userTransactionRepository = userTransactionRepository;
        this.recommendationForClientService = recommendationForClientService;
        this.messageSender = messageSender;
    }

    @Override
    public void handleRecommendationCommand(Long chatId, String userName) {
        Optional<UserFullName> optionalUser = userTransactionRepository.findUserFullNameByUsername(userName);

        if (optionalUser.isEmpty()) {
            sendUserNotFoundMessage(chatId);
            return;
        }

        UserFullName user = optionalUser.get();
        RecommendationResponse response = recommendationForClientService.getRecommendationsForClient(user.id());

        if (response.getRecommendations() == null || response.getRecommendations().isEmpty()) {
            sendNoRecommendationsMessage(chatId);
            return;
        }

        String message = buildRecommendationMessage(user, response.getRecommendations());
        messageSender.sendMessage(chatId, message);
    }

    private void sendUserNotFoundMessage(Long chatId) {
        messageSender.sendMessage(chatId, "Пользователь не найден.");
    }

    private void sendNoRecommendationsMessage(Long chatId) {
        messageSender.sendMessage(chatId, "Новых рекомендаций нет.");
    }

    private String buildRecommendationMessage(UserFullName user, List<RecommendationDTO> recommendations) {
        String fullName = user.firstName() + " " + user.lastName();
        StringBuilder message = new StringBuilder("Здравствуйте, ")
                .append(fullName)
                .append("!\n\n")
                .append("Новые продукты для вас:\n");

        for (RecommendationDTO dto : recommendations) {
            message.append("• ")
                    .append(dto.getName())
                    .append(" — ")
                    .append(dto.getText())
                    .append("\n");
        }

        return message.toString();
    }

}

