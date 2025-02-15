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
@Table(name = "rule_active_user_of")
@DiscriminatorValue("ACTIVE_USER_OF")
@Data
@NoArgsConstructor
public class RuleActiveUserOf extends RuleArguments{

    private BankProductType productType;

    @Override
    public String getSubQuery() {
        return "";
    }
}
