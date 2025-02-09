package com.star.bank.service;

import org.springframework.stereotype.Service;
// NoInvestProductRule: Проверяет, что у пользователя нет продуктов с типом INVEST
@Service
public class NoInvestProductRule implements Rule {
    @Override
    public String getSubQuery() {
        return "SELECT 1 FROM transactions t " +
                "JOIN products p ON t.product_id = p.id " +
                "WHERE p.type = 'INVEST' AND t.user_id = ? " +
                "LIMIT 1";
    }
}
