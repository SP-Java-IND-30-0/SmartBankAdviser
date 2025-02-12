package com.star.bank.dto;

import com.star.bank.service.Product;
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
    private String userId;
    private final List<Product> recommendations = new ArrayList<>();

    public PersonalRecommendationDto(String userId) {
        this.userId = userId;

    }
    public void addRecommendation(Product product) {
        this.recommendations.add(product);
    }

}
