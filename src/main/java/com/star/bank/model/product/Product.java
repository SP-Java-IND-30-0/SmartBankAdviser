package com.star.bank.model.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
/**
 * Интерфейс для всех продуктов.
 * Определяет методы для получения имени, идентификатора и текста продукта, а также запроса.
 */
@Schema(description = "Рекомендации")
public interface Product {

    /**
     * Возвращает запрос для продукта.
     *
     * @return Запрос в виде строки.
     */
    @JsonIgnore
    String getQuery();

    /**
     * Возвращает имя продукта.
     *
     * @return Имя продукта.
     */
    @Schema(description = "Имя продукта")
    @JsonProperty("name")
    String getName();

    /**
     * Возвращает идентификатор продукта.
     *
     * @return Идентификатор продукта.
     */
    @JsonProperty("id")
    @Schema(description = "Id продукта")
    String getId();

    /**
     * Возвращает описание продукта.
     *
     * @return описание продукта.
     */
    @JsonProperty("text")
    @Schema(description = "Описание продукта")
    String getText();

}
