package com.star.bank.model;

import com.star.bank.mapper.DynamicRuleMapper;
import com.star.bank.model.dto.PersonalRecommendationDto;
import com.star.bank.model.product.Product;
import com.star.bank.model.product.TopSavingProduct;
import com.star.bank.model.rule.HasDebitDepositGreaterThanWithdraw;
import com.star.bank.model.rule.HasDebitWithdrawGreater100000;
import com.star.bank.model.rule.HasNotCreditTypeProduct;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TopSavingProductTest {

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
    void test_topSavingProduct_successWithRecommendations() {
        String userId = UUID.randomUUID().toString();

        HasNotCreditTypeProduct rule1 = new HasNotCreditTypeProduct();
        HasDebitDepositGreaterThanWithdraw rule2 = new HasDebitDepositGreaterThanWithdraw();
        HasDebitWithdrawGreater100000 rule3 = new HasDebitWithdrawGreater100000();

        TopSavingProduct firstProduct = new TopSavingProduct(rule1,rule2,rule3);
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
