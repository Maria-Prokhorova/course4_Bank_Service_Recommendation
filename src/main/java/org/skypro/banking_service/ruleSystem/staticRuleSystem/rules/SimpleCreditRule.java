package org.skypro.banking_service.ruleSystem.staticRuleSystem.rules;

import org.skypro.banking_service.dto.RecommendationDto;
import org.skypro.banking_service.ruleSystem.staticRuleSystem.RecommendationRule;
import org.skypro.banking_service.ruleSystem.staticRuleSystem.parameter.RuleParameters;
import org.skypro.banking_service.service.RulesService;
import org.skypro.banking_service.service.impl.RulesServiceImpl;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import static org.skypro.banking_service.constants.ProductDescriptions.DESCRIPTION_SIMPLE_CREDIT;
import static org.skypro.banking_service.constants.ProductIdConstants.PRODUCT_ID_SIMPLE_CREDIT;
import static org.skypro.banking_service.constants.ProductLimitConstants.LIMIT_SIMPLE_CREDIT;
import static org.skypro.banking_service.constants.ProductNameConstants.PRODUCT_NAME_SIMPLE_CREDIT;
import static org.skypro.banking_service.constants.ProductTypeConstants.TYPE_CREDIT;
import static org.skypro.banking_service.constants.ProductTypeConstants.TYPE_DEBIT;


@Component
public class SimpleCreditRule implements RecommendationRule {

    private final RulesService rulesService;

    public SimpleCreditRule(RulesServiceImpl rulesService) {
        this.rulesService = rulesService;
    }

    @Override
    public Optional<RecommendationDto> checkOut(UUID userId) {
        if (isEligibleForSimpleCreditRule(userId)) {
            return buildRecommendationDto();
        }
        return Optional.empty();
    }

    private boolean isEligibleForSimpleCreditRule(UUID userId) {
        return !rulesService.isUsingProduct(
                RuleParameters.of(userId, TYPE_CREDIT))
               && rulesService.isAmountDepositsMoreThanWithdrawals(
                RuleParameters.of(userId, TYPE_DEBIT))
               && rulesService.isAmountWithdrawMoreLimit(
                RuleParameters.of(userId, TYPE_DEBIT, LIMIT_SIMPLE_CREDIT));
    }

    private Optional<RecommendationDto> buildRecommendationDto() {
        return Optional.of(new RecommendationDto(
                PRODUCT_NAME_SIMPLE_CREDIT,
                PRODUCT_ID_SIMPLE_CREDIT,
                DESCRIPTION_SIMPLE_CREDIT)
        );
    }
}

