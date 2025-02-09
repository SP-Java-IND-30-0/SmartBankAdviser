package com.star.bank.service;

import org.springframework.stereotype.Service;
// DebitIncomeHigherThanSpendingRule: Проверяет, что сумма пополнений по всем продуктам типа DEBIT больше, чем сумма трат по этим же продуктам
@Service
public class DebitIncomeHigherThanSpendingRule implements Rule {
    @Override
    public String getSubQuery() {
        return "SELECT " +
                "(SELECT COALESCE(SUM(t.amount), 0) FROM transactions t " +
                "JOIN products p ON t.product_id = p.id " +
                "WHERE p.type = 'DEBIT' AND t.amount > 0 AND t.user_id = ?) > " +
                "(SELECT COALESCE(SUM(ABS(t.amount)), 0) FROM transactions t " +
                "JOIN products p ON t.product_id = p.id " +
                "WHERE p.type = 'DEBIT' AND t.amount < 0 AND t.user_id = ?)";
    }
}
