package com.star.bank.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.star.bank.model.enums.QueryType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * ДТО для простого правила.
 */
@Data
@Schema(description = "Условия отбора")
public class SimpleRuleDto {

    /**
     * Тип запроса.
     */
    @JsonProperty("query")
    @Schema(description = "Тип запроса")
    private QueryType queryType;

    /**
     * Флаг отрицания условия.
     */
    @Schema(description = "Флаг отрицания условия")
    @JsonProperty("negate")
    private boolean negate;

    /**
     * Аргументы запроса.
     */
    @Schema(description = "Параметры запроса")
    @JsonProperty("arguments")
    private List<String> arguments;
}
