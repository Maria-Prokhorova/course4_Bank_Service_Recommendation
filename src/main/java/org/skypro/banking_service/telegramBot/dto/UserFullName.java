package org.skypro.banking_service.telegramBot.dto;

import java.util.UUID;

public record UserFullName(
        UUID id,
        String firstName,
        String lastName) {
}
