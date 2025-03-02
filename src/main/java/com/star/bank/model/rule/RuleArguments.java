package com.star.bank.model.rule;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "rule_arguments")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "query_type", discriminatorType = DiscriminatorType.STRING, length = 50)
@Setter
@Getter
public abstract class RuleArguments implements Rule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public abstract List<String> convertToList();
}
