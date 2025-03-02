package com.star.bank.service;

import com.star.bank.TestUtils;
import com.star.bank.event.DeleteDynamicRuleEvent;
import com.star.bank.exception.*;
import com.star.bank.mapper.DynamicRuleMapper;
import com.star.bank.model.dto.DynamicRuleDto;
import com.star.bank.model.product.DynamicRule;
import com.star.bank.model.rule.SimpleRule;
import com.star.bank.repositories.DynamicRuleRepository;
import com.star.bank.repositories.RuleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@ExtendWith(MockitoExtension.class)
class DynamicRuleServiceTest {

    @Mock
    private DynamicRuleRepository dynamicRuleRepository;

    @Mock
    private RuleRepository ruleRepository;

    @Mock
    private DynamicRuleMapper dynamicRuleMapper;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private DynamicRuleService dynamicRuleService;

    @Test
    void shouldSaveDynamicRuleSuccessfully() {
        DynamicRuleDto dto = TestUtils.createDynamicRuleDto();
        DynamicRule entity = TestUtils.createTestDynamicRule().id(UUID.fromString(TestUtils.PRODUCT_ID)).build();

        when(dynamicRuleMapper.toEntity(dto)).thenReturn(entity);
        when(dynamicRuleRepository.save(entity)).thenReturn(entity);

        dynamicRuleService.saveDynamicRule(dto);

        verify(dynamicRuleRepository, times(1)).save(entity);
        verify(ruleRepository, times(entity.getRules().size())).findRuleArguments(any());
    }

    @Test
    void shouldThrowExceptionWhenDtoIsNull() {
        assertThrows(IllegalArgumentException.class, () -> dynamicRuleService.saveDynamicRule(null));
    }

    @Test
    void shouldUpdateRuleArgumentsIfFound() {
        SimpleRule rule = new SimpleRule();
        rule.setArguments(TestUtils.USER_OF_DEBIT);

        DynamicRule entity = TestUtils.createTestDynamicRule().addRule(rule).build();

        when(dynamicRuleMapper.toEntity(any())).thenReturn(entity);
        when(ruleRepository.findRuleArguments(TestUtils.USER_OF_DEBIT)).thenReturn(Optional.of(TestUtils.USER_OF_CREDIT));
        when(dynamicRuleRepository.save(entity)).thenReturn(entity);

        dynamicRuleService.saveDynamicRule(TestUtils.createDynamicRuleDto());

        Assertions.assertEquals(TestUtils.USER_OF_CREDIT, rule.getArguments());
    }

    @Test
    void shouldNotUpdateRuleArgumentsIfNotFound() {
        SimpleRule rule = new SimpleRule();
        rule.setArguments(TestUtils.USER_OF_DEBIT);

        DynamicRule entity = TestUtils.createTestDynamicRule().addRule(rule).build();

        when(dynamicRuleMapper.toEntity(any())).thenReturn(entity);
        when(ruleRepository.findRuleArguments(TestUtils.USER_OF_DEBIT)).thenReturn(Optional.empty());
        when(dynamicRuleRepository.save(entity)).thenReturn(entity);

        dynamicRuleService.saveDynamicRule(TestUtils.createDynamicRuleDto());

        Assertions.assertEquals(TestUtils.USER_OF_DEBIT, rule.getArguments());
    }

    @Test
    void shouldThrowInvalidRuleDataExceptionOnDataIntegrityViolation() {
        DynamicRuleDto dto = TestUtils.createDynamicRuleDto();
        DynamicRule entity = TestUtils.createTestDynamicRule().build();

        when(dynamicRuleMapper.toEntity(dto)).thenReturn(entity);
        when(dynamicRuleRepository.save(entity)).thenThrow(new DataIntegrityViolationException(""));

        assertThrows(InvalidRuleDataException.class, () -> dynamicRuleService.saveDynamicRule(dto));
    }

    @Test
    void shouldThrowDatabaseSaveExceptionOnDataAccessException() {
        DynamicRuleDto dto = TestUtils.createDynamicRuleDto();
        DynamicRule entity = TestUtils.createTestDynamicRule().build();

        when(dynamicRuleMapper.toEntity(dto)).thenReturn(entity);
        when(dynamicRuleRepository.save(entity)).thenThrow(new DataAccessException("") {});

        assertThrows(DatabaseSaveException.class, () -> dynamicRuleService.saveDynamicRule(dto));
    }

