package com.star.bank.service;

import org.springframework.stereotype.Service;
// NoCreditProductRule: Проверяет, что у пользователя нет продуктов с типом CREDIT
@Service
public class NoCreditProductRule implements Rule {
    @Override
    public String getSubQuery() {
        return "SELECT 1 FROM transactions t " +
                "JOIN products p ON t.product_id = p.id " +
                "WHERE p.type = 'CREDIT' AND t.user_id = ? " +
                "LIMIT 1";
    }
}
