package com.star.bank.model.dto;

import com.star.bank.model.product.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO для персонализированных рекомендаций пользователя для отправки в Telegram.
 */
@Builder
@Getter
@Setter
@EqualsAndHashCode(exclude = "recommendations")
@ToString
@Schema(description = "Список рекомендаций пользователя с указанием Id пользователя")
public class PersonalRecommendationTgDto {

    /**
     * Идентификатор пользователя.
     */
    @Schema(description = "Id пользователя в формате UUID", example = "cd515076-5d8a-44be-930e-8d4fcb79f42d")
    private String userId;

    /**
     * Имя пользователя.
     */
    @Schema(description = "Имя пользователя")
    private String firstName;

    /**
     * Фамилия пользователя.
     */
    @Schema(description = "Фамилия пользователя")
    private String lastName;

    /**
     * Список рекомендаций пользователя.
     */
    @Schema(description = "Список рекомендаций пользователя")
    @Builder.Default
    private List<Product> recommendations = new ArrayList<>();

}
