package com.star.bank.service;

import org.springframework.stereotype.Service;
// DebitSpendingAbove100000Rule: Проверяет, что сумма трат по всем продуктам типа DEBIT больше 100 000 ₽
@Service
public class DebitSpendingAbove100000Rule implements Rule {
    @Override
    public String getSubQuery() {
        return "SELECT SUM(ABS(t.amount)) FROM transactions t " +
                "JOIN products p ON t.product_id = p.id " +
                "WHERE p.type = 'DEBIT' AND t.amount < 0 AND t.user_id = ? " +
                "HAVING SUM(ABS(t.amount)) > 100000";
    }
}
