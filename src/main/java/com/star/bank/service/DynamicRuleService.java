package com.star.bank.service;

import com.star.bank.exception.*;
import com.star.bank.mapper.DynamicRuleMapper;
import com.star.bank.model.dto.DynamicRuleDto;
import com.star.bank.model.product.DynamicRule;
import com.star.bank.repositories.DynamicRuleRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
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

    @Transactional
    public void deleteDynamicRule(String productId) {
        UUID uuid;
        try {
            if (productId == null || productId.isBlank()) {
                throw new IllegalArgumentException();
            }
            uuid = UUID.fromString(productId);
        } catch (IllegalArgumentException e) {
            throw new InvalidProductIdException(productId, e);
        }

        try {
            dynamicRuleRepository.deleteById(uuid);
        } catch (EmptyResultDataAccessException e) {
            throw new ProductNotFoundException(uuid, e);
        } catch (DataAccessException e) {
            throw new DatabaseAccessException(productId, e);
        }
    }

    @Transactional
    public void saveDynamicRule(DynamicRuleDto dynamicRuleDto) {
        if (dynamicRuleDto == null) {
            throw new IllegalArgumentException();
        }

        // TODO добавить дополнительные логические проверки

        try {
            DynamicRule dynamicRule = dynamicRuleMapper.toEntity(dynamicRuleDto);
            dynamicRuleRepository.save(dynamicRule);
        } catch (ConstraintViolationException e) {
            throw new DuplicateRuleException(e.getMessage(), e);
        } catch (DataIntegrityViolationException e) {
            throw new InvalidRuleDataException(e);
        } catch (DataAccessException e) {
            throw new DatabaseSaveException(e);
        }
    }

    public List<DynamicRuleDto> getDynamicRules() {
        try {
            List<DynamicRule> dynamicRules = dynamicRuleRepository.findAll();

            List<DynamicRuleDto> dynamicRuleDtos = new ArrayList<>();
            for (DynamicRule dynamicRule : dynamicRules) {
                dynamicRuleDtos.add(dynamicRuleMapper.toDto(dynamicRule));
            }

            return dynamicRuleDtos;

        } catch (DataAccessException e) {
            throw new DatabaseAccessException(e);

        }
    }
}