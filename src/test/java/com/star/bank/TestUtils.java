package com.star.bank;

import com.star.bank.model.dto.DynamicRuleDto;
import com.star.bank.model.dto.SimpleRuleDto;
import com.star.bank.model.enums.BankProductType;
import com.star.bank.model.enums.QueryType;
import com.star.bank.model.product.DynamicRule;
import com.star.bank.model.rule.RuleActiveUserOf;
import com.star.bank.model.rule.RuleArguments;
import com.star.bank.model.rule.RuleUserOf;
import com.star.bank.model.rule.SimpleRule;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class TestUtils {

    public static final RuleArguments USER_OF_DEBIT = new RuleUserOf(BankProductType.DEBIT);
    public static final RuleArguments USER_OF_CREDIT = new RuleUserOf(BankProductType.CREDIT);
    public static final RuleArguments USER_OF_SAVING = new RuleUserOf(BankProductType.SAVING);
    public static final RuleArguments USER_OF_INVEST = new RuleUserOf(BankProductType.INVEST);

    public static final RuleArguments ACTIVE_USER_OF_DEBIT = new RuleActiveUserOf(BankProductType.DEBIT);
    public static final RuleArguments ACTIVE_USER_OF_CREDIT = new RuleUserOf(BankProductType.CREDIT);
    public static final RuleArguments ACTIVE_USER_OF_SAVING = new RuleUserOf(BankProductType.SAVING);
    public static final RuleArguments ACTIVE_USER_OF_INVEST = new RuleUserOf(BankProductType.INVEST);

    public static TestDynamicRule createTestDynamicRule() {
        return new TestDynamicRule();
    }

    public static TestDynamicRuleDto createTestDynamicRuleDto() {
        return new TestDynamicRuleDto();
    }

    public static TestSimpleRule createTestSimpleRule(int id) {
        return new TestSimpleRule(id);
    }

    public static TestSimpleRuleDto createTestSimpleRuleDto() {
        return new TestSimpleRuleDto();
    }

    public static class TestDynamicRule {
        DynamicRule dynamicRule;

        public TestDynamicRule() {
            dynamicRule = new DynamicRule();
            dynamicRule.setRules(new HashSet<>());
        }

        public TestDynamicRule id(UUID id) {
            dynamicRule.setProductId(id);
            return this;
        }

        public TestDynamicRule name(String name) {
            dynamicRule.setProductName(name);
            return this;
        }

        public TestDynamicRule text(String text) {
            dynamicRule.setProductText(text);
            return this;
        }

        public TestDynamicRule addRule(SimpleRule rule) {
            dynamicRule.getRules().add(rule);
            return this;
        }

        public DynamicRule build() {
            return dynamicRule;
        }
    }

    public static class TestDynamicRuleDto {
        DynamicRuleDto dynamicRuleDto;

        public TestDynamicRuleDto() {
            dynamicRuleDto = new DynamicRuleDto();
            dynamicRuleDto.setRules(new HashSet<>());
        }

        public TestDynamicRuleDto id(UUID id) {
            dynamicRuleDto.setProductId(id);
            return this;
        }

        public TestDynamicRuleDto name(String name) {
            dynamicRuleDto.setProductName(name);
            return this;
        }

        public TestDynamicRuleDto text(String text) {
            dynamicRuleDto.setProductText(text);
            return this;
        }

        public TestDynamicRuleDto addRule(SimpleRuleDto rule) {
            dynamicRuleDto.getRules().add(rule);
            return this;
        }

        public DynamicRuleDto build() {
            return dynamicRuleDto;
        }
    }

    public static class TestSimpleRule {
            SimpleRule simpleRule;

            public TestSimpleRule(int id) {
                simpleRule = new SimpleRule();
                simpleRule.setId(id);
            }

            public TestSimpleRule query(QueryType queryType) {
                simpleRule.setQueryType(queryType);
                return this;
            }

            public TestSimpleRule negate(boolean negate) {
                simpleRule.setNegate(negate);
                return this;
            }

            public TestSimpleRule arguments(RuleArguments arguments) {
                simpleRule.setArguments(arguments);
                return this;
            }

            public SimpleRule build() {
                return simpleRule;
            }
    }

    public static class TestSimpleRuleDto {
        SimpleRuleDto simpleRuleDto;

        public TestSimpleRuleDto() {
            simpleRuleDto = new SimpleRuleDto();
        }

        public TestSimpleRuleDto query(QueryType queryType) {
            simpleRuleDto.setQueryType(queryType);
            return this;
        }

        public TestSimpleRuleDto negate(boolean negate) {
            simpleRuleDto.setNegate(negate);
            return this;
        }

        public TestSimpleRuleDto arguments(List<String> arguments) {
            simpleRuleDto.setArguments(arguments);
            return this;
        }

        public SimpleRuleDto build() {
            return simpleRuleDto;
        }
    }
}
