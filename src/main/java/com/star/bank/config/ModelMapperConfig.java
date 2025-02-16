package com.star.bank.config;

import com.star.bank.model.dto.DynamicRuleDto;
import com.star.bank.model.dto.SimpleRuleDto;
import com.star.bank.model.product.DynamicRule;
import com.star.bank.model.rule.SimpleRule;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.createTypeMap(SimpleRule.class, SimpleRuleDto.class)
                .addMappings(mapper -> mapper.map(scr -> scr.getArguments().convertToList(), SimpleRuleDto::setArguments));
        modelMapper.createTypeMap(SimpleRuleDto.class, SimpleRule.class)
                .addMappings(mapper -> mapper.skip(SimpleRule::setArguments));

        modelMapper.createTypeMap(DynamicRuleDto.class, DynamicRule.class)
                .addMappings(mapper -> mapper.skip(DynamicRule::setRules));
        modelMapper.createTypeMap(DynamicRule.class, DynamicRuleDto.class)
                .addMappings(mapper -> mapper.skip(DynamicRuleDto::setRules));

        return modelMapper;
    }
}
