package org.skypro.banking_service.service;

import org.skypro.banking_service.dto.DynamicRuleDto;
import org.skypro.banking_service.model.DynamicRule;

import java.util.List;
import java.util.UUID;

public interface DynamicRuleService {

    void createRule(DynamicRuleDto dto);

    List<DynamicRuleDto> getAllRules();

    void deleteRule(UUID id);
}