    @Test
    void shouldDeleteDynamicRuleSuccessfully() {
        String productId = TestUtils.PRODUCT_ID;
        UUID uuid = UUID.fromString(productId);

        dynamicRuleService.deleteDynamicRule(productId);
        verify(dynamicRuleRepository, times(1)).deleteById(uuid);
        verify(eventPublisher, times(1)).publishEvent(any(DeleteDynamicRuleEvent.class));
    }

    @Test
    void shouldThrowInvalidProductIdExceptionWhenProductIdIsNull() {
        assertThrows(InvalidProductIdException.class, () -> dynamicRuleService.deleteDynamicRule(null));
    }

    @Test
    void shouldThrowInvalidProductIdExceptionWhenProductIdIsBlank() {
        assertThrows(InvalidProductIdException.class, () -> dynamicRuleService.deleteDynamicRule("  "));
    }

    @Test
    void shouldThrowInvalidProductIdExceptionWhenProductIdIsInvalidUUID() {
        assertThrows(InvalidProductIdException.class, () -> dynamicRuleService.deleteDynamicRule("invalid-uuid"));
    }

    @Test
    void shouldThrowProductNotFoundExceptionWhenProductDoesNotExist() {
        String productId = TestUtils.PRODUCT_ID;
        UUID uuid = UUID.fromString(productId);
        doThrow(new EmptyResultDataAccessException(1)).when(dynamicRuleRepository).deleteById(uuid);

        assertThrows(ProductNotFoundException.class, () -> dynamicRuleService.deleteDynamicRule(productId));
    }

    @Test
    void shouldThrowDatabaseAccessExceptionWhenDeletingDynamicRule() {
        String productId = TestUtils.PRODUCT_ID;
        UUID uuid = UUID.fromString(productId);
        doThrow(new DataAccessException("DB error") {}).when(dynamicRuleRepository).deleteById(uuid);

        assertThrows(DatabaseAccessException.class, () -> dynamicRuleService.deleteDynamicRule(productId));
    }

    @Test
    void shouldReturnListOfDynamicRuleDtos() {
        DynamicRule rule1 = TestUtils.createTestDynamicRule().id(UUID.randomUUID()).build();
        DynamicRule rule2 = TestUtils.createTestDynamicRule().id(UUID.randomUUID()).build();

        DynamicRuleDto dto1 = TestUtils.createTestDynamicRuleDto().id(rule1.getProductId()).build();
        DynamicRuleDto dto2 = TestUtils.createTestDynamicRuleDto().id(rule2.getProductId()).build();

        when(dynamicRuleRepository.findAll()).thenReturn(List.of(rule1, rule2));
        when(dynamicRuleMapper.toDto(rule1)).thenReturn(dto1);
        when(dynamicRuleMapper.toDto(rule2)).thenReturn(dto2);

        List<DynamicRuleDto> result = dynamicRuleService.getDynamicRules();

        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.contains(dto1));
        Assertions.assertTrue(result.contains(dto2));
        verify(dynamicRuleRepository, times(1)).findAll();
        verify(dynamicRuleMapper, times(2)).toDto(any(DynamicRule.class));
    }

    @Test
    void shouldReturnEmptyListIfNoDynamicRulesExist() {
        when(dynamicRuleRepository.findAll()).thenReturn(Collections.emptyList());

        List<DynamicRuleDto> result = dynamicRuleService.getDynamicRules();

        Assertions.assertTrue(result.isEmpty());
        verify(dynamicRuleRepository, times(1)).findAll();
        verify(dynamicRuleMapper, never()).toDto(any());
    }

    @Test
    void shouldThrowDatabaseAccessExceptionWhenFetchingDynamicRules() {
        when(dynamicRuleRepository.findAll()).thenThrow(new DataAccessException("DB error") {});

        assertThrows(DatabaseAccessException.class, () -> dynamicRuleService.getDynamicRules());
    }
}