package com.star.bank.model.product;

import com.star.bank.model.rule.SimpleRule;
import com.star.bank.utils.Literals;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;
import java.util.StringJoiner;
import java.util.UUID;

@Entity
@Data
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
        StringJoiner joiner = new StringJoiner(" AND ", Literals.QUERY_PREFIX, Literals.QUERY_SUFFIX);
        rules.forEach(rule -> joiner.add(rule.getSubQuery()));
        return joiner.toString();
    }
}
