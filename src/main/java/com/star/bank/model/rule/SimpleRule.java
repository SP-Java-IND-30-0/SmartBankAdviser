package com.star.bank.model.rule;

import com.star.bank.model.enums.QueryType;
import com.star.bank.model.product.DynamicRule;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "simple_rules")
@Data
public class SimpleRule implements Rule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private QueryType queryType;
    private boolean negate;
    @OneToOne(cascade = CascadeType.ALL)
    private RuleArguments arguments;
    @ManyToMany(mappedBy = "rules")
    private Set<DynamicRule> dynamicRules = new HashSet<>();

    @Override
    public String getSubQuery() {
        return negate ? arguments.getSubQuery() : "NOT " + arguments.getSubQuery();
    }
}
