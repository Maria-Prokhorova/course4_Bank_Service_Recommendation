package org.skypro.banking_service.telegramBot.telegramBotSystem;

import org.skypro.banking_service.dto.RecommendationDTO;
import org.skypro.banking_service.dto.RecommendationResponse;
import org.skypro.banking_service.repositories.h2.repository.UserTransactionRepository;
import org.skypro.banking_service.service.RecommendationForClientService;
import org.skypro.banking_service.telegramBot.dto.UserFullName;
import org.skypro.banking_service.telegramBot.service.TelegramUserService;
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

    private final TelegramUserService telegramUserService;

    private final UserTransactionRepository userTransactionRepository;

    private final RecommendationForClientService recommendationForClientService;

    private final String botUsername;

    private final String botToken;

    public TelegramBot(
            TelegramUserService telegramUserService,
            UserTransactionRepository userTransactionRepository,
            RecommendationForClientService recommendationForClientService,
            @Value("${telegram.bot.username}") String botUsername,
            @Value("${telegram.bot.token}") String botToken
    ) {
        this.telegramUserService = telegramUserService;
        this.userTransactionRepository = userTransactionRepository;
        this.recommendationForClientService = recommendationForClientService;
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
        logger.info("Получен апдейт: {}", update);

        // Если ботом получено сообщение
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText().trim();
            Long chatId = update.getMessage().getChatId();
            String userName = update.getMessage().getFrom().getUserName();

            // Регистрируем пользователя, если он не существует в БД
            registerUserIfNotExists(chatId, userName);

            // Отправляем текст сообщения обработчику команд
            handleCommand(text, chatId);
        }
    }

    /**
     * Метод, который регистрирует пользователя, если он не существует в БД.
     * Предварительно выполняя проверку, действительно пользователь отсутствует в БД.
     *
     * @param chatId   - id чата.
     * @param userName - имя пользователя.
     */
    private void registerUserIfNotExists(Long chatId, String userName) {
        // Если пользователь отсутствует в БД
        if (telegramUserService.isFirstTime(chatId)) {
            //Выполняется его регистрация
            telegramUserService.register(chatId, userName);
        }
    }

    /**
     * Метод, который обрабатывает команды бота.
     *
     * @param text   - текст сообщения.
     * @param chatId - id чата.
     */
    private void handleCommand(String text, Long chatId) {
        // Обработка команды "start" - выдаем приветствие
        if (text.equals("/start")) {
            sendMessage(chatId, """
                    Привет!
                    
                    Я бот банка «Стар». Я могу для вас подготовить список банковских продуктов. Для этого воспользуйтесь командой:
                    
                    /recommend username — получить персональные рекомендации
                    """);
        }
        // Обработка команды "recommend"
        else if (text.startsWith("/recommend ")) {
            String username = text.substring("/recommend ".length()).trim();
            // Передаем обработку такого сообщения обработчику команды рекомендаций
            handleRecommendationCommand(chatId, username);
        }
        // Если пришло сообщение с другой командой
        else {
            sendMessage(chatId, "Я не знаю такой команды. Попробуй /recommend username.");
        }
    }

    /**
     * Метод подготовки сообщения для отправки в телеграм-бот
     *
     * @param chatId - id чата.
     * @param text   - текст сообщения.
     */
    private void sendMessage(Long chatId, String text) {
        // Создаем сообщение для отправки
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);

        // Вызываем метод отправки сообщения
        executeSafely(message);
    }

    /**
     * Метод безопасной отправки сообщения.
     *
     * @param message - сообщение для отправки
     * @throws TelegramApiException в случае сбоя выбрасываем исключение
     */
    private void executeSafely(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            logger.error("Ошибка при отправке сообщения в Telegram. chatId={}, текст='{}'",
                    message.getChatId(), message.getText(), e);
        }
    }

    /**
     * Метод, который обрабатывает команду "/recommend", подготавливает рекомендации для клиента.
     *
     * @param chatId   - id чата
     * @param userName - имя пользователя.
     */
    private void handleRecommendationCommand(Long chatId, String userName) {
        // Находим в БД полную информацию (имя и фамилию) пользователя по егу имени
        Optional<UserFullName> optionalUser = userTransactionRepository.findUserFullNameByUsername(userName);

        // Если пользователь в БД не найдет, отправляем сообщение об этом в бот
        if (optionalUser.isEmpty()) {
            sendMessage(chatId, "Пользователь не найден.");
            return;
        }

        UserFullName user = optionalUser.get();

        // Готовим рекомендации о банковских продуктах для пользователя
        RecommendationResponse response = recommendationForClientService.getRecommendationsForClient(user.id());

        // Если список рекомендаций пуст, отправляем сообщение об этом в бот
        if (response.getRecommendations() == null || response.getRecommendations().isEmpty()) {
            sendMessage(chatId, "Новых рекомендаций нет.");
            return;
        }

        // Если рекомендации для клиента есть, готовим и отправляем их в сообщении в бот
        StringBuilder message = new StringBuilder("Здравствуйте, ")
                .append(user.firstName())
                .append(" ")
                .append(user.lastName())
                .append("!\n\n")
                .append("Новые продукты для вас:\n");

        for (RecommendationDTO dto : response.getRecommendations()) {
            message.append("• ").append(dto.getName()).append(" — ").append(dto.getText()).append("\n");
        }

        sendMessage(chatId, message.toString());
    }
}
