package com.star.bank.model.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("id")
    private UUID productId;
    @JsonProperty("name")
    private String productName;
    @JsonProperty("text")
    private String productText;
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "dynamic_rules_simple_rules",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "rule_id")
    )
    @JsonIgnore
    private Set<SimpleRule> rules;

    @Override
    @JsonIgnore
    public String getQuery() {
        StringJoiner joiner = new StringJoiner(" AND ", Literals.QUERY_PREFIX, Literals.QUERY_SUFFIX);
        rules.forEach(rule -> joiner.add(rule.getSubQuery()));
        return joiner.toString();
    }

    @Override
    public String getName() {
        return productName;
    }

    @Override
    public String getId() {
        return productId.toString();
    }

    @Override
    public String getText() {
        return productText;
    }
}
