package com.star.bank.service;

import org.springframework.stereotype.Service;
import java.util.Set;

@Service
public class TopSavingProduct implements Product {
    private final String name = "Top Saving";
    private final String id = "59efc529-2fff-41af-baff-90ccd7402925";
    private final Set<Rule> rules;
    private final String text = "Откройте свою собственную «Копилку» с нашим банком! ...";

    public TopSavingProduct(Set<Rule> rules) {
        this.rules = rules;
    }




    @Override
    public String getQuery() {
        return "SELECT SUM(amount) FROM transactions t " +
                "JOIN products p ON t.product_id = p.id " +
                "WHERE p.type = 'DEBIT' AND t.user_id = ? " +
                "HAVING SUM(amount) >= 50000";
    }
}
