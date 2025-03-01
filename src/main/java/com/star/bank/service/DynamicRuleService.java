package com.star.bank.service;

import com.star.bank.event.DeleteDynamicRuleEvent;
import com.star.bank.exception.*;
import com.star.bank.mapper.DynamicRuleMapper;
import com.star.bank.model.dto.DynamicRuleDto;
import com.star.bank.model.enums.BankProductType;
import com.star.bank.model.product.DynamicRule;
import com.star.bank.model.rule.*;
import com.star.bank.repositories.DynamicRuleRepository;
import com.star.bank.repositories.RuleRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DynamicRuleService {

    private final DynamicRuleRepository dynamicRuleRepository;
    private final DynamicRuleMapper dynamicRuleMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final RuleRepository ruleRepository;
    private final EntityManager entityManager;

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
            eventPublisher.publishEvent(new DeleteDynamicRuleEvent(this, uuid));
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

        DynamicRule dynamicRule = dynamicRuleMapper.toEntity(dynamicRuleDto);
        Set<SimpleRule> updatedRules = Optional.ofNullable(dynamicRule.getRules()).orElse(new HashSet<>());

        for (SimpleRule rule : updatedRules) {
            RuleArguments arguments = rule.getArguments();
//            BankProductType productType = extractProductType(arguments);

            RuleArguments updatedArguments = ruleRepository.findRuleArguments(arguments).orElse(arguments);
            rule.setArguments(updatedArguments);
//            if (!ruleRepository.ruleExists(rule, productType)) {
//                entityManager.persist(rule);
//            } else {
//                entityManager.merge(rule);
//            }
        }

        dynamicRule.setRules(updatedRules);

        try {
            dynamicRuleRepository.save(dynamicRule);
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

//    private BankProductType extractProductType(RuleArguments arguments) {
//        if (arguments instanceof RuleCompareSum) {
//            return ((RuleCompareSum) arguments).getProductType();
//        } else if (arguments instanceof RuleCompareOperationSum) {
//            return ((RuleCompareOperationSum) arguments).getProductType();
//        } else if (arguments instanceof RuleUserOf) {
//            return ((RuleUserOf) arguments).getProductType();
//        } else if (arguments instanceof RuleActiveUserOf) {
//            return ((RuleActiveUserOf) arguments).getProductType();
//        } else {
//            throw new IllegalArgumentException("Не удалось извлечь тип продукта");
//        }
//    }
}