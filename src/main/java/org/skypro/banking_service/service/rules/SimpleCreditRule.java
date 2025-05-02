package org.skypro.banking_service.service.rules;

import org.skypro.banking_service.dto.RecommendationDto;
import org.skypro.banking_service.service.RulesService;
import org.springframework.stereotype.Component;

import java.util.*;

import static org.skypro.banking_service.service.constants.ProductConstants.*;

@Component
public class SimpleCreditRule implements RecommendationRule {

    private final RulesService rulesService;

    public SimpleCreditRule(RulesService rulesService) {
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
        return !rulesService.isUsingProduct(userId, TYPE_CREDIT)
                && rulesService.isAmountDepositsMoreThanWithdrawals(userId, TYPE_DEBIT)
                && rulesService.isAmountWithdrawMoreLimit(userId, TYPE_DEBIT, LIMIT_SIMPLE_CREDIT);
    }

    private Optional<RecommendationDto> buildRecommendationDto() {
        return Optional.of(new RecommendationDto(
                PRODUCT_NAME_SIMPLE_CREDIT,
                PRODUCT_ID_SIMPLE_CREDIT,
                DESCRIPTION_SIMPLE_CREDIT)
        );
    }
}

