package com.star.bank;

import com.star.bank.model.enums.BankProductType;
import com.star.bank.model.enums.CompareType;
import com.star.bank.model.enums.OperationType;
import com.star.bank.model.enums.QueryType;
import com.star.bank.model.product.DynamicRule;
import com.star.bank.model.rule.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class TestDynamicRule {


    public static DynamicRule getAlwaysTrueProduct() {
        DynamicRule rule = new DynamicRule();
        rule.setProductId(UUID.randomUUID());
        rule.setProductName("Product for all users");
        rule.setProductText("This products rules always return true");
        rule.setRules(new HashSet<>());
        return rule;
    }

    public static DynamicRule getAlwaysFalseProduct() {
        DynamicRule rule = new DynamicRule();
        rule.setProductId(UUID.randomUUID());
        rule.setProductName("Product for no one");
        rule.setProductText("This products rules always return false");

        SimpleRule rule1 = new SimpleRule();
        rule1.setQueryType(QueryType.TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW);
        rule1.setNegate(false);
        rule1.setArguments(new RuleCompareOperationSum(BankProductType.DEBIT, CompareType.GT));

        SimpleRule rule2 = new SimpleRule();
        rule2.setQueryType(QueryType.TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW);
        rule2.setNegate(false);
        rule2.setArguments(new RuleCompareOperationSum(BankProductType.DEBIT, CompareType.LT));

        rule.setRules(Set.of(rule1, rule2));
        return rule;
    }

    public static DynamicRule getTestProduct() {
        DynamicRule rule = new DynamicRule();
        rule.setProductId(UUID.randomUUID());
        rule.setProductName("Test product");
        rule.setProductText("This products rules test");

        SimpleRule rule1 = new SimpleRule();
        rule1.setQueryType(QueryType.ACTIVE_USER_OF);
        rule1.setNegate(true);
        rule1.setArguments(new RuleUserOf(BankProductType.DEBIT));

        SimpleRule rule2 = new SimpleRule();
        rule2.setQueryType(QueryType.TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW);
        rule2.setNegate(false);
        rule2.setArguments(new RuleCompareOperationSum(BankProductType.CREDIT, CompareType.GT));

        SimpleRule rule3 = new SimpleRule();
        rule3.setQueryType(QueryType.TRANSACTION_SUM_COMPARE);
        rule3.setNegate(false);
        rule3.setArguments(new RuleCompareSum(BankProductType.SAVING, OperationType.DEPOSIT, CompareType.LE, 1000));

        rule.setRules(Set.of(rule1, rule2, rule3));
        return rule;
    }
}
