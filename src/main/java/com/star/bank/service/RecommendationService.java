package com.star.bank.service;

import com.star.bank.exception.UserNotFoundException;
import com.star.bank.exception.UsernameNotFoundException;
import com.star.bank.mapper.DynamicRuleMapper;
import com.star.bank.model.dto.PersonalRecommendationDto;
import com.star.bank.model.dto.PersonalRecommendationTgDto;
import com.star.bank.model.dto.UserDto;
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

        if (!repository.isUserExist(userId)) {
            throw new UserNotFoundException(userId);
        }

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
      
    public PersonalRecommendationTgDto sendRecommendationTg(String username) {
        List<UserDto> users = repository.getUser(username);
        if (users == null || users.size() != 1) {
            throw new UsernameNotFoundException(username);
        }
        PersonalRecommendationDto dto = sendRecommendation(users.get(0).getId());
        return PersonalRecommendationTgDto.builder()
                .firstName(users.get(0).getFirstName())
                .lastName(users.get(0).getLastName())
                .recommendations(dto.getRecommendations())
                .build();
    }

    private void addDynamicRules() {
        List<DynamicRule> dynamicRuleList = dynamicRuleService.getDynamicRules()
                .stream()
                .map(dynamicRuleMapper::toEntity)
                .toList();
        products.addAll(dynamicRuleList);
    }
}
