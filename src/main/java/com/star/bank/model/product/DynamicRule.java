package com.star.bank.model.product;

import com.star.bank.model.rule.SimpleRule;
import jakarta.persistence.*;

import java.util.Set;
import java.util.UUID;

@Entity
public class DynamicRule implements Product {

    @Id
    private UUID productId;
    private String productName;
    private String productText;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "dynamic_rules_simple_rules",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "rule_id")
    )
    private Set<SimpleRule> rules;

    @Override
    public String getQuery() {
        return "";
    }
}
