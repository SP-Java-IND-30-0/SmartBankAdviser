package com.star.bank.service;

import com.star.bank.PersonalRecommendationDto;
import com.star.bank.Product;
import com.star.bank.RecommendationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
        List<Product> recommendationProduct = new ArrayList<>();
        for (Product pr : products) {
            if (repository.checkProductRules(userId, pr.getQuery())) {
                recommendationProduct.add(pr);
            }
        }
        
        return new PersonalRecommendationDto(userId,recommendationProduct);
    }

}
