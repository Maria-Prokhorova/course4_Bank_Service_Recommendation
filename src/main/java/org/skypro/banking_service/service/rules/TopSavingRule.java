package org.skypro.banking_service.service.rules;

import org.skypro.banking_service.dto.RecommendationDto;
import org.skypro.banking_service.service.RulesService;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import static org.skypro.banking_service.service.constants.ProductConstants.*;

@Component
public class TopSavingRule implements RecommendationRule {

    private final RulesService rulesService;

    public TopSavingRule(RulesService rulesService) {
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
        return rulesService.isUsingProduct(userId, TYPE_DEBIT)
                && rulesService.isAmountSeveralDepositsMoreOrEqualsLimit(userId, TYPE_DEBIT, TYPE_SAVING, LIMIT_TOP_SAVING)
                && rulesService.isAmountDepositsMoreThanWithdrawals(userId, TYPE_DEBIT);
    }

    private Optional<RecommendationDto> buildRecommendationDto() {
        return Optional.of(new RecommendationDto(
                PRODUCT_NAME_TOP_SAVING,
                PRODUCT_ID_TOP_SAVING,
                DESCRIPTION_TOP_SAVING
        ));
    }
}