package com.star.bank.service;

import com.star.bank.model.dto.PersonalRecommendationDto;
import com.star.bank.model.product.DynamicRule;
import com.star.bank.model.product.Product;
import com.star.bank.repositories.DynamicRuleRepository;
import com.star.bank.repositories.RecommendationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class RecommendationService {
    private final RecommendationRepository repository;
    private final DynamicRuleRepository dynamicRuleRepository;
    private final Set<Product> products;


    public RecommendationService(RecommendationRepository repository, DynamicRuleRepository dynamicRuleRepository, Set<Product> products) {
        this.repository = repository;
        this.dynamicRuleRepository = dynamicRuleRepository;
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

    public PersonalRecommendationDto getRecommendation(String userId) {
        PersonalRecommendationDto dynamicDto = new PersonalRecommendationDto(userId);
        List<DynamicRule> dynamicRuleList = dynamicRuleRepository.findAll();
        for (Product product : products) {
            for (DynamicRule dynamicRule : dynamicRuleList) {
                if (dynamicRule.getQuery().equals(product.getQuery())) {
                    repository.checkProductRules(userId, dynamicRule.getQuery());
                    dynamicDto.addRecommendation(product);
                }
            }
        }
        return dynamicDto;
    }
}
