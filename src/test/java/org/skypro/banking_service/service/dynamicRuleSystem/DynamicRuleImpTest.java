package org.skypro.banking_service.service.dynamicRuleSystem;

import org.junit.jupiter.api.Test;
import org.skypro.banking_service.exception.QueryEvaluationException;
import org.skypro.banking_service.model.QueryRules;
import org.skypro.banking_service.model.Recommendation;
import org.skypro.banking_service.service.ruleSystem.dynamicRulesSystem.impl.DynamicRuleImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DynamicRuleImpTest {

    private static final UUID testUserId = UUID.randomUUID();
    private final String productType = "DEBIT";
    private final List<String> listProduct = List.of(productType);

    private final QueryRules queryRulesTrue = new QueryRules(
            "active_user_of",
            listProduct,
            true,
            new Recommendation()
    );

    private final QueryRules queryRulesFalse = new QueryRules(
            "active_user_of",
            listProduct,
            false,
            new Recommendation()
    );

    private final QueryRules wrongQueryRules = new QueryRules(
            "wrong",
            listProduct,
            true,
            new Recommendation()
    );

    @Autowired
    private DynamicRuleImp dynamicRuleImp;

    @Test
    void shouldResultOfCheckOutDynamicRuleEvaluationExceptionIfArgumentsNull() {
        assertThrows(QueryEvaluationException.class, () -> dynamicRuleImp.checkOutDinamicRule
                (new QueryRules(), null));
    }

    @Test
    void shouldResultOfCheckOutDynamicRuleWrong() {
        assertThrows(QueryEvaluationException.class, () -> dynamicRuleImp.checkOutDinamicRule
                (wrongQueryRules, testUserId));
    }

    @Test
    void shouldResultOfCheckOutDynamicRuleIsTrue() {
        boolean ruleQuery = dynamicRuleImp.checkOutDinamicRule(queryRulesTrue, testUserId);
        assertTrue(ruleQuery);
    }

    @Test
    void shouldResultOfCheckOutDynamicRuleIsFalse() {
        boolean ruleQuery = dynamicRuleImp.checkOutDinamicRule(queryRulesFalse, testUserId);
        assertFalse(ruleQuery);
    }
}
