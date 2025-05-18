package org.skypro.banking_service.serviceQuery;

import org.junit.jupiter.api.Test;
import org.skypro.banking_service.exception.QueryEvaluationException;
import org.skypro.banking_service.model.Queries;
import org.skypro.banking_service.model.Recommendations;
import org.skypro.banking_service.ruleSystem.dynamicRulesSystem.serviceQuery.RuleQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RuleQueryServiceTest {

    private static final UUID testUserId = UUID.randomUUID();
    private final String productType = "DEBIT";
    private final List<String> listProduct = List.of(productType);

    private final Queries queryTrue = new Queries(
            "active_user_of",
            listProduct,
            true,
            new Recommendations()
    );

    private final Queries queryFalse = new Queries(
            "active_user_of",
            listProduct,
            false,
            new Recommendations()
    );

    private final Queries wrongQuery = new Queries(
            "wrong",
            listProduct,
            true,
            new Recommendations()
    );

    @Autowired
    private RuleQueryService ruleQueryService;

    @Test
    void shouldResultOfRuleQueryThrowQueryEvaluationExceptionIfArgumentsNull() {
        assertThrows(QueryEvaluationException.class, () -> ruleQueryService.ruleQuery(new Queries(), null));
    }

    @Test
    void shouldResultOfRuleQueryThrowQueryEvaluationExceptionIfQueryWrong() {
        assertThrows(QueryEvaluationException.class, () -> ruleQueryService.ruleQuery(wrongQuery, testUserId));
    }

    @Test
    void shouldResultOfRuleQueryIsTrue() {
        boolean ruleQuery = ruleQueryService.ruleQuery(queryTrue, testUserId);
        assertTrue(ruleQuery);
    }

    @Test
    void shouldResultOfRuleQueryIsFalse() {
        boolean ruleQuery = ruleQueryService.ruleQuery(queryFalse, testUserId);
        assertFalse(ruleQuery);
    }
}
