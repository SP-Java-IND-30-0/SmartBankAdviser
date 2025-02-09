package com.star.bank.service;

import org.springframework.stereotype.Service;
// HasDebitProductRule: Проверяет, есть ли у пользователя хотя бы один продукт с типом DEBIT
@Service
public class HasDebitProductRule implements Rule {
    @Override
    public String getSubQuery() {
        return "SELECT 1 FROM transactions t " +
                "JOIN products p ON t.product_id = p.id " +
                "WHERE p.type = 'DEBIT' AND t.user_id = ? " +
                "LIMIT 1";
    }
}
