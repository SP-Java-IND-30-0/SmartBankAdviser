package com.star.bank.service;

import org.springframework.stereotype.Service;
// SavingTopUpAbove1000Rule: Проверяет, что сумма пополнений по всем продуктам типа SAVING больше 1000 ₽
@Service
public class SavingTopUpAbove1000Rule implements Rule {
    @Override
    public String getSubQuery() {
        return "SELECT SUM(t.amount) FROM transactions t " +
                "JOIN products p ON t.product_id = p.id " +
                "WHERE p.type = 'SAVING' AND t.user_id = ? " +
                "HAVING SUM(t.amount) > 1000";
    }
}
