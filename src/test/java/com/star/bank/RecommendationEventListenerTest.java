package com.star.bank;

import com.star.bank.component.RecommendationEventListener;
import com.star.bank.event.DeleteDynamicRuleEvent;
import com.star.bank.event.SendRecommendationEvent;
import com.star.bank.model.product.Product;
import com.star.bank.service.StatsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecommendationEventListenerTest {

    @Mock
    private StatsService statsService;

    private RecommendationEventListener listener;

    @BeforeEach
    void setUp() {
        listener = new RecommendationEventListener(statsService);
    }

    @Test
    void testHandleRecommendationEvent() {
        Product mockProduct = mock(Product.class);
        SendRecommendationEvent event = new SendRecommendationEvent(this, mockProduct);

        listener.handleRecommendationEvent(event);

        verify(statsService, times(1)).incrementProduct(event);
    }

    @Test
    void testHandleDeleteDynamicRuleEvent() {
        UUID ruleId = UUID.randomUUID();
        DeleteDynamicRuleEvent event = new DeleteDynamicRuleEvent(this, ruleId);

        listener.handleDeleteDynamicRuleEvent(event);

        verify(statsService, times(1)).deleteDynamicRule(ruleId);
    }
}