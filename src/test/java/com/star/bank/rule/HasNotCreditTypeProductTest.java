package com.star.bank.rule;

import com.star.bank.model.rule.HasDebitTypeProduct;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HasNotCreditTypeProductTest {
    @Test
    void getSubQueryTest() {
        HasDebitTypeProduct hasDebitTypeProduct = new HasDebitTypeProduct();

        String whatWeDoExpected = "COUNT(CASE WHEN P.TYPE = 'CREDIT' THEN 1 END) = 0";

        String actual = hasDebitTypeProduct.getSubQuery();
        Assertions.assertNotNull(hasDebitTypeProduct);
        Assertions.assertNotEquals(whatWeDoExpected,actual);

    }
}
