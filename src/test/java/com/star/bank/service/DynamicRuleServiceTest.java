package com.star.bank.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.star.bank.TestUtils;
import com.star.bank.exception.DatabaseSaveException;
import com.star.bank.exception.IllegalStateExceptionWithDetails;
import com.star.bank.exception.InvalidRuleDataException;
import com.star.bank.mapper.DynamicRuleMapper;
import com.star.bank.model.dto.DynamicRuleDto;
import com.star.bank.model.dto.SimpleRuleDto;
import com.star.bank.model.enums.BankProductType;
import com.star.bank.model.enums.QueryType;
import com.star.bank.model.product.DynamicRule;
import com.star.bank.model.rule.RuleArguments;
import com.star.bank.model.rule.RuleUserOf;
import com.star.bank.model.rule.SimpleRule;
import com.star.bank.repositories.DynamicRuleRepository;
import com.star.bank.repositories.RuleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class DynamicRuleServiceTest {

    @Mock private DynamicRuleMapper dynamicRuleMapper;
    @Mock private RuleRepository ruleRepository;
    @Mock private DynamicRuleRepository dynamicRuleRepository;
    @InjectMocks private DynamicRuleService dynamicRuleService;

    private DynamicRuleDto dynamicRuleDto;
    private SimpleRuleDto simpleRuleDto;
    private DynamicRule dynamicRule;
    private SimpleRule simpleRule;

    @BeforeEach
    public void setUp() {
        simpleRuleDto = new SimpleRuleDto();
        simpleRuleDto.setArguments(Collections.singletonList("SOME_PRODUCT_TYPE"));
        simpleRuleDto.setQueryType(QueryType.USER_OF);
        simpleRuleDto.setNegate(false);

        dynamicRuleDto = TestUtils.createDynamicRuleDto();
        dynamicRule = TestUtils.createDynamicRule();

        lenient().when(dynamicRuleMapper.toEntity(dynamicRuleDto)).thenReturn(dynamicRule);
    }

    @Test
    public void testSaveDynamicRule_NullDto() {
        assertThrows(IllegalArgumentException.class, () -> dynamicRuleService.saveDynamicRule(null));
    }

    @Test
    public void testSaveDynamicRule_ExistingRule() {
        when(dynamicRuleMapper.toEntity(dynamicRuleDto)).thenReturn(dynamicRule);

        SimpleRule existingRule = TestUtils.createSimpleRule();
        RuleArguments arguments = new RuleUserOf(BankProductType.DEBIT);
        existingRule.setArguments(arguments);

        when(ruleRepository.findRuleById(anyInt())).thenReturn(Optional.of(existingRule));

        dynamicRuleService.saveDynamicRule(dynamicRuleDto);

        verify(ruleRepository).findRuleById(anyInt());
        verify(dynamicRuleRepository).save(any(DynamicRule.class));
    }

    @Test
    public void testSaveDynamicRule_DataIntegrityViolationException() {
        when(dynamicRuleRepository.save(any(DynamicRule.class)))
                .thenThrow(new DataIntegrityViolationException("Test"));

        assertThrows(InvalidRuleDataException.class, () -> dynamicRuleService.saveDynamicRule(dynamicRuleDto));
    }

    @Test
    public void testSaveDynamicRule_DataAccessException() {
        when(dynamicRuleRepository.save(any(DynamicRule.class)))
                .thenThrow(new DataAccessException("Test") {});

        assertThrows(DatabaseSaveException.class, () -> dynamicRuleService.saveDynamicRule(dynamicRuleDto));
    }

    @Test
    public void testSaveDynamicRule_Success() {
        when(ruleRepository.findRuleById(anyInt())).thenReturn(Optional.empty());

        when(dynamicRuleRepository.save(any(DynamicRule.class))).thenReturn(dynamicRule);

        dynamicRuleService.saveDynamicRule(dynamicRuleDto);

        verify(dynamicRuleRepository).save(any(DynamicRule.class));
    }

    @Test
    public void testSaveDynamicRule_NewRuleWithIdZero() {
        RuleArguments newArguments = mock(RuleArguments.class);
        when(newArguments.getId()).thenReturn(0);

        SimpleRuleDto newRuleDto = new SimpleRuleDto();
        newRuleDto.setArguments(Collections.singletonList("SOME_PRODUCT_TYPE"));
        newRuleDto.setQueryType(QueryType.USER_OF);
        newRuleDto.setNegate(false);

        dynamicRuleDto.setRules(new HashSet<>(dynamicRuleDto.getRules()));
        dynamicRuleDto.getRules().clear();
        dynamicRuleDto.getRules().add(newRuleDto);

        System.out.println("Mocked Rule ID: " + newArguments.getId());

        when(ruleRepository.findRuleById(0)).thenReturn(Optional.empty());
        when(ruleRepository.existsRuleByType(0, newArguments)).thenReturn(false);

        verify(ruleRepository).existsRuleByType(eq(0), eq(newArguments));

        dynamicRuleService.saveDynamicRule(dynamicRuleDto);

        verify(ruleRepository).existsRuleByType(0, newArguments);

        verify(dynamicRuleRepository).save(any(DynamicRule.class));
    }

    @Test
    public void testSaveDynamicRule_ArgumentExists_ThrowsIllegalStateException() {
        RuleArguments newArguments = mock(RuleArguments.class);
        when(newArguments.getId()).thenReturn(0);

        System.out.println("Mocked RuleArguments ID: " + newArguments.getId());

        SimpleRule newRule = TestUtils.createSimpleRule();
        newRule.setArguments(newArguments);
        newRule.setQueryType(QueryType.USER_OF);

        dynamicRuleDto.setRules(new HashSet<>(dynamicRuleDto.getRules()));
        dynamicRuleDto.getRules().clear();
        dynamicRuleDto.getRules().add(simpleRuleDto);

        when(ruleRepository.existsRuleByType(eq(0), eq(newArguments))).thenReturn(true);

        dynamicRuleService.saveDynamicRule(dynamicRuleDto);

        verify(ruleRepository).existsRuleByType(eq(0), eq(newArguments));

        IllegalStateExceptionWithDetails exception = assertThrows(IllegalStateExceptionWithDetails.class,
                () -> dynamicRuleService.saveDynamicRule(dynamicRuleDto));

        assertEquals("Rule: 0", exception.getMessage());
    }
}