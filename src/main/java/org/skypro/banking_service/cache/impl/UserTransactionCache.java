package org.skypro.banking_service.cache.impl;

import org.skypro.banking_service.cache.core.GenericCache;
import org.skypro.banking_service.cache.keys.TransactionQueryKey;
import org.skypro.banking_service.cache.keys.UserProductKey;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class UserTransactionCache {
    // это кеш.

    public final GenericCache<UUID, List<String>> userTypeProductCache;


    public final GenericCache<TransactionQueryKey, Long> totalAmountCache;


    public final GenericCache<UserProductKey, Boolean> userProductExistsCache;

    public UserTransactionCache() {
        this.userTypeProductCache = new GenericCache<>(100, 100);
        this.totalAmountCache = new GenericCache<>(100, 100);
        this.userProductExistsCache = new GenericCache<>(100, 100);
    }
}

