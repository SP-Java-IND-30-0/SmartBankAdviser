package com.star.bank.service;

import org.springframework.stereotype.Service;
// SavingTopUpAbove50000Rule: Проверяет, что сумма пополнений по всем продуктам типа SAVING больше или равна 50 000 ₽
@Service
public class SavingTopUpAbove50000Rule implements Rule {
    @Override
    public String getSubQuery() {
        return "SELECT SUM(t.amount) FROM transactions t " +
                "JOIN products p ON t.product_id = p.id " +
                "WHERE p.type = 'SAVING' AND t.user_id = ? " +
                "HAVING SUM(t.amount) >= 50000";
    }
}
