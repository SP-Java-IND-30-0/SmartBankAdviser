package com.star.bank.service;

import com.star.bank.mapper.DynamicRuleMapper;
import com.star.bank.model.dto.DynamicRuleDto;
import com.star.bank.model.product.DynamicRule;
import com.star.bank.exception.InvalidProductIdException;
import com.star.bank.exception.ProductNotFoundException;
import com.star.bank.exception.DatabaseSaveException;
import com.star.bank.exception.DatabaseAccessException;
import com.star.bank.repositories.DynamicRuleRepository;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
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
        try {
            if (productId == null || productId.isBlank()) {
                throw new IllegalArgumentException();
            }
            uuid = UUID.fromString(productId);
        } catch (IllegalArgumentException e) {
            throw new InvalidProductIdException(productId);
        }

        try {
            dynamicRuleRepository.deleteById(uuid);
        } catch (EmptyResultDataAccessException e) {
            throw new ProductNotFoundException(uuid);
        }
    }

    public void saveDynamicRule(DynamicRuleDto dynamicRuleDto) {
        try {
            if (dynamicRuleDto == null) {
                throw new IllegalArgumentException();
            }

            DynamicRule dynamicRule = dynamicRuleMapper.toEntity(dynamicRuleDto);
            dynamicRuleRepository.save(dynamicRule);

        } catch (IllegalArgumentException e) {
            throw e;

        } catch (DataAccessException e) {
            throw new DatabaseSaveException(e);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<DynamicRuleDto> getDynamicRules() {
        try {
            List<DynamicRule> dynamicRules = dynamicRuleRepository.findAll();
            if (dynamicRules.isEmpty()) {
                return new ArrayList<>();
            }

            List<DynamicRuleDto> dynamicRuleDtos = new ArrayList<>();
            for (DynamicRule dynamicRule : dynamicRules) {
                dynamicRuleDtos.add(dynamicRuleMapper.toDto(dynamicRule));
            }

            return dynamicRuleDtos;

        } catch (DataAccessException e) {
            throw new DatabaseAccessException(e);

        } catch (EntityNotFoundException e) {
            throw e;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}