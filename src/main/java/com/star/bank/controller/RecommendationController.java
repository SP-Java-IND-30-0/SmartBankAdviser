package com.star.bank.controller;

import com.star.bank.model.dto.PersonalRecommendationDto;
import com.star.bank.model.dto.PersonalRecommendationTgDto;
import com.star.bank.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

/**
 * Контроллер для получения персонализированных рекомендаций для пользователей.
 * Предоставляет API для получения рекомендаций по идентификатору пользователя и по имени пользователя.
 */
@RestController
@RequestMapping("/recommendation")
@Tag(name = "Recommendation", description = "API получения рекомендаций для пользователя")
public class RecommendationController {

    private final RecommendationService recommendationService;

    /**
     * Конструктор для инициализации контроллера.
     *
     * @param recommendationService Сервис для работы с рекомендациями.
     */
    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    /**
     * Возвращает персонализированные рекомендации для пользователя по его идентификатору.
     *
     * @param userId Идентификатор пользователя.
     * @return CompletableFuture с ResponseEntity, содержащим рекомендации для пользователя.
     */
    @GetMapping("/{user_id}")
    @Operation(summary = "Получить рекомендации для пользователя по его ID")
    @ApiResponse(responseCode = "200", description = "Получен список подходящих рекомендаций для пользователя")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    public CompletableFuture<ResponseEntity<PersonalRecommendationDto>> getUserId(@PathVariable("user_id") String userId) {
        return recommendationService.sendRecommendation(userId)
                .thenApply(ResponseEntity::ok)
                .exceptionally(e -> {
                            throw new RuntimeException(e.getCause());
                        }
                );
    }

    /**
     * Возвращает персонализированные рекомендации для пользователя по его имени в Telegram.
     *
     * @param username Имя пользователя в Telegram.
     * @return CompletableFuture с ResponseEntity, содержащим рекомендации для пользователя.
     */
    @GetMapping("/username/{username}")
    @Operation(summary = "Получить рекомендации для пользователя по значению 'username'")
    @ApiResponse(responseCode = "200", description = "Получен список подходящих рекомендаций для пользователя")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    public CompletableFuture<ResponseEntity<PersonalRecommendationTgDto>> getUsername(@PathVariable("username") String username) {
        return recommendationService.sendRecommendationTg(username)
                .thenApply(ResponseEntity::ok)
                .exceptionally(e -> {
                            throw new RuntimeException(e.getCause());
                        }
                );
    }

}
