package com.star.bank.service;

import com.star.bank.mapper.DynamicRuleMapper;
import com.star.bank.model.dto.DynamicRuleDto;
import com.star.bank.model.product.DynamicRule;
import com.star.bank.exception.InvalidProductIdException;
import com.star.bank.exception.ProductNotFoundException;
import com.star.bank.repositories.DynamicRuleRepository;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class DynamicRuleService implements DynamicRuleRepository {

    private final DynamicRuleRepository dynamicRuleRepository;
    private final DynamicRuleMapper dynamicRuleMapper;

    public DynamicRuleService(DynamicRuleRepository dynamicRuleRepository, DynamicRuleMapper dynamicRuleMapper) {
        this.dynamicRuleRepository = dynamicRuleRepository;
        this.dynamicRuleMapper = dynamicRuleMapper;
    }

    public void deleteDynamicRule(String productId) {
        UUID uuid;
        if (productId == null || productId.isBlank()) {
            throw new IllegalArgumentException();
        }

        try {
            uuid = UUID.fromString(productId);
        } catch (IllegalArgumentException e) {
            throw new InvalidProductIdException(productId);
        }

        if (!dynamicRuleRepository.findById(uuid)) {
            throw new ProductNotFoundException(productId);
        }

        dynamicRuleRepository.deleteById(uuid);
    }

    public void saveDynamicRule(DynamicRuleDto dynamicRuleDto) {
        if (dynamicRuleDto == null) {
            throw new IllegalArgumentException();
        }

        DynamicRule dynamicRule = dynamicRuleMapper.toEntity(dynamicRuleDto);

        dynamicRuleRepository.save(dynamicRule);
    }

    public List<DynamicRuleDto> getDynamicRules() {
        List<DynamicRule> dynamicRules = dynamicRuleRepository.findAll();

        if (dynamicRules.isEmpty()) {
            throw new EntityNotFoundException();
        }

        List<DynamicRuleDto> dynamicRuleDtos = new ArrayList<>();
        for (DynamicRule dynamicRule : dynamicRules) {
            dynamicRuleDtos.add(dynamicRuleMapper.toDto(dynamicRule));
        }

        return dynamicRuleDtos;
    }
}