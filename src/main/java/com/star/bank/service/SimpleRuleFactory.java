package com.star.bank.service;

import com.star.bank.model.enums.QueryType;
import com.star.bank.model.rule.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SimpleRuleFactory {

    public RuleArguments getSimpleRule(QueryType queryType, List<String> arguments) {
        return switch (queryType) {
            case USER_OF -> new RuleUserOf(arguments);
            case ACTIVE_USER_OF -> new RuleActiveUserOf(arguments);
            case TRANSACTION_SUM_COMPARE -> new RuleCompareSum(arguments);
            case TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW -> new RuleCompareOperationSum(arguments);
        };
    }
}
