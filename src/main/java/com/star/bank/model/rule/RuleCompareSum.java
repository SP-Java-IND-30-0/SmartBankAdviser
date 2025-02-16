package com.star.bank.model.rule;

import com.star.bank.exception.InvalidQueryArgumentsException;
import com.star.bank.model.enums.BankProductType;
import com.star.bank.model.enums.CompareType;
import com.star.bank.model.enums.OperationType;
import com.star.bank.model.enums.QueryType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "rule_compare_sum")
@DiscriminatorValue("TRANSACTION_SUM_COMPARE")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RuleCompareSum extends RuleArguments {

    private BankProductType productType;
    private OperationType operationType;
    private CompareType compareType;
    private int amount;

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

        return  String.format("SUM(CASE WHEN P.TYPE = '%s' AND T.TYPE = '%s' THEN T.AMOUNT ELSE 0 END) %s %d",
                productType.name(),operationType.name(),compareType.name(),amount);
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
