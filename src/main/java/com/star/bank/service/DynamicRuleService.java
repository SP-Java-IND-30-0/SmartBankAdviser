package com.star.bank.service;

import com.star.bank.event.DeleteDynamicRuleEvent;
import com.star.bank.exception.*;
import com.star.bank.mapper.DynamicRuleMapper;
import com.star.bank.model.dto.DynamicRuleDto;
import com.star.bank.model.product.DynamicRule;
import com.star.bank.model.rule.RuleArguments;
import com.star.bank.model.rule.SimpleRule;
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

        Set<SimpleRule> updatedRules = dynamicRule.getRules() != null ? dynamicRule.getRules() : new HashSet<>();

        for (SimpleRule rule : dynamicRule.getRules()) {
            RuleArguments arguments = rule.getArguments();

            if (arguments.getId() == 0) {
                boolean exists = ruleRepository.existsRuleByType(0, arguments);
                if (exists) {
                    throw new IllegalStateExceptionWithDetails("Rule", String.valueOf(arguments.getId()));
                }
            }

            Optional<SimpleRule> existingRuleOptional = ruleRepository.findRuleById(arguments.getId());

            if (existingRuleOptional.isPresent()) {
                SimpleRule existingRule = existingRuleOptional.get();
                existingRule.setQueryType(rule.getQueryType());
                existingRule.setNegate(rule.isNegate());
                existingRule.setArguments(rule.getArguments());

                updatedRules.add(existingRule);
            } else {
                updatedRules.add(rule);
            }
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
}