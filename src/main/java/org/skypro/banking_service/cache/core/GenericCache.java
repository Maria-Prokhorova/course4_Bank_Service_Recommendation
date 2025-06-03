package org.skypro.banking_service.cache.core;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class GenericCache<K, V> implements CacheWrapper<K, V> {
    private final Cache<K, V> cache;

    public GenericCache(long maxSize, long expireAfterWriteMinutes) {
        Caffeine<Object, Object> builder = Caffeine.newBuilder()
                .maximumSize(maxSize);
        if (expireAfterWriteMinutes > 0) {
            builder = builder.expireAfterWrite(expireAfterWriteMinutes, TimeUnit.MINUTES);
        }
        this.cache = builder.build();
    }

    @Override
    public V get(K key, Function<K, V> loader) {
        return cache.get(key, loader);
    }

    @Override
    public void invalidateAll() {
        cache.invalidateAll();
    }

    @Override
    public V getIfPresent(K key) {
        return cache.getIfPresent(key);
    }
}
