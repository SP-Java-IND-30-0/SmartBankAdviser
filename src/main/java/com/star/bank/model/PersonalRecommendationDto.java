package com.star.bank.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PersonalRecommendationDto {

    String userId;
    List<Product> recommendations;
}
