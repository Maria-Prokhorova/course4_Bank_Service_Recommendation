package org.skypro.banking_service.cache.keys;

import java.util.UUID;

public record UserProductKey(
        UUID userId,
        String productType) {
}
