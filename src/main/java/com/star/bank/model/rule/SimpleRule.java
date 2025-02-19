package com.star.bank.model.rule;

import com.star.bank.model.enums.QueryType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "simple_rules")
@Data
public class SimpleRule implements Rule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Enumerated(EnumType.ORDINAL)
    private QueryType queryType;
    private boolean negate;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "arguments_id")
    private RuleArguments arguments;


    @Override
    public String getSubQuery() {
        return negate ? "NOT " + arguments.getSubQuery() : arguments.getSubQuery();
    }
}
