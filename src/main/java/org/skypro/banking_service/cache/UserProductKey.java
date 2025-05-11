package org.skypro.banking_service.cache;

import java.util.UUID;

public record UserProductKey(
        UUID userId,
        String productType) {
}
