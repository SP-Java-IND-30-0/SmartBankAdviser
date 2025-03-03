package com.star.bank.listener;

import com.star.bank.event.DeleteDynamicRuleEvent;
import com.star.bank.event.SendRecommendationEvent;
import com.star.bank.service.StatsService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Слушатель событий, связанных с рекомендациями и динамическими правилами.
 * Обрабатывает события удаления динамических правил и отправки рекомендаций.
 */
@Component
public class RecommendationEventListener {

    private final StatsService statsService;

    /**
     * Конструктор для инициализации слушателя.
     *
     * @param statsService Сервис для работы со статистикой.
     */
    public RecommendationEventListener(StatsService statsService) {
        this.statsService = statsService;
    }

    /**
     * Обрабатывает событие удаления динамического правила.
     * Удаляет счётчик рекомендаций для удалённого правила.
     *
     * @param event Событие удаления динамического правила.
     */
    @EventListener
    public void handleDeleteDynamicRuleEvent(DeleteDynamicRuleEvent event) {
        statsService.deleteDynamicRule(event.getRuleId());
    }

    /**
     * Обрабатывает событие отправки рекомендации.
     * Увеличивает счётчик рекомендаций для продукта.
     *
     * @param event Событие отправки рекомендации.
     */
    @EventListener
    public void handleRecommendationEvent(SendRecommendationEvent event) {
        statsService.incrementProduct(event);
    }
}