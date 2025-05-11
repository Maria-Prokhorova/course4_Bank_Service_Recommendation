package org.skypro.banking_service.serviceTest;

import org.junit.jupiter.api.Test;
import org.skypro.banking_service.rule_system.static_rules.parameter.RuleParameters;
import org.skypro.banking_service.rule_system.static_rules.StaticQueryExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.skypro.banking_service.constants.ProductConstants.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class StaticQueryRulesExecutorTest {

    @Autowired
    private StaticQueryExecutor staticQueryExecutor;

    @Test
    void shouldReturnTrueIfUserIsUsingProductDebit() {
        UUID userId = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");
        assertThat(staticQueryExecutor.isUsingProduct(
                RuleParameters.of (
                        userId,
                        TYPE_DEBIT))).isTrue();
    }

    @Test
    void shouldReturnTrueIfAmountDepositMoreLimit() {
        UUID userId = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");

        assertThat(staticQueryExecutor.isAmountDepositMoreLimit(
                RuleParameters.of(
                        userId,
                        TYPE_SAVING,
                        LIMIT_INVEST_500))).isTrue();
    }

    @Test
    void shouldReturnTrueIfAmountSeveralDepositsMoreOrEqualsLimit() {
        UUID userId = UUID.fromString("1f9b149c-6577-448a-bc94-16bea229b71a");

        assertThat(staticQueryExecutor.isAmountSeveralDepositsMoreOrEqualsLimit(
                RuleParameters.of(
                        userId,
                        TYPE_DEBIT,
                        TYPE_SAVING,
                        LIMIT_TOP_SAVING))).isTrue();
     }

    @Test
    void shouldReturnTrueIfAmountWithdrawMoreLimit() {
        UUID userId = UUID.fromString("1f9b149c-6577-448a-bc94-16bea229b71a");

        assertThat(staticQueryExecutor.isAmountWithdrawMoreLimit(
                RuleParameters.of(
                        userId,
                        TYPE_DEBIT,
                        LIMIT_SIMPLE_CREDIT))).isTrue();
    }

    @Test
    void shouldReturnTrueIfAmountDepositsMoreThanWithdrawals() {
        UUID userId = UUID.fromString("1f9b149c-6577-448a-bc94-16bea229b71a");

        assertThat(staticQueryExecutor.isAmountDepositsMoreThanWithdrawals(
                RuleParameters.of(
                        userId,
                        TYPE_DEBIT))).isTrue();
    }
}
