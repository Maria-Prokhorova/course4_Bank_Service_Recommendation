package org.skypro.banking_service.config;

import org.skypro.banking_service.dto.InfoDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfoConfiguration {

    @Bean
    public InfoDTO infoDTO() {

        // Инициализация InfoDTO с использованием значений из application.properties
        return new InfoDTO("Banking Service", "1.0.0");
    }
}