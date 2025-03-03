package com.star.bank.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * DTO для статистики.
 * Содержит список статистических данных по продуктам.
 */
@Data
@AllArgsConstructor
public class StatsDto {

    /**
     * Список статистических данных по продуктам.
     */
    @JsonProperty("stats")
    private List<ProductStat> stats;

    /**
     * DTO для статистики по продукту.
     * Содержит идентификатор правила и количество рекомендаций для данного правила.
     */
    @Data
    @AllArgsConstructor
    public static class ProductStat {

        /**
         * Идентификатор правила.
         */
        @JsonProperty("rule_id")
        private String ruleId;
        /**
         * Количество рекомендаций для данного правила.
         */
        @JsonProperty("count")
        private Integer count;
    }
}