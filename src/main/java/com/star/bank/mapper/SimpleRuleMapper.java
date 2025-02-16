package com.star.bank.mapper;

import com.star.bank.model.dto.SimpleRuleDto;
import com.star.bank.model.rule.SimpleRule;
import com.star.bank.service.SimpleRuleFactory;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class SimpleRuleMapper {

    private final ModelMapper modelMapper;
    private final SimpleRuleFactory simpleRuleFactory;

    public SimpleRuleMapper(ModelMapper modelMapper, SimpleRuleFactory simpleRuleFactory) {
        this.modelMapper = modelMapper;
        this.simpleRuleFactory = simpleRuleFactory;
    }

    public SimpleRuleDto toDto(SimpleRule simpleRule) {
        return modelMapper.map(simpleRule, SimpleRuleDto.class);
    }

    public SimpleRule toEntity(SimpleRuleDto simpleRuleDto) {
        SimpleRule simpleRule = modelMapper.map(simpleRuleDto, SimpleRule.class);
        simpleRule.setArguments(simpleRuleFactory.getSimpleRule(simpleRule.getQueryType(), simpleRuleDto.getArguments()));
        return simpleRule;
    }
}
