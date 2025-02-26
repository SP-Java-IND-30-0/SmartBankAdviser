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
    @JsonIgnore
    private UUID productId;
    @JsonIgnore
    private String productName;
    @JsonIgnore
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
        if (rules != null && !rules.isEmpty()) {
            rules.forEach(rule -> joiner.add(rule.getSubQuery()));
        } else {
            joiner.add("1=1");
        }
        return joiner.toString();
    }

    @Override
    @JsonProperty("name")
    public String getName() {
        return productName;
    }

    @Override
    @JsonProperty("id")
    public String getId() {
        return productId.toString();
    }

    @Override
    @JsonProperty("text")
    public String getText() {
        return productText;
    }
}
