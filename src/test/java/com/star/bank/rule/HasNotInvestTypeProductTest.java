package com.star.bank.rule;

import com.star.bank.model.rule.HasNotInvestTypeProduct;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HasNotInvestTypeProductTest {
    @Test
    void getSubQueryTest() {
        HasNotInvestTypeProduct hasNotInvestTypeProduct = new HasNotInvestTypeProduct();
        String whatWeDoExpected = "COUNT(CASE WHEN P.TYPE = 'INVEST' THEN 1 END) = 0";

        String actual = hasNotInvestTypeProduct.getSubQuery();

        Assertions.assertEquals(whatWeDoExpected, actual);
        Assertions.assertNotNull(hasNotInvestTypeProduct);

    }
}
