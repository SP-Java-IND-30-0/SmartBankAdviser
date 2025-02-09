package com.star.bank.service;

import org.springframework.stereotype.Service;
import java.util.Set;

@Service
public class Invest500Product implements Product {
    private final String name = "Invest 500";
    private final String id = "147f6a0f-3b91-413b-ab99-87f081d60d5a";
    private final Set<Rule> rules;
    private final String text = "Откройте свой путь к успеху с индивидуальным инвестиционным счетом ...";

    public Invest500Product(Set<Rule> rules) {
        this.rules = rules;
    }

    @Override
    public String getQuery() {
        return "SELECT 1 FROM transactions t " +
                "JOIN products p ON t.product_id = p.id " +
                "WHERE p.type = 'DEBIT' AND t.user_id = ? " +
                "LIMIT 1";
    }
}
