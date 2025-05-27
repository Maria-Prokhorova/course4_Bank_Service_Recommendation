package org.skypro.banking_service.service.statickRulesSystem;

import org.junit.jupiter.api.Test;
import org.skypro.banking_service.service.ruleSystem.statickRulesSystem.parameter.StaticRuleParameters;
import org.skypro.banking_service.service.ruleSystem.statickRulesSystem.StaticQueryExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.skypro.banking_service.constants.ConstantsForStaticRules.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class StaticQueryRulesExecutorTest {

    @Autowired
    private StaticQueryExecutor staticQueryExecutor;

    @Test
    void shouldReturnTrueIfUserIsUsingProductDebit() {
        UUID userId = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");
        assertThat(staticQueryExecutor.isUsingProduct(
                StaticRuleParameters.of (
                        userId,
                        TYPE_DEBIT))).isTrue();
    }

    @Test
    void shouldReturnTrueIfAmountDepositMoreLimit() {
        UUID userId = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");

        assertThat(staticQueryExecutor.isAmountDepositMoreLimit(
                StaticRuleParameters.of(
                        userId,
                        TYPE_SAVING,
                        LIMIT_INVEST_500))).isTrue();
    }

    @Test
    void shouldReturnTrueIfAmountSeveralDepositsMoreOrEqualsLimit() {
        UUID userId = UUID.fromString("1f9b149c-6577-448a-bc94-16bea229b71a");

        assertThat(staticQueryExecutor.isAmountSeveralDepositsMoreOrEqualsLimit(
                StaticRuleParameters.of(
                        userId,
                        TYPE_DEBIT,
                        TYPE_SAVING,
                        LIMIT_TOP_SAVING))).isTrue();
     }

    @Test
    void shouldReturnTrueIfAmountWithdrawMoreLimit() {
        UUID userId = UUID.fromString("1f9b149c-6577-448a-bc94-16bea229b71a");

        assertThat(staticQueryExecutor.isAmountWithdrawMoreLimit(
                StaticRuleParameters.of(
                        userId,
                        TYPE_DEBIT,
                        LIMIT_SIMPLE_CREDIT))).isTrue();
    }

    @Test
    void shouldReturnTrueIfAmountDepositsMoreThanWithdrawals() {
        UUID userId = UUID.fromString("1f9b149c-6577-448a-bc94-16bea229b71a");

        assertThat(staticQueryExecutor.isAmountDepositsMoreThanWithdrawals(
                StaticRuleParameters.of(
                        userId,
                        TYPE_DEBIT))).isTrue();
    }
}
