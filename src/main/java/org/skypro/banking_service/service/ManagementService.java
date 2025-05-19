package org.skypro.banking_service.service;

import org.springframework.cache.annotation.CacheEvict;

public interface ManagementService {

    // Метод сброса кеша рекомендаций.
    @CacheEvict(value = "recommendations", allEntries = true)
    void clearCache();
}
