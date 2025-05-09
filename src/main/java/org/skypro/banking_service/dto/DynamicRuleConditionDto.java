package org.skypro.banking_service.dto;

import java.util.List;

public record DynamicRuleConditionDto(
        String query,
        List<String> arguments,
        boolean negate) {

}
