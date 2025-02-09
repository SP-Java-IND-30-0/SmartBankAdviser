package com.star.bank.dto;

import com.star.bank.service.Product;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class PersonalRecommendationDto {
    private String userId;
    private List<Product> recommendations;

    public PersonalRecommendationDto(String userId, List<Product> recommendations) {
        this.userId = userId;
        this.recommendations = recommendations;
    }
}
