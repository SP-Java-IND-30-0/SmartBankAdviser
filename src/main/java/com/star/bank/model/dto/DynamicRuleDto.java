package com.star.bank.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
@Schema(description = "Динамическое правило")
public class DynamicRuleDto {

    @JsonProperty("product_id")
    @Schema(description = "ID продукта")
    private UUID productId;
    @NotBlank(message = "Имя продукта не может быть пустым")
    @JsonProperty("product_name")
    @Schema(description = "Имя продукта")
    private String productName;
    @JsonProperty("product_text")
    @Schema(description = "Текст рекомендации")
    private String productText;
    @JsonProperty("rule")
    @Schema(description = "Список условий отбора для рекомендации")
    private Set<SimpleRuleDto> rules;
}
