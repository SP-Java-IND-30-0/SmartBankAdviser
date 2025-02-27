package com.star.bank.model;


import com.star.bank.mapper.DynamicRuleMapper;
import com.star.bank.model.dto.PersonalRecommendationDto;
import com.star.bank.model.enums.BankProductType;
import com.star.bank.model.enums.CompareType;
import com.star.bank.model.enums.OperationType;
import com.star.bank.model.product.Product;
import com.star.bank.model.product.SimpleCreditProduct;
import com.star.bank.model.rule.HasDebitOrSavingDepositGreaterThan50000;
import com.star.bank.model.rule.RuleActiveUserOf;
import com.star.bank.model.rule.RuleCompareSum;
import com.star.bank.repositories.RecommendationRepository;
import com.star.bank.service.DynamicRuleService;
import com.star.bank.service.RecommendationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SimpleCreditProductTest {

    @Mock
    private RecommendationRepository recommendationRepository;
    @Mock
    private DynamicRuleService dynamicRuleService;
    @Mock
    private DynamicRuleMapper dynamicRuleMapper;
    @Mock
    private ApplicationEventPublisher eventPublisher;

    private final Set<Product> products = new HashSet<>();

    private RecommendationService service;


    @BeforeEach
    void info() {
        service = new RecommendationService(
                recommendationRepository, dynamicRuleService, dynamicRuleMapper, eventPublisher, products
        );
    }

    @Test
    void test_simpleCreditProduct_successWithRecommendations() {
        String userId = UUID.randomUUID().toString();

        RuleActiveUserOf rule1 = new RuleActiveUserOf();
        rule1.setProductType(BankProductType.DEBIT);
        HasDebitOrSavingDepositGreaterThan50000 rule2 = new HasDebitOrSavingDepositGreaterThan50000();
        RuleCompareSum rule3 = new RuleCompareSum();
        rule3.setProductType(BankProductType.DEBIT);
        rule3.setOperationType(OperationType.WITHDRAW);
        rule3.setCompareType(CompareType.EQ);
        rule3.setAmount(1);

        SimpleCreditProduct firstProduct = new SimpleCreditProduct(rule1,rule2,rule3);
        service.getProducts().add(firstProduct);

        when(recommendationRepository.isUserExist(userId)).thenReturn(true);

        when(recommendationRepository.checkProductRules(any(), any())).thenReturn(true);
        doNothing().when(eventPublisher).publishEvent(any());

        CompletableFuture<PersonalRecommendationDto> futureRecommendationDto = service.sendRecommendation(userId);

        PersonalRecommendationDto recommendationDto = futureRecommendationDto.join();

        assertEquals(1,service.getProducts().size());

        assertEquals(1, recommendationDto.getRecommendations().size());
        assertEquals(userId, recommendationDto.getUserId());
        assertTrue(recommendationDto.getRecommendations().contains(firstProduct));

    }

}
