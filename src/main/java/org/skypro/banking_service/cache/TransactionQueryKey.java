package org.skypro.banking_service.cache;

import java.util.UUID;

public record TransactionQueryKey(
        UUID userId,
        String productType,
        String transactionType) {
}
