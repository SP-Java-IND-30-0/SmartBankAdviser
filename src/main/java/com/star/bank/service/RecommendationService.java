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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import java.util.concurrent.CompletableFuture;


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

    @Async
    public CompletableFuture<PersonalRecommendationDto> sendRecommendation(String userId) {

        try {
            UUID.fromString(userId);
        } catch (IllegalArgumentException e) {
            throw new UserNotFoundException(userId);
        }

        if (!repository.isUserExist(userId)) {
            throw new UserNotFoundException(userId);
        }

        addDynamicRules();
        PersonalRecommendationDto dto = new PersonalRecommendationDto(userId);

        dto.setRecommendations(getRecommendations(userId));

        return CompletableFuture.completedFuture(dto);
    }

    private List<Product> getRecommendations(String userId) {
        List<Product> result = new ArrayList<>();
        addDynamicRules();
        for (Product pr : products) {
            if (repository.checkProductRules(userId, pr.getQuery())) {
                result.add(pr);
            }
        }
        return result;
    }



    public List<UUID> getAllUserIds() {
        return repository.getAllUserIds();
    }
      

    public CompletableFuture<PersonalRecommendationTgDto> sendRecommendationTg(String username) {

        List<UserDto> users = repository.getUser(username);
        if (users == null || users.size() != 1) {
            throw new UsernameNotFoundException(username);
        }

        PersonalRecommendationTgDto dto = PersonalRecommendationTgDto.builder()
                .firstName(users.get(0).getFirstName())
                .lastName(users.get(0).getLastName())
                .build();
        dto.setRecommendations(getRecommendations(users.get(0).getId()));

        return CompletableFuture.completedFuture(dto);
    }

    private void addDynamicRules() {
        List<DynamicRule> dynamicRuleList = dynamicRuleService.getDynamicRules()
                .stream()
                .map(dynamicRuleMapper::toEntity)
                .toList();
        products.addAll(dynamicRuleList);
    }
}
