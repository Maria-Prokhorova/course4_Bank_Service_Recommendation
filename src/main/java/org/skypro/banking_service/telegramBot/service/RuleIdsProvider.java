package org.skypro.banking_service.telegramBot.service;

import java.util.List;
import java.util.UUID;

public interface RuleIdsProvider {

    List<UUID> getAllRuleProductIds();
}
