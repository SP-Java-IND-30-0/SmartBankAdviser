package com.star.bank.rule;

import com.star.bank.model.rule.HasDebitTypeProduct;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HasDebitTypeProductTest {

    @Test
    void getSubQueryTest() {
        HasDebitTypeProduct hasDebitTypeProduct = new HasDebitTypeProduct();

        String whatWeDoExpected = "COUNT(CASE WHEN P.TYPE = 'DEBIT' THEN 1 END) > 0";

        String actual = hasDebitTypeProduct.getSubQuery();

        Assertions.assertEquals(whatWeDoExpected, actual);
        Assertions.assertNotNull(hasDebitTypeProduct);

    }
}
