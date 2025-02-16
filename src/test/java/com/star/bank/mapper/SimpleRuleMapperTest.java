package com.star.bank.mapper;

import com.star.bank.model.dto.SimpleRuleDto;
import com.star.bank.model.enums.BankProductType;
import com.star.bank.model.enums.QueryType;
import com.star.bank.model.rule.SimpleRule;
import com.star.bank.service.SimpleRuleFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.List;

import static com.star.bank.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

class SimpleRuleMapperTest {

    private SimpleRuleMapper out;

    @BeforeEach
    void setUp() {
        ModelMapper modelMapper = new ModelMapper();
        SimpleRuleFactory simpleRuleFactory = new SimpleRuleFactory();
        modelMapper.createTypeMap(SimpleRule.class, SimpleRuleDto.class)
                .addMappings(mapper -> mapper.map(scr -> scr.getArguments().convertToList(), SimpleRuleDto::setArguments));
        modelMapper.createTypeMap(SimpleRuleDto.class, SimpleRule.class)
                .addMappings(mapper -> mapper.skip(SimpleRule::setArguments));
        out = new SimpleRuleMapper(modelMapper, simpleRuleFactory);
    }

    @Test
    void toDto() {
        SimpleRule rule = createTestSimpleRule(1).query(QueryType.USER_OF).arguments(USER_OF_DEBIT).build();
        SimpleRuleDto ruleDto = out.toDto(rule);

        assertNotNull(ruleDto);
        assertEquals(QueryType.USER_OF, ruleDto.getQueryType());
        assertEquals(1, ruleDto.getArguments().size());
        assertEquals(BankProductType.DEBIT.name(), ruleDto.getArguments().get(0));


    }

    @Test
    void fromDto() {
        SimpleRuleDto dto = createTestSimpleRuleDto().query(QueryType.USER_OF).arguments(List.of(BankProductType.DEBIT.name())).build();
        SimpleRule rule = out.toEntity(dto);

        assertNotNull(rule);
        assertEquals(QueryType.USER_OF, rule.getQueryType());
        assertEquals(USER_OF_DEBIT,rule.getArguments());

    }
}