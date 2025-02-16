package com.star.bank.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class DynamicRuleDto {

    @JsonProperty("product_id")
    private UUID productId;
    @JsonProperty("product_name")
    private String productName;
    @JsonProperty("product_text")
    private String productText;
    @JsonProperty("rule")
    private Set<SimpleRuleDto> rules;
}
