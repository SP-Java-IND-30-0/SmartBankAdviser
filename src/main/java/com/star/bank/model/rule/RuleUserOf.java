package com.star.bank.model.rule;

import com.star.bank.exception.InvalidQueryArgumentsException;
import com.star.bank.model.enums.BankProductType;
import com.star.bank.model.enums.QueryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
/**
 * Правило для проверки принадлежности пользователя к определённому типу продукта.
 * Расширяет класс RuleArguments.
 */
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "rule_user_of",
        uniqueConstraints = @UniqueConstraint(columnNames = {"product_type"}))
@DiscriminatorValue("USER_OF")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RuleUserOf extends RuleArguments {
    /**
     * Тип продукта, который должен использовать пользователь.
     */
    @Enumerated(EnumType.ORDINAL)
    private BankProductType productType;

    /**
     * Конструктор для создания правила из списка аргументов.
     *
     * @param arguments Список аргументов.
     */
    public RuleUserOf(List<String> arguments) {
        super();
        if (arguments == null || arguments.size() != 1) {
            throw new InvalidQueryArgumentsException(QueryType.USER_OF, arguments != null ? arguments : List.of());
        }
        try {
            this.productType = BankProductType.valueOf(arguments.get(0));
        } catch (IllegalArgumentException e) {
            throw new InvalidQueryArgumentsException(QueryType.USER_OF, arguments);
        }

    }

    @Override
    public String getSubQuery() {
        return String.format("COUNT(CASE WHEN P.TYPE = '%s' THEN 1 END) > 0", productType.name());
    }

    @Override
    public List<String> convertToList() {
        return List.of(productType.name());
    }
}
