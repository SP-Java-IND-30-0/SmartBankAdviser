package com.star.bank.controller;

import com.star.bank.model.dto.PersonalRecommendationDto;
import com.star.bank.model.dto.PersonalRecommendationTgDto;
import com.star.bank.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recommendation")
@Tag(name = "Recommendation", description = "API получения рекомендаций для пользователя")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/{user_id}")
    @Operation(summary = "Получить рекомендации для пользователя по его ID")
    @ApiResponse(responseCode = "200", description = "Получен список подходящих рекомендаций для пользователя")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    public PersonalRecommendationDto getUserId(@PathVariable("user_id") String userId) {
        return recommendationService.sendRecommendation(userId);
    }

    @GetMapping("username/{username}")
    @Operation(summary = "Получить рекомендации для пользователя по значению 'username'")
    @ApiResponse(responseCode = "200", description = "Получен список подходящих рекомендаций для пользователя")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    public PersonalRecommendationTgDto getUsername(@PathVariable("username") String username) {
        return recommendationService.sendRecommendationTg(username);
    }

}
