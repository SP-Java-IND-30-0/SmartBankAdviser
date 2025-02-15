package com.star.bank.model.rule;

import com.star.bank.model.enums.BankProductType;
import com.star.bank.model.enums.CompareType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "rule_compare_operation_sum")
@DiscriminatorValue("TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW")
@Data
@NoArgsConstructor
public class RuleCompareOperationSum extends RuleArguments{

    private BankProductType productType;
    private CompareType compareType;

    @Override
    public String getSubQuery() {
        return "";
    }
}
