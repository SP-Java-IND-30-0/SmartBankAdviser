package com.star.bank.model.rule;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "rule_arguments")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "query_type", discriminatorType = DiscriminatorType.STRING)
public abstract class RuleArguments implements Rule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    private SimpleRule simpleRule;

    @PrePersist
    public void prePersist() {
        if (simpleRule != null) {
            simpleRule.setArguments(this);

        }
    }

    public abstract List<String> convertToList();
}
