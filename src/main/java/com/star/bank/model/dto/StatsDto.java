package com.star.bank.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class StatsDto {
    private List<ProductStat> stats;

    @Data
    @AllArgsConstructor
    public static class ProductStat {
        private String ruleId;
        private Integer count;
    }
}