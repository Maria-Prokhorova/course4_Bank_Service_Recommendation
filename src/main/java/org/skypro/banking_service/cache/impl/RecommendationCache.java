package org.skypro.banking_service.cache.impl;

import org.skypro.banking_service.cache.core.GenericCache;
import org.skypro.banking_service.dto.RecommendationResponse;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.function.Function;

@Component
public class RecommendationCache {
    private final GenericCache<UUID, RecommendationResponse> cache;

    public RecommendationCache() {
        this.cache = new GenericCache<>(100, 60);
    }

    public RecommendationResponse get(UUID key, Function<UUID, RecommendationResponse> loader) {
        return cache.get(key, loader);
    }

    public void invalidateAll() {
        cache.invalidateAll();
    }
}

