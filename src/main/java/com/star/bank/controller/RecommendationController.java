package com.star.bank.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.star.bank.model.PersonalRecommendationDto;
import com.star.bank.service.RecommendationService;

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

}
