package org.skypro.banking_service.mapper;

import org.skypro.banking_service.dto.DynamicRuleConditionDto;
import org.skypro.banking_service.dto.DynamicRuleDto;
import org.skypro.banking_service.model.DynamicRule;
import org.skypro.banking_service.model.RuleCondition;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DynamicRuleMapper {

    public DynamicRule toEntity(DynamicRuleDto dto) {
        DynamicRule rule = new DynamicRule();
        rule.setId(dto.id());
        rule.setProductName(dto.productName());

        List<RuleCondition> conditions = new ArrayList<>();
        for (DynamicRuleConditionDto condDto : dto.conditions()) {
            RuleCondition condition = new RuleCondition();
            condition.setQuery(condDto.query());
            condition.setArguments(condDto.arguments());
            condition.setNegate(condDto.negate());
            condition.setRule(rule);
            conditions.add(condition);
        }

        rule.setConditions(conditions);
        return rule;
    }

    public DynamicRuleDto toDto(DynamicRule rule) {
        List<DynamicRuleConditionDto> conditionDtos = new ArrayList<>();
        for (RuleCondition condition : rule.getConditions()) {
            conditionDtos.add(new DynamicRuleConditionDto(
                    condition.getQuery(),
                    condition.getArguments(),
                    condition.isNegate()
            ));
        }

        return new DynamicRuleDto(
                rule.getId(),
                rule.getProductName(),
                conditionDtos
        );
    }

    public List<DynamicRuleDto> toDtoList(List<DynamicRule> rules) {
        List<DynamicRuleDto> result = new ArrayList<>();
        for (DynamicRule rule : rules) {
            result.add(toDto(rule));
        }
        return result;
    }
}

