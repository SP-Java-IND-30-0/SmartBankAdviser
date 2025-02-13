package com.star.bank.model.rule;

import org.springframework.stereotype.Component;

@Component
public class HasDebitDepositGreaterThanWithdraw implements Rule {
    @Override
    public String getSubQuery() {
        return """
                SUM(CASE WHEN P.TYPE = 'DEBIT' AND T.TYPE = 'DEPOSIT' THEN T.AMOUNT ELSE 0 END)
                >
                SUM(CASE WHEN P.TYPE = 'DEBIT' AND T.TYPE = 'WITHDRAW' THEN T.AMOUNT ELSE 0 END)
                """;

    }
}

/*Сумма пополнений по всем продуктам типа DEBIT больше,
чем сумма трат по всем продуктам типа DEBIT
 */