package com.star.bank.model.dto;

import com.star.bank.model.product.Product;
import lombok.*;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(exclude = "recommendations")
@ToString
@Builder
public class PersonalRecommendationTgDto {
    private String firstName;
    private String lastName;
    private List<Product> recommendations;

}
