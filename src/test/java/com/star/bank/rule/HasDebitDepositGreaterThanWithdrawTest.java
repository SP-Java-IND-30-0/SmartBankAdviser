package com.star.bank.rule;

import com.star.bank.model.rule.HasDebitDepositGreaterThanWithdraw;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HasDebitDepositGreaterThanWithdrawTest {

    @Test
    void getSubQueryTest() {
        HasDebitDepositGreaterThanWithdraw hasDebitDepositGreaterThanWithdraw =
                new HasDebitDepositGreaterThanWithdraw();

        String whatWeExpected = """
                SUM(CASE WHEN P.TYPE = 'DEBIT' AND T.TYPE = 'DEPOSIT' THEN T.AMOUNT ELSE 0 END)
                >
                SUM(CASE WHEN P.TYPE = 'DEBIT' AND T.TYPE = 'WITHDRAW' THEN T.AMOUNT ELSE 0 END)
                """;

        String actual = hasDebitDepositGreaterThanWithdraw.getSubQuery();
        Assertions.assertEquals(whatWeExpected, actual);
        Assertions.assertNotNull(hasDebitDepositGreaterThanWithdraw);

    }
}
