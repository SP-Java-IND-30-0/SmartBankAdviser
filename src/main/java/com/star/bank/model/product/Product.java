package com.star.bank.model.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Рекомендации")
public interface Product {

    @JsonIgnore
    String getQuery();

    @Schema(description = "Имя продукта")
    String getName();

    @Schema(description = "Id продукта")
    String getId();

    @Schema(description = "Описание продукта")
    String getText();

}
