package com.star.bank.service;

import com.star.bank.event.SendRecommendationEvent;
import com.star.bank.model.dto.StatsDto;
import com.star.bank.model.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatsServiceTest {

    private StatsService statsService;

    @BeforeEach
    void setUp() {
        statsService = new StatsService();
    }

    @Test
    void testIncrementProduct() {
        UUID ruleId = UUID.randomUUID();
        Product mockProduct = mock(Product.class);
        when(mockProduct.getId()).thenReturn(ruleId.toString());

        SendRecommendationEvent event = new SendRecommendationEvent(this, mockProduct);
        statsService.incrementProduct(event);

        StatsDto stats = statsService.getStats();
        assertThat(stats.getStats()).hasSize(1);
        assertThat(stats.getStats().get(0).getRuleId()).isEqualTo(ruleId.toString());
        assertThat(stats.getStats().get(0).getCount()).isEqualTo(1);
    }

    @Test
    void testDeleteDynamicRule() {
        UUID ruleId = UUID.randomUUID();
        statsService.deleteDynamicRule(ruleId);

        StatsDto stats = statsService.getStats();
        assertThat(stats.getStats()).doesNotContain(new StatsDto.ProductStat(ruleId.toString(), 0));
    }
}