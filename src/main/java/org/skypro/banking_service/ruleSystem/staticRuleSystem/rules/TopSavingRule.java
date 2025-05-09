package org.skypro.banking_service.ruleSystem.staticRuleSystem.rules;

import org.skypro.banking_service.dto.RecommendationDto;
import org.skypro.banking_service.ruleSystem.staticRuleSystem.RecommendationRule;
import org.skypro.banking_service.ruleSystem.staticRuleSystem.parameter.RuleParameters;
import org.skypro.banking_service.service.RulesService;
import org.skypro.banking_service.service.impl.RulesServiceImpl;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import static org.skypro.banking_service.constants.ProductDescriptions.DESCRIPTION_TOP_SAVING;
import static org.skypro.banking_service.constants.ProductIdConstants.PRODUCT_ID_TOP_SAVING;
import static org.skypro.banking_service.constants.ProductLimitConstants.LIMIT_TOP_SAVING;
import static org.skypro.banking_service.constants.ProductNameConstants.PRODUCT_NAME_TOP_SAVING;
import static org.skypro.banking_service.constants.ProductTypeConstants.TYPE_DEBIT;
import static org.skypro.banking_service.constants.ProductTypeConstants.TYPE_SAVING;

@Component
public class TopSavingRule implements RecommendationRule {

    private final RulesService rulesService;

    public TopSavingRule(RulesServiceImpl rulesService) {
        this.rulesService = rulesService;
    }

    @Override
    public Optional<RecommendationDto> checkOut(UUID userId) {
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