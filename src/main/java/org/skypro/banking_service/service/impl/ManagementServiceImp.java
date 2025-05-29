package org.skypro.banking_service.service.impl;

import org.skypro.banking_service.service.ManagementService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

/**
 * Сервис для управления системой, позволяет сбросить кеш рекомендаций.
 */
@Service
public class ManagementServiceImp implements ManagementService {

    /**
     * Метод очищает все закешированные результаты, что означает обновление базы данных.
     * Spring автоматически очистит кеш благодаря аннотации.
     */
    @CacheEvict(value = "recommendations", allEntries = true)
    @Override
    public void clearCache() {
    }
}
