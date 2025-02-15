package com.star.bank.model.rule;

import com.star.bank.model.enums.BankProductType;
import com.star.bank.model.enums.CompareType;
import com.star.bank.model.enums.OperationType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "rule_compare_sum")
@DiscriminatorValue("TRANSACTION_SUM_COMPARE")
@Data
@NoArgsConstructor
public class RuleCompareSum extends RuleArguments{

    private BankProductType productType;
    private OperationType operationType;
    private CompareType compareType;
    private int amount;

    @Override
    public String getSubQuery() {
        return "";
    }
}
