package org.skypro.banking_service.rule_system.static_rules.rules;

import org.skypro.banking_service.dto.RecommendationDto;
import org.skypro.banking_service.rule_system.static_rules.parameter.RuleParameters;
import org.skypro.banking_service.rule_system.static_rules.StaticQueryExecutorImpl;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import static org.skypro.banking_service.constants.ProductConstants.*;

@Component
public class StaticRuleInvest500Imp implements StaticRule {

    private final StaticQueryExecutorImpl rulesService;

    public StaticRuleInvest500Imp(StaticQueryExecutorImpl rulesService) {
        this.rulesService = rulesService;
    }

    @Override
    public Optional<RecommendationDto> checkOutStaticRule(UUID userId) {
        if (isEligibleForInvest500(userId)) {
            return buildRecommendationDto();
        }
        return Optional.empty();
    }

    private boolean isEligibleForInvest500(UUID userId) {
        return rulesService.isUsingProduct(
                RuleParameters.of(userId, TYPE_DEBIT))
               && !rulesService.isUsingProduct(
                RuleParameters.of(userId, TYPE_INVEST))
               && rulesService.isAmountDepositMoreLimit(
                RuleParameters.of(userId, TYPE_SAVING, LIMIT_INVEST_500));
    }

    private Optional<RecommendationDto> buildRecommendationDto() {
        return Optional.of(new RecommendationDto(
                PRODUCT_NAME_INVEST_500,
                PRODUCT_ID_INVEST_500,
                DESCRIPTION_INVEST_500
        ));
    }

}