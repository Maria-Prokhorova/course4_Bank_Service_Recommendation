package org.skypro.banking_service.rule_system.static_rules.rules;

import org.skypro.banking_service.dto.RecommendationDto;
import org.skypro.banking_service.rule_system.static_rules.parameter.RuleParameters;
import org.skypro.banking_service.rule_system.static_rules.StaticQueryExecutorImpl;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import static org.skypro.banking_service.constants.ProductConstants.*;

@Component
public class StaticRuleTopSavingImp implements StaticRule {

    private final StaticQueryExecutorImpl rulesService;

    public StaticRuleTopSavingImp(StaticQueryExecutorImpl rulesService) {
        this.rulesService = rulesService;
    }

    @Override
    public Optional<RecommendationDto> checkOutStaticRule(UUID userId) {
        if (isEligibleForTopSaving(userId)) {
            return buildRecommendationDto();
        }
        return Optional.empty();
    }

    private boolean isEligibleForTopSaving(UUID userId) {
        return rulesService.isUsingProduct(
                RuleParameters.of(userId, TYPE_DEBIT))
               && rulesService.isAmountDepositsMoreThanWithdrawals(
                RuleParameters.of(userId, TYPE_DEBIT))
               && rulesService.isAmountSeveralDepositsMoreOrEqualsLimit(
                RuleParameters.of(userId, TYPE_DEBIT, TYPE_SAVING, LIMIT_TOP_SAVING));
    }

    private Optional<RecommendationDto> buildRecommendationDto() {
        return Optional.of(new RecommendationDto(
                PRODUCT_NAME_TOP_SAVING,
                PRODUCT_ID_TOP_SAVING,
                DESCRIPTION_TOP_SAVING
        ));
    }
}