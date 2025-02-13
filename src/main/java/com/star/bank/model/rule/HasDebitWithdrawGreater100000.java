package com.star.bank.model.rule;

import org.springframework.stereotype.Component;

@Component
public class HasDebitWithdrawGreater100000 implements Rule {
    @Override
    public String getSubQuery() {
        return "SUM(CASE WHEN P.TYPE = 'DEBIT' AND T.TYPE = 'WITHDRAW' THEN T.AMOUNT ELSE 0 END) > 100000";
    }
}
//Сумма трат по всем продуктам типа DEBIT больше, чем 100 000