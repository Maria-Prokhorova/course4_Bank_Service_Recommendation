package org.skypro.banking_service.telegramBot.service;

import org.skypro.banking_service.telegramBot.dto.RuleStatsDTO;

import java.util.List;
import java.util.UUID;

public interface RuleStatService {

    void incrementCounter(UUID ruleId);

    void deleteStat(UUID ruleId);

    List<RuleStatsDTO> getAllStats();
}
