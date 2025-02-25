package com.star.bank.service;

import com.star.bank.event.SendRecommendationEvent;
import com.star.bank.model.dto.StatsDto;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class StatsService {

    private final Map<UUID, Integer> ruleCounters = new ConcurrentHashMap<>();

    public void incrementProduct(SendRecommendationEvent event) {
        UUID ruleId = UUID.fromString(event.getProduct().getId());
        ruleCounters.merge(ruleId, 1, Integer::sum);
    }

    public void deleteDynamicRule(UUID ruleId) {
        ruleCounters.remove(ruleId);
    }

    public StatsDto getStats() {
        List<StatsDto.ProductStat> stats = ruleCounters.entrySet().stream()
                .map(entry -> new StatsDto.ProductStat(entry.getKey().toString(), entry.getValue()))
                .toList();
        return new StatsDto(stats.isEmpty() ? Collections.emptyList() : stats);
    }
}