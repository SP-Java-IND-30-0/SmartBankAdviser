package com.star.bank.controller;

import com.star.bank.model.dto.PersonalRecommendationDto;
import com.star.bank.model.dto.PersonalRecommendationTgDto;
import com.star.bank.service.RecommendationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recommendation")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/{user_id}")
    public PersonalRecommendationDto getUserId(@PathVariable("user_id") String userId) {
        return recommendationService.sendRecommendation(userId);
    }

    @GetMapping("username/{username}")
    public PersonalRecommendationTgDto getUsername(@PathVariable("username") String username) {
        return recommendationService.sendRecommendationTg(username);
    }

}
