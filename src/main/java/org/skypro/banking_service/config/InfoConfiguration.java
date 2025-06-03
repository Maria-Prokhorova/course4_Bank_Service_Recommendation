package org.skypro.banking_service.config;

import org.skypro.banking_service.dto.InfoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfoConfiguration {

    @Bean
    public InfoDTO infoDTO(
            @Value("${info.name}") String name,
            @Value("${info.version}") String version) {
        return new InfoDTO(name, version);
    }
}