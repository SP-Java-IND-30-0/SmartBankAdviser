package com.star.bank.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class StatsDto {
    @JsonProperty("stats")
    private List<ProductStat> stats;

    @Data
    @AllArgsConstructor
    public static class ProductStat {
        @JsonProperty("rule_id")
        private String ruleId;
        @JsonProperty("count")
        private Integer count;
    }
}