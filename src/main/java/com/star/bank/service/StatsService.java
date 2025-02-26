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

@Service
public class StatsService {

    private final Map<UUID, Integer> ruleCounters = new ConcurrentHashMap<>();
    private final DynamicRuleService dynamicRuleService;
    private final Set<Product> products;

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

    public void incrementProduct(SendRecommendationEvent event) {
        UUID ruleId = UUID.fromString(event.getProduct().getId());
        ruleCounters.merge(ruleId, 1, Integer::sum);
    }

    public void deleteDynamicRule(UUID ruleId) {
        ruleCounters.remove(ruleId);
    }

    public StatsDto getStats() {
        getAllRuleIds().forEach(ruleId -> ruleCounters.putIfAbsent(ruleId, 0));

        List<StatsDto.ProductStat> stats = ruleCounters.entrySet().stream()
                .map(entry -> new StatsDto.ProductStat(entry.getKey().toString(), entry.getValue()))
                .collect(Collectors.toList());

        return new StatsDto(stats);
    }
}