package org.skypro.banking_service.service.rules;

import org.skypro.banking_service.dto.RecommendationDto;
import org.skypro.banking_service.repository.RecommendationRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import static org.skypro.banking_service.service.constants.ProductConstants.*;


@Component
public class TopSavingRule implements RecommendationRule {

    private final RecommendationRepository recommendationRepository;

    public TopSavingRule(RecommendationRepository recommendationRepository) {
        this.recommendationRepository = recommendationRepository;
    }

    @Override
    public Optional<RecommendationDto> checkOut(UUID userId) {

        if (isEligibleForTopSaving(userId)) {
            return buildRecommendationDto();
        }
        return Optional.empty();
    }

    private boolean isEligibleForTopSaving(UUID userId) {
        return usesDebitProduct(userId)
               && hasLargeEnoughDeposits(userId)
               && depositsExceedWithdrawals(userId);
    }

    private boolean usesDebitProduct(UUID userId) {
        return recommendationRepository.existsUserProductByType(userId, TYPE_DEBIT);
    }

    private boolean hasLargeEnoughDeposits(UUID userId) {
        long debitDeposits = recommendationRepository.findTotalDepositByUserIdAndProductType(userId, TYPE_DEBIT);
        long savingDeposits = recommendationRepository.findTotalDepositByUserIdAndProductType(userId, TYPE_SAVING);
        return debitDeposits >= LIMIT_TOP_SAVING || savingDeposits >= LIMIT_TOP_SAVING;
    }

    private boolean depositsExceedWithdrawals(UUID userId) {
        long debitDeposits = recommendationRepository.findTotalDepositByUserIdAndProductType(userId, TYPE_DEBIT);
        long debitWithdrawals = recommendationRepository.findTotalWithdrawByUserIdAndProductType(userId, TYPE_DEBIT);
        return debitDeposits > debitWithdrawals;
    }

    private Optional<RecommendationDto> buildRecommendationDto() {
        return Optional.of(new RecommendationDto(
                PRODUCT_NAME_TOP_SAVING,
                PRODUCT_ID_TOP_SAVING,
                DESCRIPTION_TOP_SAVING
        ));
    }
}