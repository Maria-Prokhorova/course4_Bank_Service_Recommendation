package org.skypro.banking_service.serviceTest;

import org.junit.jupiter.api.Test;
import org.skypro.banking_service.rulesystem.parameter.RuleParameters;
import org.skypro.banking_service.service.RulesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.skypro.banking_service.constants.ProductConstants.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class RulesServiceTest {

    @Autowired
    private RulesService rulesService;


    @Test
    void shouldReturnTrueIfUserIsUsingProductDebit() {
        UUID userId = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");
        assertThat(rulesService.isUsingProduct(
                new RuleParameters(
                        userId,
                        TYPE_DEBIT,
                        null,
                        0))).isTrue();
    }

    @Test
    void shouldReturnTrueIfAmountDepositMoreLimit() {
        UUID userId = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");

        assertThat(rulesService.isAmountDepositMoreLimit(
                new RuleParameters(
                        userId,
                        TYPE_SAVING,
                        null,
                        LIMIT_INVEST_500))).isTrue();
    }

    @Test
    void shouldReturnTrueIfAmountSeveralDepositsMoreOrEqualsLimit() {
        UUID userId = UUID.fromString("1f9b149c-6577-448a-bc94-16bea229b71a");

        assertThat(rulesService.isAmountSeveralDepositsMoreOrEqualsLimit(
                new RuleParameters(
                        userId,
                        TYPE_DEBIT,
                        TYPE_SAVING,
                        0))).isTrue();
     }

    @Test
    void shouldReturnTrueIfAmountWithdrawMoreLimit() {
        UUID userId = UUID.fromString("1f9b149c-6577-448a-bc94-16bea229b71a");

        assertThat(rulesService.isAmountWithdrawMoreLimit(
                new RuleParameters(
                        userId,
                        TYPE_DEBIT,
                        null,
                        LIMIT_SIMPLE_CREDIT))).isTrue();
    }

    @Test
    void shouldReturnTrueIfAmountDepositsMoreThanWithdrawals() {
        UUID userId = UUID.fromString("1f9b149c-6577-448a-bc94-16bea229b71a");

        assertThat(rulesService.isAmountDepositsMoreThanWithdrawals(
                new RuleParameters(
                        userId,
                        TYPE_DEBIT,
                        null,
                        0))).isTrue();
    }
}
