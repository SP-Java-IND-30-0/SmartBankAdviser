package com.star.bank.model.dto;

import com.star.bank.model.product.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(exclude = "recommendations")
@ToString
@Builder
@Schema(description = "Список рекомендаций пользователя с указанием Id пользователя")
public class PersonalRecommendationTgDto {

    @Schema(description = "Имя пользователя")
    private String firstName;

    @Schema(description = "Фамилия пользователя")
    private String lastName;

    @Schema(description = "Список рекомендаций пользователя")
    private List<Product> recommendations;

}
