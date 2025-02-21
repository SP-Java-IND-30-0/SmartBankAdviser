package com.star.bank.component;

import com.star.bank.event.DeleteDynamicRuleEvent;
import com.star.bank.event.SendRecommendationEvent;
import com.star.bank.service.StatsService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class RecommendationEventListener {

    private final StatsService statsService;

    public RecommendationEventListener(StatsService statsService) {
        this.statsService = statsService;
    }

    @EventListener
    public void handleDeleteDynamicRuleEvent(DeleteDynamicRuleEvent event) {
        statsService.deleteDynamicRule(event.getRuleId());
    }

    @EventListener
    public void handleRecommendationEvent(SendRecommendationEvent event) {
        statsService.incrementProduct(event);
    }
}