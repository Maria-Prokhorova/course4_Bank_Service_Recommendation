package org.skypro.banking_service.service.statistics;

import java.util.List;
import java.util.UUID;

public interface MonitoringStatistics {

    // Получение списка всех id правил.
    List<UUID> getAllRuleIDs();

    // Увеличение счетчика срабатывания указанного правила в базе ведения статистики.
    void incrementCounter(UUID ruleId);

    // Удаление правила из мониторинга статистики по его id.
    void deleteRuleIdFromStatistics(UUID ruleId);
}
