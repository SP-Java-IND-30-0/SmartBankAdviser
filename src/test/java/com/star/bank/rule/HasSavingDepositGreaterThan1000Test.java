package com.star.bank.rule;

import com.star.bank.model.rule.HasSavingDepositGreaterThan1000;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HasSavingDepositGreaterThan1000Test {
    @Test
    void getSubQueryTest() {
        HasSavingDepositGreaterThan1000 hasSavingDepositGreaterThan1000 =
                new HasSavingDepositGreaterThan1000();
        String whatWeDoExpected =
                "SUM(CASE WHEN P.TYPE = 'SAVING' AND T.TYPE = 'DEPOSIT' THEN T.AMOUNT ELSE 0 END) > 1000";

        String actual = hasSavingDepositGreaterThan1000.getSubQuery();

        Assertions.assertEquals(whatWeDoExpected, actual);
        Assertions.assertNotNull(hasSavingDepositGreaterThan1000);
    }
}
