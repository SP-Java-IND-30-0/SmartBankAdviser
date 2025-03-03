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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Сервис для работы с динамическими правилами.
 * Предоставляет методы для сохранения, удаления и получения динамических правил.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DynamicRuleService {

    private final DynamicRuleRepository dynamicRuleRepository;
    private final DynamicRuleMapper dynamicRuleMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final RuleRepository ruleRepository;

    /**
     * Удаляет динамическое правило по его идентификатору.
     *
     * @param productId Идентификатор продукта, связанного с динамическим правилом.
     * @throws InvalidProductIdException Если идентификатор продукта неверен.
     * @throws ProductNotFoundException Если продукт не найден.
     * @throws DatabaseAccessException Если возникает ошибка доступа к базе данных.
     */
    @Transactional
    public void deleteDynamicRule(String productId) {
        UUID uuid;
        try {
            if (productId == null || productId.isBlank()) {
                throw new EmptyValueException("productId");
            }
            uuid = UUID.fromString(productId);
        } catch (IllegalArgumentException e) {
            throw new InvalidProductIdException(productId, e);
        }

        try {
            dynamicRuleRepository.deleteById(uuid);
            eventPublisher.publishEvent(new DeleteDynamicRuleEvent(this, uuid));
            log.info("Dynamic rule with productId {} deleted", uuid);
        } catch (EmptyResultDataAccessException e) {
            throw new ProductNotFoundException(uuid, e);
        } catch (DataAccessException e) {
            throw new DatabaseAccessException(productId, e);
        }
    }

    /**
     * Сохраняет динамическое правило.
     *
     * @param dynamicRuleDto DTO с данными динамического правила.
     * @throws IllegalArgumentException Если переданное DTO равно null.
     * @throws InvalidRuleDataException Если данные правила неверны.
     * @throws DatabaseSaveException Если возникает ошибка при сохранении в базе данных.
     */
    @Transactional
    public void saveDynamicRule(DynamicRuleDto dynamicRuleDto) {
        if (dynamicRuleDto == null) {
            throw new EmptyValueException("DynamicRuleDto");
        }

        DynamicRule dynamicRule = dynamicRuleMapper.toEntity(dynamicRuleDto);
        Set<SimpleRule> updatedRules = Optional.ofNullable(dynamicRule.getRules()).orElse(new HashSet<>());

        for (SimpleRule rule : updatedRules) {
            RuleArguments arguments = rule.getArguments();

            RuleArguments updatedArguments = ruleRepository.findRuleArguments(arguments).orElse(arguments);
            rule.setArguments(updatedArguments);
        }

        dynamicRule.setRules(updatedRules);

        try {
            dynamicRuleRepository.save(dynamicRule);
            log.info("Dynamic rule with productId {} saved", dynamicRule.getProductId());
        } catch (DataIntegrityViolationException e) {
            throw new InvalidRuleDataException(e);
        } catch (DataAccessException e) {
            throw new DatabaseSaveException(e);
        }
    }

    /**
     * Возвращает список всех динамических правил.
     *
     * @return Список DTO с данными динамических правил.
     * @throws DatabaseAccessException Если возникает ошибка доступа к базе данных.
     */
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