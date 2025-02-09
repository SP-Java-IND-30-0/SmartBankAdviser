package com.star.bank.service;

import org.springframework.stereotype.Service;
import java.util.Set;

@Service
public class SimpleCreditProduct implements Product {
    private final String name = "Простой кредит";
    private final String id = "ab138afb-f3ba-4a93-b74f-0fcee86d447f";
    private final Set<Rule> rules;
    private final String text = "Откройте мир выгодных кредитов с нами! ...";

    public SimpleCreditProduct(Set<Rule> rules) {
        this.rules = rules;
    }

    @Override
    public String getQuery() {
        return "SELECT 1 FROM transactions t " +
                "JOIN products p ON t.product_id = p.id " +
                "WHERE p.type = 'CREDIT' AND t.user_id = ? " +
                "LIMIT 1";
    }
}
