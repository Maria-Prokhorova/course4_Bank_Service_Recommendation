package org.skypro.banking_service.service.impl;

import org.skypro.banking_service.dto.DynamicRuleDto;
import org.skypro.banking_service.mapper.DynamicRuleMapper;
import org.skypro.banking_service.model.DynamicRule;
import org.skypro.banking_service.repository.rules.DynamicRuleRepository;
import org.skypro.banking_service.service.DynamicRuleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DynamicRuleServiceImpl implements DynamicRuleService {

    private final DynamicRuleRepository repository;
    private final DynamicRuleMapper mapper;

    public DynamicRuleServiceImpl(DynamicRuleRepository repository, DynamicRuleMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void createRule(DynamicRuleDto ruleDto) {
        DynamicRule rule = mapper.toEntity(ruleDto);
        if (rule.getConditions().isEmpty()) {
            throw new IllegalStateException("Правило должно иметь хотя бы одно условие");
        }
        repository.save(rule);
    }

    @Override
    public List<DynamicRuleDto> getAllRules() {
        return mapper.toDtoList(repository.findAll());
    }

    @Override
    public void deleteRule(UUID id) {
        repository.deleteById(id);
    }
}

