package com.star.bank.model.rule;

import org.springframework.stereotype.Component;

@Component
public class HasSavingDepositGreaterThan1000 implements Rule {
    @Override
    public String getSubQuery() {
        return "SUM(CASE WHEN P.TYPE = 'SAVING' AND T.TYPE = 'DEPOSIT' THEN T.AMOUNT ELSE 0 END) > 1000";
    }
}
//Сумма пополнений продуктов с типом SAVING больше 1000 ₽