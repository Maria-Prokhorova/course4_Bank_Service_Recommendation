package org.skypro.banking_service.service.ruleSystem.dynamicRulesSystem;

import org.skypro.banking_service.model.QueryRules;

import java.util.UUID;

public interface DynamicRule {

    // Проверка подходит ли банковский продукт с динамическими правилами заданному клиенту.
    boolean checkOutDinamicRule(QueryRules queries, UUID userId);
}
