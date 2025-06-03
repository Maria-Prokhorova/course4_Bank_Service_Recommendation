package org.skypro.banking_service.cache.keys;

import java.util.UUID;

public record TransactionQueryKey(
        UUID userId,
        String productType,
        String transactionType) {
}
