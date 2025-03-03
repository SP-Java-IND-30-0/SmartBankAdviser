package com.star.bank.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

/**
 * DTO для динамического правила.
 * Содержит идентификатор, имя и текст продукта, а также набор простых правил.
 */
@Data
@Schema(description = "Динамическое правило")
public class DynamicRuleDto {

    /**
     * Идентификатор продукта.
     */
    @JsonProperty("product_id")
    @Schema(description = "ID продукта")
    private UUID productId;

    /**
     * Имя продукта.
     */
    @JsonProperty("product_name")
    @Schema(description = "Имя продукта")
    private String productName;
    /**
     * Текст рекомендации.
     */
    @JsonProperty("product_text")
    @Schema(description = "Текст рекомендации")
    private String productText;
    /**
     * Список условий отбора для рекомендации.
     */
    @JsonProperty("rule")
    @Schema(description = "Список условий отбора для рекомендации")
    private Set<SimpleRuleDto> rules;
}
