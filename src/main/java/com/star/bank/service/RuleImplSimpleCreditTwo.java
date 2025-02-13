package com.star.bank.service;

import org.springframework.stereotype.Component;

@Component
public class RuleImplSimpleCreditTwo implements Rule {
    @Override
    public String getSubQuery() {
        return "(SELECT SUM(t.amount) FROM transactions t " +
                "JOIN products p ON t.product_id = p.id " +
                "WHERE p.type = 'DEBIT' AND t.user_id = ? AND t.transaction_type = 'DEPOSIT') > " +
                "(SELECT SUM(t.amount) FROM transactions t " +
                "JOIN products p ON t.product_id = p.id " +
                "WHERE p.type = 'DEBIT' AND t.user_id = ? AND t.transaction_type = 'SPEND')";
    }
}
/*Сумма пополнений по всем продуктам типа DEBIT больше,
чем сумма трат по всем продуктам типа DEBIT
 */