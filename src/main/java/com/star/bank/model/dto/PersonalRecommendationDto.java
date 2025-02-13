package com.star.bank.model.dto;

import com.star.bank.model.product.Product;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(exclude = "recommendations")
@ToString
public class PersonalRecommendationDto {
    private final String userId;
    private final List<Product> recommendations = new ArrayList<>();

    public PersonalRecommendationDto(String userId) {
        this.userId = userId;

    }

    public void addRecommendation(Product product) {
        this.recommendations.add(product);
    }

}
