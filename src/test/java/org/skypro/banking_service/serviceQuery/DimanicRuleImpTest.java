package org.skypro.banking_service.serviceQuery;

import org.junit.jupiter.api.Test;
import org.skypro.banking_service.exception.QueryEvaluationException;
import org.skypro.banking_service.model.QueryRules;
import org.skypro.banking_service.model.Recommendation;
import org.skypro.banking_service.service.ruleSystem.dynamicRulesSystem.DimanicRuleImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DimanicRuleImpTest {

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
    private DimanicRuleImp dimanicRuleImp;

    @Test
    void shouldResultOfCheckOutDinamicRuleEvaluationExceptionIfArgumentsNull() {
        assertThrows(QueryEvaluationException.class, () -> dimanicRuleImp.checkOutDinamicRule(new QueryRules(), null));
    }

    @Test
    void shouldResultOfCheckOutDinamicRuleWrong() {
        assertThrows(QueryEvaluationException.class, () -> dimanicRuleImp.checkOutDinamicRule(wrongQueryRules, testUserId));
    }

    @Test
    void shouldResultOfCheckOutDinamicRuleIsTrue() {
        boolean ruleQuery = dimanicRuleImp.checkOutDinamicRule(queryRulesTrue, testUserId);
        assertTrue(ruleQuery);
    }

    @Test
    void shouldResultOfCheckOutDinamicRuleIsFalse() {
        boolean ruleQuery = dimanicRuleImp.checkOutDinamicRule(queryRulesFalse, testUserId);
        assertFalse(ruleQuery);
    }
}
