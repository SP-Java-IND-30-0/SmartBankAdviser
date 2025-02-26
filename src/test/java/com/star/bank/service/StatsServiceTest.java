package com.star.bank.service;

import com.star.bank.event.SendRecommendationEvent;
import com.star.bank.model.dto.StatsDto;
import com.star.bank.model.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatsServiceTest {

    @Mock
    private DynamicRuleService dynamicRuleService;

    @Mock
    private Set<Product> products;

    private StatsService statsService;

    @BeforeEach
    void setUp() {
        statsService = new StatsService(dynamicRuleService, products);
    }

    @Test
    void testIncrementProduct() {
        UUID ruleId = UUID.randomUUID();
        Product mockProduct = mock(Product.class);
        when(mockProduct.getId()).thenReturn(ruleId.toString());

        SendRecommendationEvent event = new SendRecommendationEvent(this, mockProduct);

        when(dynamicRuleService.getDynamicRules()).thenReturn(Collections.emptyList());
        when(products.stream()).thenAnswer(invocation -> Stream.of(mockProduct));

        StatsDto statsBefore = statsService.getStats();
        assertThat(statsBefore.getStats()).anyMatch(stat ->
                stat.getRuleId().equals(ruleId.toString()) && stat.getCount() == 0
        );

        statsService.incrementProduct(event);

        StatsDto statsAfter = statsService.getStats();
        assertThat(statsAfter.getStats()).anyMatch(stat ->
                stat.getRuleId().equals(ruleId.toString()) && stat.getCount() == 1
        );
    }

    @Test
    void testIncrementProductNoEventBefore() {
        UUID ruleId = UUID.randomUUID();
        Product mockProduct = mock(Product.class);
        when(mockProduct.getId()).thenReturn(ruleId.toString());

        when(dynamicRuleService.getDynamicRules()).thenReturn(Collections.emptyList());
        when(products.stream()).thenAnswer(invocation -> Stream.of(mockProduct));

        StatsDto statsBefore = statsService.getStats();
        assertThat(statsBefore.getStats()).anyMatch(stat ->
                stat.getRuleId().equals(ruleId.toString()) && stat.getCount() == 0
        );

        SendRecommendationEvent event = new SendRecommendationEvent(this, mockProduct);
        statsService.incrementProduct(event);

        StatsDto statsAfter = statsService.getStats();
        assertThat(statsAfter.getStats()).anyMatch(stat ->
                stat.getRuleId().equals(ruleId.toString()) && stat.getCount() == 1
        );
    }

    @Test
    void testStatsIncludeZeroCountForInactiveRules() {
        UUID dynamicRuleId = UUID.randomUUID();
        UUID staticRuleId = UUID.randomUUID();

        when(dynamicRuleService.getDynamicRules()).thenReturn(Collections.emptyList());

        Product staticProduct = mock(Product.class);
        when(staticProduct.getId()).thenReturn(staticRuleId.toString());

        when(products.stream()).thenAnswer(invocation -> Stream.of(staticProduct));

        StatsDto statsBefore = statsService.getStats();
        assertThat(statsBefore.getStats()).anyMatch(stat ->
                stat.getRuleId().equals(staticRuleId.toString()) && stat.getCount() == 0
        );

        Product dynamicProduct = mock(Product.class);
        when(dynamicProduct.getId()).thenReturn(dynamicRuleId.toString());
        SendRecommendationEvent event = new SendRecommendationEvent(this, dynamicProduct);
        statsService.incrementProduct(event);

        StatsDto statsAfterIncrement = statsService.getStats();
        assertThat(statsAfterIncrement.getStats()).hasSize(2);
        assertThat(statsAfterIncrement.getStats()).anyMatch(stat ->
                stat.getRuleId().equals(dynamicRuleId.toString()) && stat.getCount() == 1
        );
        assertThat(statsAfterIncrement.getStats()).anyMatch(stat ->
                stat.getRuleId().equals(staticRuleId.toString()) && stat.getCount() == 0
        );
    }
}