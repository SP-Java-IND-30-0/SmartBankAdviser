package com.star.bank.model.rule;

import com.star.bank.model.enums.BankProductType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "rule_user_of")
@DiscriminatorValue("USER_OF")
@Data
@NoArgsConstructor
public class RuleUserOf extends RuleArguments {

    private BankProductType productType;

    @Override
    public String getSubQuery() {
        return "";
    }
}
