package com.star.bank.model.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Рекомендации")
public interface Product {

    @JsonIgnore
    String getQuery();

    @Schema(description = "Имя продукта")
    @JsonProperty("name")
    String getName();

    @JsonProperty("id")
    @Schema(description = "Id продукта")
    String getId();

    @JsonProperty("text")
    @Schema(description = "Описание продукта")
    String getText();

}
