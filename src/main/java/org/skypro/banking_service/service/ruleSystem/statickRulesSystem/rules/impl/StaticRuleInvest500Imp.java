package org.skypro.banking_service.service.ruleSystem.statickRulesSystem.rules.impl;

import org.skypro.banking_service.dto.RecommendationDTO;
import org.skypro.banking_service.service.ruleSystem.statickRulesSystem.parameter.StaticRuleParameters;
import org.skypro.banking_service.service.ruleSystem.statickRulesSystem.impl.StaticQueryExecutorImpl;
import org.skypro.banking_service.service.ruleSystem.statickRulesSystem.rules.StaticRule;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import static org.skypro.banking_service.constants.ConstantsForStaticRules.*;

@Component
public class StaticRuleInvest500Imp implements StaticRule {

    private final StaticQueryExecutorImpl staticQueryExecutor;

    public StaticRuleInvest500Imp(StaticQueryExecutorImpl staticQueryExecutor) {
        this.staticQueryExecutor = staticQueryExecutor;
    }

    /**
     * Метод проверяет подходит ли банковский продукт "Invest 500" заданному клиенту.
     *
     * @param userId - ID клиента.
     * @return если проверка прошла успешно, продукт будет включен в список рекомендаций клиента.
     */
    @Override
    public Optional<RecommendationDTO> checkOutStaticRule(UUID userId) {
        if (isEligibleForInvest500(userId)) {
            return buildRecommendationDto();
        }
        return Optional.empty();
    }

    /**
     * Внутренний метод, который проверяет, чтобы весь набор правил по продукт "Invest 500" соблюдался.
     *
     * @param userId - ID клиента.
     * @return булевое значение: если правила соблюдаются - true, иначе - false.
     */
    private boolean isEligibleForInvest500(UUID userId) {
        return staticQueryExecutor.isUsingProduct(
                StaticRuleParameters.of(userId, TYPE_DEBIT))
                && !staticQueryExecutor.isUsingProduct(
                StaticRuleParameters.of(userId, TYPE_INVEST))
                && staticQueryExecutor.isAmountDepositMoreLimit(
                StaticRuleParameters.of(userId, TYPE_SAVING, LIMIT_INVEST_500));
    }

    /**
     * Внутренний метод, который собирает рекомендацию по банковскому продукту.
     *
     * @return RecommendationDTO.
     */
    private Optional<RecommendationDTO> buildRecommendationDto() {
        return Optional.of(new RecommendationDTO(
                PRODUCT_NAME_INVEST_500,
                PRODUCT_ID_INVEST_500,
                DESCRIPTION_INVEST_500
        ));
    }
}