package com.star.bank.model.rule;

import com.star.bank.model.enums.QueryType;
import jakarta.persistence.*;
import lombok.Data;

/**
 * Простое правило, реализующее интерфейс Rule.
 * Содержит тип запроса и аргументы правила.
 */
@Entity
@Table(name = "simple_rules")
@Data
public class SimpleRule implements Rule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /**
     * Тип запроса, определяющий поведение правила.
     */
    @Enumerated(EnumType.ORDINAL)
    private QueryType queryType;
    /**
     * Флаг, указывающий на отрицание условия правила.
     */
    private boolean negate;
    /**
     * Аргументы правила.
     */
    @ManyToOne (cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "arguments_id")
    private RuleArguments arguments;


    @Override
    public String getSubQuery() {
        return negate ? "NOT " + arguments.getSubQuery() : arguments.getSubQuery();
    }
}
