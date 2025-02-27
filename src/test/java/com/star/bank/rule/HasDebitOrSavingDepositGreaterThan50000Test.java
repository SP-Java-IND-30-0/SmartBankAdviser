package com.star.bank.rule;

import com.star.bank.exception.InvalidQueryArgumentsException;
import com.star.bank.model.rule.HasDebitOrSavingDepositGreaterThan50000;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HasDebitOrSavingDepositGreaterThan50000Test {

    @Test
    void getSubQueryTest() throws InvalidQueryArgumentsException {
        HasDebitOrSavingDepositGreaterThan50000 hasDebitOrSavingDepositGreaterThan50000 =
                new HasDebitOrSavingDepositGreaterThan50000();
        String whatDoWeExpect = """
                SUM(CASE WHEN P.TYPE = 'SAVING' AND T.TYPE = 'DEPOSIT' THEN T.AMOUNT ELSE 0 END) > 50000
                OR
                SUM(CASE WHEN P.TYPE = 'DEBIT' AND T.TYPE = 'DEPOSIT' THEN T.AMOUNT ELSE 0 END) > 50000
                """;
        String actual = hasDebitOrSavingDepositGreaterThan50000.getSubQuery();
        Assertions.assertEquals(whatDoWeExpect, actual);
        Assertions.assertNotNull(hasDebitOrSavingDepositGreaterThan50000);
    }
}
