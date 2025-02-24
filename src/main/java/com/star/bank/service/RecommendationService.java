package com.star.bank.service;

import com.star.bank.mapper.DynamicRuleMapper;
import com.star.bank.model.dto.PersonalRecommendationDto;
import com.star.bank.model.product.DynamicRule;
import com.star.bank.model.product.Product;
import com.star.bank.repositories.RecommendationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class RecommendationService {
    private final RecommendationRepository repository;
    private final DynamicRuleService dynamicRuleService;
    private final DynamicRuleMapper dynamicRuleMapper;
    private final Set<Product> products;


    public RecommendationService(RecommendationRepository repository, DynamicRuleService dynamicRuleService, DynamicRuleMapper dynamicRuleMapper, Set<Product> products) {
        this.repository = repository;
        this.dynamicRuleService = dynamicRuleService;
        this.dynamicRuleMapper = dynamicRuleMapper;
        this.products = products;
    }

    public PersonalRecommendationDto sendRecommendation(String userId) {
        addDynamicRules();
        PersonalRecommendationDto dto = new PersonalRecommendationDto(userId);

        for (Product pr : products) {
            if (repository.checkProductRules(userId, pr.getQuery())) {
                dto.addRecommendation(pr);
            }
        }
        return dto;
    }

    public List<UUID> getAllUserIds() {
        return repository.getAllUserIds();
    }

    private void addDynamicRules() {
        List<DynamicRule> dynamicRuleList = dynamicRuleService.getDynamicRules()
                .stream()
                .map(dynamicRuleMapper::toEntity)
                .toList();
        products.addAll(dynamicRuleList);
    }

}
