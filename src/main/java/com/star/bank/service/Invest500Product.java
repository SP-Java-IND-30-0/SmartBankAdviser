package com.star.bank.service;

import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class Invest500Product implements Product {
    private final String name = "Invest 500";
    private final String id = "147f6a0f-3b91-413b-ab99-87f081d60d5a";
    private final Set<Rule> rules;

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

    private final String text = "Откройте свой путь к успеху с индивидуальным инвестиционным счетом (ИИС) от нашего банка! Воспользуйтесь налоговыми льготами и начните инвестировать с умом. Пополните счет до конца года и получите выгоду в виде вычета на взнос в следующем налоговом периоде. Не упустите возможность разнообразить свой портфель, снизить риски и следить за актуальными рыночными тенденциями. Откройте ИИС сегодня и станьте ближе к финансовой независимости!";
}
