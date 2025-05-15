package org.skypro.banking_service.telegramBot.config;

import org.skypro.banking_service.telegramBot.dto.InfoDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public InfoDTO infoDTO() {
        // Инициализация InfoDTO с использованием значений из application.properties
        return new InfoDTO("Banking Service", "1.0.0");
    }
}