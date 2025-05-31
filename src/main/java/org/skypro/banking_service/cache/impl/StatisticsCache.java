package org.skypro.banking_service.cache.impl;

import org.skypro.banking_service.cache.core.GenericCache;
import org.skypro.banking_service.dto.StatisticsDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
public class StatisticsCache {
    private final GenericCache<String, List<StatisticsDTO>> cache;

    public StatisticsCache() {
        this.cache = new GenericCache<>(100, 30);
    }

    public List<StatisticsDTO> get(String key, Function<String, List<StatisticsDTO>> loader) {
        return cache.get(key, loader);
    }

    public void invalidateAll() {
        cache.invalidateAll();
    }
}

