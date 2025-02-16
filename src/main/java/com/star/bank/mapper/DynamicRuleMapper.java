package com.star.bank.mapper;

import com.star.bank.model.dto.DynamicRuleDto;
import com.star.bank.model.product.DynamicRule;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class DynamicRuleMapper {

    private final ModelMapper modelMapper;
    private final SimpleRuleMapper simpleRuleMapper;

    public DynamicRuleMapper(ModelMapper modelMapper, SimpleRuleMapper simpleRuleMapper) {
        this.modelMapper = modelMapper;
        this.simpleRuleMapper = simpleRuleMapper;
    }

    public DynamicRuleDto toDto(DynamicRule dynamicRule) {
        DynamicRuleDto dto = modelMapper.map(dynamicRule, DynamicRuleDto.class);
        dto.setRules(
                dynamicRule.getRules().stream()
                        .map(simpleRuleMapper::toDto)
                        .collect(Collectors.toSet())
        );
        return dto;
    }

    public DynamicRule toEntity(DynamicRuleDto dynamicRuleDto) {
        DynamicRule entity = modelMapper.map(dynamicRuleDto, DynamicRule.class);
        entity.setRules(
                dynamicRuleDto.getRules().stream()
                        .map(simpleRuleMapper::toEntity)
                        .collect(Collectors.toSet())
        );
        return entity;
    }
}
