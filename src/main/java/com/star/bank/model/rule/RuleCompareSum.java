package com.star.bank.model.rule;

import com.star.bank.exception.InvalidQueryArgumentsException;
import com.star.bank.model.enums.BankProductType;
import com.star.bank.model.enums.CompareType;
import com.star.bank.model.enums.OperationType;
import com.star.bank.model.enums.QueryType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
/**
 * Правило для сравнения суммы транзакций по определённому типу продукта и операции.
 * Расширяет класс RuleArguments.
 */
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "rule_compare_sum",
        uniqueConstraints = @UniqueConstraint(columnNames = {"product_type", "operation_type", "compare_type", "amount"}))
@DiscriminatorValue("TRANSACTION_SUM_COMPARE")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RuleCompareSum extends RuleArguments {

    /**
     * Тип продукта для сравнения.
     */
    @Enumerated(EnumType.ORDINAL)
    private BankProductType productType;
    /**
     * Тип операции для сравнения.
     */
    @Enumerated(EnumType.ORDINAL)
    private OperationType operationType;
    /**
     * Тип сравнения для суммы.
     */
    @Enumerated(EnumType.ORDINAL)
    private CompareType compareType;
    /**
     * Сумма для сравнения.
     */
    private int amount;

    /**
     * Конструктор для создания правила из списка аргументов.
     *
     * @param arguments Список аргументов.
     */
    public RuleCompareSum(List<String> arguments) {
        super();
        if (arguments == null || arguments.size() != 4) {
            throw new InvalidQueryArgumentsException(QueryType.TRANSACTION_SUM_COMPARE, arguments != null ? arguments : List.of());
        }
        try {
            this.productType = BankProductType.valueOf(arguments.get(0));
            this.operationType = OperationType.valueOf(arguments.get(1));
            this.compareType = CompareType.findByValue(arguments.get(2));
            this.amount = Integer.parseInt(arguments.get(3));
        } catch (IllegalArgumentException e) {
            throw new InvalidQueryArgumentsException(QueryType.TRANSACTION_SUM_COMPARE, arguments);
        }
    }


    @Override
    public String getSubQuery() {

        return String.format("SUM(CASE WHEN P.TYPE = '%s' AND T.TYPE = '%s' THEN T.AMOUNT ELSE 0 END) %s %d",
                productType.name(), operationType.name(), compareType.getValue(), amount);
    }

    @Override
    public List<String> convertToList() {
        return List.of(
                productType.name(),
                operationType.name(),
                compareType.getValue(),
                Integer.toString(amount)
        );
    }
}
