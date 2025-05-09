package org.skypro.banking_service.dto;

import java.util.List;
import java.util.UUID;

public record DynamicRuleDto(
        UUID id,
        String productName,
        List<DynamicRuleConditionDto> conditions) {
}
