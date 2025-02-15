package com.star.bank.mapper;

import com.star.bank.model.dto.DynamicRuleDto;
import com.star.bank.model.product.DynamicRule;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class DynamicRuleMapper {

    private final ModelMapper modelMapper;

    public DynamicRuleMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public DynamicRuleDto toDto(DynamicRule dynamicRule) {
        return modelMapper.map(dynamicRule, DynamicRuleDto.class);
    }

    public DynamicRule toEntity(DynamicRuleDto dynamicRuleDto) {
        return modelMapper.map(dynamicRuleDto, DynamicRule.class);
    }
}
