package com.star.bank.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.star.bank.model.product.DynamicRule;
import com.star.bank.model.product.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(exclude = "recommendations")
@ToString
@NoArgsConstructor()
@Schema(description = "Список рекомендаций пользователя с указанием Id пользователя")
public class PersonalRecommendationDto {

    @Schema(description = "Id пользователя в формате UUID", example = "cd515076-5d8a-44be-930e-8d4fcb79f42d")
    private String userId;

    @JsonDeserialize(contentAs = DynamicRule.class)
    @Schema(description = "Список рекомендаций пользователя")
    private List<Product> recommendations = new ArrayList<>();

    public PersonalRecommendationDto(String userId) {
        this.userId = userId;

    }

}
