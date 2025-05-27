package org.skypro.banking_service.service.statickRulesSystem.rules;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.skypro.banking_service.dto.RecommendationDTO;
import org.skypro.banking_service.service.ruleSystem.statickRulesSystem.rules.StaticRuleInvest500Imp;
import org.skypro.banking_service.service.ruleSystem.statickRulesSystem.rules.StaticRuleSimpleCreditImp;
import org.skypro.banking_service.service.ruleSystem.statickRulesSystem.rules.StaticRuleTopSavingImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.skypro.banking_service.constants.ConstantsForStaticRules.*;

@SpringBootTest
public class RulesTest {

    @Autowired
    StaticRuleInvest500Imp staticRuleInvest500Imp;

    @Autowired
    StaticRuleTopSavingImp staticRuleTopSavingImp;

    @Autowired
    StaticRuleSimpleCreditImp staticRuleSimpleCreditImp;


    @Test
    void shouldReturnRecommendationInvest500() {
        UUID userId = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");

        Optional<RecommendationDTO> result = staticRuleInvest500Imp.checkOutStaticRule(userId);
        assertEquals(new RecommendationDTO(
                PRODUCT_NAME_INVEST_500,
                PRODUCT_ID_INVEST_500,
                DESCRIPTION_INVEST_500), result.get());
    }

    @Test
    void shouldReturnRecommendationTopSaving() {
        UUID userId = UUID.fromString("d4a4d619-9a0c-4fc5-b0cb-76c49409546b");

        Optional<RecommendationDTO> result = staticRuleTopSavingImp.checkOutStaticRule(userId);
        assertEquals(new RecommendationDTO(
                PRODUCT_NAME_TOP_SAVING,
                PRODUCT_ID_TOP_SAVING,
                DESCRIPTION_TOP_SAVING), result.get());
    }

    @Test
    void shouldReturnRecommendationSimpleCredit() {
        UUID userId = UUID.fromString("1f9b149c-6577-448a-bc94-16bea229b71a");

        Optional<RecommendationDTO> result = staticRuleSimpleCreditImp.checkOutStaticRule(userId);
        assertEquals(new RecommendationDTO(
                PRODUCT_NAME_SIMPLE_CREDIT,
                PRODUCT_ID_SIMPLE_CREDIT,
                DESCRIPTION_SIMPLE_CREDIT), result.get());
    }

    @Test
    void shouldReturnEmptyRecommendation() {
        UUID userId = UUID.fromString("1f9b149c-6577-448a-bc94-16bea229b71a");

        Optional<RecommendationDTO> result = staticRuleInvest500Imp.checkOutStaticRule(userId);
        Assertions.assertTrue(result.isEmpty());
    }
}
