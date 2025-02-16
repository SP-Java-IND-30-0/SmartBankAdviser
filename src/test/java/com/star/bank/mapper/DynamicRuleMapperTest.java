package com.star.bank.mapper;

import com.star.bank.model.dto.DynamicRuleDto;
import com.star.bank.model.dto.SimpleRuleDto;
import com.star.bank.model.enums.BankProductType;
import com.star.bank.model.enums.CompareType;
import com.star.bank.model.enums.OperationType;
import com.star.bank.model.enums.QueryType;
import com.star.bank.model.product.DynamicRule;
import com.star.bank.model.rule.RuleCompareSum;
import com.star.bank.model.rule.SimpleRule;
import com.star.bank.service.SimpleRuleFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.UUID;

import static com.star.bank.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class DynamicRuleMapperTest {

    ModelMapper modelMapper;
    DynamicRuleMapper out;
    SimpleRuleFactory simpleRuleFactory;
    SimpleRuleMapper simpleRuleMapper;

    @BeforeEach
    void setUp() {
        simpleRuleFactory = new SimpleRuleFactory();
        modelMapper = new ModelMapper();
        modelMapper.createTypeMap(SimpleRule.class, SimpleRuleDto.class)
                .addMappings(mapper -> mapper.map(scr -> scr.getArguments().convertToList(), SimpleRuleDto::setArguments));
        modelMapper.createTypeMap(SimpleRuleDto.class, SimpleRule.class)
                .addMappings(mapper -> mapper.skip(SimpleRule::setArguments));

        modelMapper.createTypeMap(DynamicRuleDto.class, DynamicRule.class)
                .addMappings(mapper -> mapper.skip(DynamicRule::setRules));
        modelMapper.createTypeMap(DynamicRule.class, DynamicRuleDto.class)
                .addMappings(mapper -> mapper.skip(DynamicRuleDto::setRules));

        simpleRuleMapper = new SimpleRuleMapper(modelMapper, simpleRuleFactory);

        out = new DynamicRuleMapper(modelMapper, simpleRuleMapper);
    }

    @Test
    void test_toDto() {
        SimpleRule rule1 = createTestSimpleRule(1).query(QueryType.USER_OF).arguments(USER_OF_DEBIT).build();
        SimpleRule rule2 = createTestSimpleRule(2).query(QueryType.TRANSACTION_SUM_COMPARE)
                .arguments(RuleCompareSum.builder()
                        .productType(BankProductType.DEBIT)
                        .operationType(OperationType.DEPOSIT)
                        .compareType(CompareType.GE)
                        .amount(10000)
                        .build())
                .build();
        DynamicRule source = createTestDynamicRule()
                .id(UUID.randomUUID())
                .name("test")
                .text("test description")
                .addRule(rule1)
                .addRule(rule2)
                .build();

        DynamicRuleDto dto = out.toDto(source);

        assertNotNull(dto);
        assertEquals(dto.getProductId(), source.getProductId());
        assertEquals(dto.getProductName(), source.getProductName());
        assertEquals(2, dto.getRules().size());
    }

    @Test
    void toEntity() {
        SimpleRuleDto ruleDto1 = createTestSimpleRuleDto().query(QueryType.USER_OF).arguments(List.of(BankProductType.DEBIT.name())).build();
        SimpleRuleDto ruleDto2 = createTestSimpleRuleDto().query(QueryType.TRANSACTION_SUM_COMPARE)
                .arguments(List.of(BankProductType.DEBIT.name(), OperationType.DEPOSIT.name(), CompareType.GE.getValue(), Integer.toString(10000)))
                .build();

        DynamicRuleDto source = createTestDynamicRuleDto()
                .id(UUID.randomUUID())
                .name("test")
                .text("test description")
                .addRule(ruleDto1)
                .addRule(ruleDto2)
                .build();

        DynamicRule entity = out.toEntity(source);

        assertNotNull(entity);
        assertEquals(source.getProductId(), entity.getProductId());
        assertEquals(source.getProductName(), entity.getProductName());
        assertEquals(2, entity.getRules().size());


    }
}