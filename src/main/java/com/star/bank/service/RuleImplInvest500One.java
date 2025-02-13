package com.star.bank.service;

import org.springframework.stereotype.Component;

@Component
public class RuleImplInvest500One implements Rule {
    @Override
    public String getSubQuery() {
        return "EXISTS (SELECT 1 FROM transactions t " +
                "JOIN products p ON t.product_id = p.id " +
                "WHERE p.type = 'DEBIT' AND t.user_id = ?)";
    }
}
//Пользователь использует как минимум один продукт с типом DEBIT