package org.skypro.banking_service.telegramBot.service.impl;

import org.skypro.banking_service.telegramBot.Repository.RuleStatRepository;
import org.skypro.banking_service.telegramBot.dto.RuleStatsDTO;
import org.skypro.banking_service.telegramBot.model.RuleStat;
import org.skypro.banking_service.telegramBot.service.RuleStatService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RuleStatServiceImpl implements RuleStatService {

    private final RuleStatRepository ruleStatRepository;

    public RuleStatServiceImpl(RuleStatRepository ruleStatRepository) {
        this.ruleStatRepository = ruleStatRepository;
    }

    @Transactional
    @Override
    public void incrementCounter(UUID ruleId) {
        ruleStatRepository.findById(ruleId)
                .orElseGet(() -> ruleStatRepository.save(new RuleStat(ruleId, 0L)));
        ruleStatRepository.incrementCount(ruleId);
    }

    @Override
    public void deleteStat(UUID ruleId) {
        ruleStatRepository.deleteById(ruleId);
    }

    @Override
    public List<RuleStatsDTO> getAllStats() {
        return ruleStatRepository.findAll().stream()
                .map(stat -> new RuleStatsDTO(stat.getRuleId(), stat.getCount()))
                .collect(Collectors.toList());
    }
}
