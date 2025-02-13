package com.star.bank.service;

import org.springframework.stereotype.Component;

@Component
public class RuleImplTopSavingThree implements Rule {
    @Override
    public String getSubQuery() {
        return "(\n" +
                "    SELECT SUM(t.amount) \n" +
                "    FROM transactions t \n" +
                "    JOIN products p ON t.product_id = p.id \n" +
                "    WHERE t.user_id = ? AND t.transaction_type = 'DEPOSIT' \n" +
                "    AND p.type = 'DEBIT'\n" +
                ") > (\n" +
                "    SELECT SUM(t.amount) \n" +
                "    FROM transactions t \n" +
                "    JOIN products p ON t.product_id = p.id \n" +
                "    WHERE t.user_id = ? AND t.transaction_type = 'WITHDRAWAL' \n" +
                "    AND p.type = 'DEBIT'\n" +
                ")";
    }
}
/*Сумма пополнений по всем продуктам типа
 DEBIT больше, чем сумма трат по всем продуктам типа DEBIT
 */