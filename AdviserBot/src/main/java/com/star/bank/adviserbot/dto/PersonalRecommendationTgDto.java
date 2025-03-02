package com.star.bank.adviserbot.dto;

import java.util.List;
import java.util.UUID;

public record PersonalRecommendationTgDto(String firstName, String lastName, List<Product> recommendations) {

    public record Product(UUID id, String name, String text) {
    }
}
