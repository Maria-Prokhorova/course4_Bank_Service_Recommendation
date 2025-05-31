package org.skypro.banking_service.exception;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(UUID userId) {
        super("Клиент с id: " + userId + " в БД не найдет.");
    }
}
