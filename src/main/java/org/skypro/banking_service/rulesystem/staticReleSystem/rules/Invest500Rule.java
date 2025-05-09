package org.skypro.banking_service.rulesystem.staticReleSystem.rules;

import org.skypro.banking_service.dto.RecommendationDto;
import org.skypro.banking_service.rulesystem.staticReleSystem.RecommendationRule;
import org.skypro.banking_service.rulesystem.staticReleSystem.parameter.RuleParameters;
import org.skypro.banking_service.service.RulesService;
import org.skypro.banking_service.service.impl.RulesServiceImpl;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import static org.skypro.banking_service.constants.ProductDescriptions.DESCRIPTION_INVEST_500;
import static org.skypro.banking_service.constants.ProductIdConstants.PRODUCT_ID_INVEST_500;
import static org.skypro.banking_service.constants.ProductLimitConstants.LIMIT_INVEST_500;
import static org.skypro.banking_service.constants.ProductNameConstants.PRODUCT_NAME_INVEST_500;
import static org.skypro.banking_service.constants.ProductTypeConstants.*;


@Component
public class Invest500Rule implements RecommendationRule {

    private final RulesService rulesService;

    public Invest500Rule(RulesServiceImpl rulesService) {
        this.rulesService = rulesService;
    }

    @Override
    public Optional<RecommendationDto> checkOut(UUID userId) {
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