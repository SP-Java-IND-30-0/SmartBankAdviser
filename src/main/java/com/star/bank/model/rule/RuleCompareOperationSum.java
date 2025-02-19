package com.star.bank.model.rule;

import com.star.bank.exception.InvalidQueryArgumentsException;
import com.star.bank.model.enums.BankProductType;
import com.star.bank.model.enums.CompareType;
import com.star.bank.model.enums.QueryType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "rule_compare_operation_sum",
        uniqueConstraints = @UniqueConstraint(columnNames = {"product_type", "compare_type"}))
@DiscriminatorValue("TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RuleCompareOperationSum extends RuleArguments {

    private BankProductType productType;
    private CompareType compareType;

    public RuleCompareOperationSum(List<String> arguments) {
        super();
        if (arguments == null || arguments.size() != 2) {
            throw new InvalidQueryArgumentsException(QueryType.TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW, arguments != null ? arguments : List.of());
        }
        try {
            this.productType = BankProductType.valueOf(arguments.get(0));
            this.compareType = CompareType.findByValue(arguments.get(1));
        } catch (IllegalArgumentException e) {
            throw new InvalidQueryArgumentsException(QueryType.TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW, arguments);
        }
    }

    @Override
    public String getSubQuery() {

        return String.format("SUM(CASE WHEN P.TYPE = '%s' AND T.TYPE = 'DEPOSIT' THEN T.AMOUNT ELSE 0 END)" +
                        " %s SUM(CASE WHEN P.TYPE = '%s' AND T.TYPE = 'WITHDRAW' THEN T.AMOUNT ELSE 0 END)",
                productType.name(), compareType.getValue(), productType.name());
    }

    @Override
    public List<String> convertToList() {
        return List.of(
                productType.name(),
                compareType.getValue()
        );
    }
}
