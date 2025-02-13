package com.star.bank.service;


import com.star.bank.model.dto.PersonalRecommendationDto;
import com.star.bank.model.product.Product;
import com.star.bank.repositories.RecommendationRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RecommendationService {
    private final RecommendationRepository repository;
    private final Set<Product> products;

    public RecommendationService(RecommendationRepository repository, Set<Product> products) {
        this.repository = repository;
        this.products = products;
    }

    public PersonalRecommendationDto sendRecommendation(String userId) {
        PersonalRecommendationDto dto = new PersonalRecommendationDto(userId);
        for (Product pr : products) {
            if (repository.checkProductRules(userId, pr.getQuery())) {
                dto.addRecommendation(pr);
            }
        }
        return dto;
    }

}
