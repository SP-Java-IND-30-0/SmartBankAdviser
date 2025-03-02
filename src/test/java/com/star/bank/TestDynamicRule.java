package com.star.bank;

import com.star.bank.model.dto.DynamicRuleDto;
import com.star.bank.model.dto.SimpleRuleDto;
import com.star.bank.model.enums.QueryType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class TestDynamicRule {


    public static DynamicRuleDto getAlwaysTrueProduct() {
        DynamicRuleDto rule = new DynamicRuleDto();
        rule.setProductId(UUID.randomUUID());
        rule.setProductName("Product for all users");
        rule.setProductText("This products rules always return true");
        rule.setRules(new HashSet<>());
        return rule;
    }

    public static DynamicRuleDto getAlwaysFalseProduct() {
        DynamicRuleDto rule = new DynamicRuleDto();
        rule.setProductId(UUID.randomUUID());
        rule.setProductName("Product for no one");
        rule.setProductText("This products rules always return false");

        SimpleRuleDto rule1 = new SimpleRuleDto();
        rule1.setQueryType(QueryType.TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW);
        rule1.setNegate(false);
        rule1.setArguments(List.of("DEBIT", ">"));

        SimpleRuleDto rule2 = new SimpleRuleDto();
        rule2.setQueryType(QueryType.TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW);
        rule2.setNegate(false);
        rule2.setArguments(List.of("DEBIT", "<"));

        rule.setRules(Set.of(rule1, rule2));
        return rule;
    }

    public static DynamicRuleDto getTestProduct() {
        DynamicRuleDto rule = new DynamicRuleDto();
        rule.setProductId(UUID.randomUUID());
        rule.setProductName("Test product");
        rule.setProductText("This products rules test");

        SimpleRuleDto rule1 = new SimpleRuleDto();
        rule1.setQueryType(QueryType.ACTIVE_USER_OF);
        rule1.setNegate(true);
        rule1.setArguments(List.of("DEBIT"));

        SimpleRuleDto rule2 = new SimpleRuleDto();
        rule2.setQueryType(QueryType.TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW);
        rule2.setNegate(false);
        rule2.setArguments(List.of("CREDIT", ">"));

        SimpleRuleDto rule3 = new SimpleRuleDto();
        rule3.setQueryType(QueryType.TRANSACTION_SUM_COMPARE);
        rule3.setNegate(false);
        rule3.setArguments(List.of("SAVING", "DEPOSIT", "<=", "1000"));

        rule.setRules(Set.of(rule1, rule2, rule3));
        return rule;
    }
}
