package com.star.bank.service;

import com.star.bank.event.SendRecommendationEvent;
import com.star.bank.model.dto.DynamicRuleDto;
import com.star.bank.model.dto.StatsDto;
import com.star.bank.model.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Сервис для сбора и предоставления статистики по динамическим правилам и продуктам.
 * Отслеживает количество рекомендаций для каждого правила и предоставляет сводную статистику.
 */
@Service
public class StatsService {

    private final Map<UUID, Integer> ruleCounters = new ConcurrentHashMap<>();
    private final DynamicRuleService dynamicRuleService;
    private final Set<Product> products;

    /**
     * Конструктор для инициализации сервиса.
     *
     * @param dynamicRuleService Сервис для работы с динамическими правилами.
     * @param products           Набор продуктов.
     */
    @Autowired
    public StatsService(DynamicRuleService dynamicRuleService, Set<Product> products) {
        this.dynamicRuleService = dynamicRuleService;
        this.products = products;
    }

    private Set<UUID> getAllRuleIds() {
        List<DynamicRuleDto> dynamicRules = dynamicRuleService.getDynamicRules();
        Set<UUID> ruleIds = dynamicRules.stream()
                .map(DynamicRuleDto::getProductId)
                .collect(Collectors.toSet());

        if (products != null) {
            products.stream()
                    .map(pr -> UUID.fromString(pr.getId()))
                    .forEach(ruleIds::add);
        }

        return ruleIds;
    }

    /**
     * Увеличивает счётчик рекомендаций для продукта при получении события отправки рекомендации.
     *
     * @param event Событие отправки рекомендации.
     */
    public void incrementProduct(SendRecommendationEvent event) {
        UUID ruleId = UUID.fromString(event.getProduct().getId());
        ruleCounters.merge(ruleId, 1, Integer::sum);
    }

    /**
     * Удаляет динамическое правило из списка счётчиков рекомендаций.
     *
     * @param ruleId Идентификатор динамического правила.
     */
    public void deleteDynamicRule(UUID ruleId) {
        ruleCounters.remove(ruleId);
    }

    public StatsDto getStats() {
        getAllRuleIds().forEach(ruleId -> ruleCounters.putIfAbsent(ruleId, 0));

        List<StatsDto.ProductStat> stats = ruleCounters.entrySet().stream()
                .map(entry -> new StatsDto.ProductStat(entry.getKey().toString(), entry.getValue()))
                .toList();

        return new StatsDto(stats);
    }
}