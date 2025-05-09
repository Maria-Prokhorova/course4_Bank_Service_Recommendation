package org.skypro.banking_service.rulesystem.dynasmicRuleSystem;

import java.util.List;
import java.util.UUID;

public interface ConditionExecutor {

    boolean supports(String queryType);
    boolean evaluate(UUID userId, List<String> arguments, boolean negate);
}
