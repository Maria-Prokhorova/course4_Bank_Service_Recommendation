package org.skypro.banking_service.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class UserTransactionCache {

    public final Cache<UUID, List<String>> userTypeProductCache =
            Caffeine.newBuilder()
                    .maximumSize(10_000)
                    .removalListener((key, value, cause)
                            -> System.out.println("Cleared cache entry"))
                    .build();

    public final Cache<TransactionQueryKey, Long> totalAmountCache =
            Caffeine.newBuilder()
                    .maximumSize(10_000)
                    .removalListener((key, value, cause)
                            -> System.out.println("Cleared cache entry"))
                    .build();

    public final Cache<UserProductKey, Boolean> userProductExsistsCache =
            Caffeine.newBuilder()
                    .maximumSize(10_000)
                    .removalListener((key, value, cause)
                            -> System.out.println("Cleared cache entry"))
                    .build();
}
