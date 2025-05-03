package org.skypro.banking_service.service.rules;

import org.skypro.banking_service.dto.RecommendationDto;
import org.skypro.banking_service.service.RulesService;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;
import static org.skypro.banking_service.constants.ProductConstants.*;

@Component
public class Invest500Rule implements RecommendationRule {

    private final RulesService rulesService;

    public Invest500Rule(RulesService rulesService) {
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
        return rulesService.isUsingProduct(userId, TYPE_DEBIT)
                && !rulesService.isUsingProduct(userId, TYPE_INVEST)
                && rulesService.isAmountDepositMoreLimit(userId, TYPE_SAVING, LIMIT_INVEST_500);
    }

    private Optional<RecommendationDto> buildRecommendationDto() {
        return Optional.of(new RecommendationDto(
                PRODUCT_NAME_INVEST_500,
                PRODUCT_ID_INVEST_500,
                DESCRIPTION_INVEST_500
        ));
    }

}