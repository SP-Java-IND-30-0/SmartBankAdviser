package com.star.bank.model.dto;

import com.star.bank.model.product.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO для персонализированных рекомендаций пользователя.
 * Содержит идентификатор пользователя и список рекомендаций.
 */
@Getter
@Setter
@EqualsAndHashCode(exclude = "recommendations")
@ToString
@NoArgsConstructor()
@Schema(description = "Список рекомендаций пользователя с указанием Id пользователя")
public class PersonalRecommendationDto {

    /**
     * Идентификатор пользователя.
     */
    @Schema(description = "Id пользователя в формате UUID", example = "cd515076-5d8a-44be-930e-8d4fcb79f42d")
    private String userId;

    /**
     * Список рекомендаций пользователя.
     */
    @Schema(description = "Список рекомендаций пользователя")
    private List<Product> recommendations = new ArrayList<>();

    /**
     * Конструктор для создания DTO с указанным идентификатором пользователя.
     *
     * @param userId Идентификатор пользователя.
     */
    public PersonalRecommendationDto(String userId) {
        this.userId = userId;

    }

}
