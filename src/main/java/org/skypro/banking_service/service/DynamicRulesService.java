package org.skypro.banking_service.service;

import org.skypro.banking_service.model.Recommendation;
import org.skypro.banking_service.dto.StatisticsDTO;

import java.util.List;
import java.util.UUID;

public interface DynamicRulesService {

    // Добавление банковского продукта с динамическим правилом.
    Recommendation addProductWithDynamicRule(Recommendation recommendation);

    // Получение списка всех банковских продуктов с динамическими правилами.
    List<Recommendation> getAllProductsWithDynamicRule();

    // Удаление банковского продукта с его динамическим правилом.
    void deleteProductWithDynamicRule(UUID id);

    // Получение статистики срабатывания правил рекомендаций.
    List<StatisticsDTO> getStatistics();
}
