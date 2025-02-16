package com.star.bank.model.rule;

import com.star.bank.exception.InvalidQueryArgumentsException;
import com.star.bank.model.enums.BankProductType;
import com.star.bank.model.enums.QueryType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "rule_active_user_of")
@DiscriminatorValue("ACTIVE_USER_OF")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RuleActiveUserOf extends RuleArguments {

    private BankProductType productType;

    public RuleActiveUserOf(List<String> arguments) {
        super();
        if (arguments == null || arguments.size() != 1) {
            throw new InvalidQueryArgumentsException(QueryType.ACTIVE_USER_OF, arguments != null ? arguments : List.of());
        }
        try {
            this.productType = BankProductType.valueOf(arguments.get(0));
        } catch (IllegalArgumentException e) {
            throw new InvalidQueryArgumentsException(QueryType.ACTIVE_USER_OF, arguments);
        }
    }

    @Override
    public String getSubQuery() {
        return "";
    }

    @Override
    public List<String> convertToList() {
        return List.of(productType.name());
    }
}
