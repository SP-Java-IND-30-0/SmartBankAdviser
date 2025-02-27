package com.star.bank.rule;

import com.star.bank.model.rule.HasDebitWithdrawGreater100000;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HasDebitWithdrawGreater100000Test {
    @Test
    void getSubQueryTest() {
        HasDebitWithdrawGreater100000 hasDebitWithdrawGreater100000 = new HasDebitWithdrawGreater100000();

        String whatWeDoExpected =
                "SUM(CASE WHEN P.TYPE = 'DEBIT' AND T.TYPE = 'WITHDRAW' THEN T.AMOUNT ELSE 0 END) > 100000";

        String actual = hasDebitWithdrawGreater100000.getSubQuery();
        Assertions.assertEquals(whatWeDoExpected, actual);
        Assertions.assertNotNull(hasDebitWithdrawGreater100000);

    }

}
