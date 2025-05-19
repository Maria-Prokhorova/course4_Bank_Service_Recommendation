package org.skypro.banking_service.service.ruleSystem.statickRulesSystem.rules;

import org.skypro.banking_service.dto.RecommendationDTO;
import org.skypro.banking_service.service.ruleSystem.statickRulesSystem.parameter.StaticRuleParameters;
import org.skypro.banking_service.service.ruleSystem.statickRulesSystem.StaticQueryExecutorImpl;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import static org.skypro.banking_service.constants.ConstantsForStaticRules.*;

@Component
public class StaticRuleTopSavingImp implements StaticRule {

    private final StaticQueryExecutorImpl staticQueryExecutor;

    public StaticRuleTopSavingImp(StaticQueryExecutorImpl staticQueryExecutor) {
        this.staticQueryExecutor = staticQueryExecutor;
    }

    /**
     * Метод проверяет подходит ли банковский продукт "Top Saving" заданному клиенту.
     *
     * @param userId - ID клиента.
     * @return если проверка прошла успешно, продукт будет включен в список рекомендаций клиента.
     */
    @Override
    public Optional<RecommendationDTO> checkOutStaticRule(UUID userId) {
        if (isEligibleForTopSaving(userId)) {
            return buildRecommendationDto();
        }
        return Optional.empty();
    }

    /**
     * Внутренний метод, который проверяет, чтобы весь набор правил по продукт "Top Saving" соблюдался.
     *
     * @param userId - ID клиента.
     * @return булевое значение: если правила соблюдаются - true, иначе - false.
     */
    private boolean isEligibleForTopSaving(UUID userId) {
        return staticQueryExecutor.isUsingProduct(
                StaticRuleParameters.of(userId, TYPE_DEBIT))
                && staticQueryExecutor.isAmountDepositsMoreThanWithdrawals(
                StaticRuleParameters.of(userId, TYPE_DEBIT))
                && staticQueryExecutor.isAmountSeveralDepositsMoreOrEqualsLimit(
                StaticRuleParameters.of(userId, TYPE_DEBIT, TYPE_SAVING, LIMIT_TOP_SAVING));
    }

    /**
     * Внутренний метод, который собирает рекомендацию по банковскому продукту.
     *
     * @return RecommendationDTO.
     */
    private Optional<RecommendationDTO> buildRecommendationDto() {
        return Optional.of(new RecommendationDTO(
                PRODUCT_NAME_TOP_SAVING,
                PRODUCT_ID_TOP_SAVING,
                DESCRIPTION_TOP_SAVING
        ));
    }
}