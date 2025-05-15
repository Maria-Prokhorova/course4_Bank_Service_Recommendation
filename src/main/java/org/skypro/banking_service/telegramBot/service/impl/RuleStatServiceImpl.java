package org.skypro.banking_service.telegramBot.service.impl;

import org.skypro.banking_service.service.DynamicRulesService;
import org.skypro.banking_service.telegramBot.Repository.RuleStatRepository;
import org.skypro.banking_service.telegramBot.dto.RuleStatsDTO;
import org.skypro.banking_service.telegramBot.model.RuleStat;
import org.skypro.banking_service.telegramBot.service.RuleIdsProvider;
import org.skypro.banking_service.telegramBot.service.RuleStatService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class RuleStatServiceImpl implements RuleStatService {

    private final RuleStatRepository ruleStatRepository;

    private final RuleIdsProvider ruleIdsProvider;

    public RuleStatServiceImpl(RuleStatRepository ruleStatRepository,
                               RuleIdsProvider ruleIdsProvider) {
        this.ruleStatRepository = ruleStatRepository;
        this.ruleIdsProvider = ruleIdsProvider;
    }

    @Transactional
    @Override
    public void incrementCounter(UUID ruleId) {
        if (!ruleStatRepository.existsById(ruleId)) {
            ruleStatRepository.save(new RuleStat(ruleId, 0L));
        }
        ruleStatRepository.incrementCount(ruleId);
    }

    @Override
    public void deleteStat(UUID ruleId) {
        ruleStatRepository.deleteById(ruleId);
    }

    @Override
    public List<RuleStatsDTO> getAllStats() {
        List<UUID> allRuleIds = ruleIdsProvider.getAllRuleProductIds();
        List<RuleStat> statsFromDb = ruleStatRepository.findAll();

        Map<UUID, Long> dbMap = statsFromDb.stream()
                .collect(Collectors.toMap(RuleStat::getRuleId, RuleStat::getCount));

        return allRuleIds.stream()
                .map(ruleId -> new RuleStatsDTO(ruleId, dbMap.getOrDefault(ruleId, 0L)))
                .collect(Collectors.toList());
    }
}
