package com.star.bank.service;

import org.springframework.stereotype.Component;

@Component
public class RuleImplInvest500Two implements Rule {
    @Override
    public String getSubQuery() {
        return "NOT EXISTS (SELECT 1 FROM transactions t " +
                "JOIN products p ON t.product_id = p.id " +
                "WHERE p.type = 'INVEST' AND t.user_id = ?)";
    }
}
//Пользователь не использует продукты с типом INVEST