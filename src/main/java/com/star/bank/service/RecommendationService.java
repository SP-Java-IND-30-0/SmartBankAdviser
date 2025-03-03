package com.star.bank.service;

import com.star.bank.event.SendRecommendationEvent;
import com.star.bank.exception.UserNotFoundException;
import com.star.bank.exception.UsernameNotFoundException;
import com.star.bank.mapper.DynamicRuleMapper;
import com.star.bank.model.dto.PersonalRecommendationDto;
import com.star.bank.model.dto.PersonalRecommendationTgDto;
import com.star.bank.model.dto.UserDto;
import com.star.bank.model.product.DynamicRule;
import com.star.bank.model.product.Product;
import com.star.bank.repositories.RecommendationRepository;
import lombok.Getter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Сервис для формирования персонализированных рекомендаций для пользователей.
 * Предоставляет методы для отправки рекомендаций по идентификатору пользователя и имени пользователя в Telegram.
 */
@Service
public class RecommendationService {
    private final RecommendationRepository repository;
    private final DynamicRuleService dynamicRuleService;
    private final DynamicRuleMapper dynamicRuleMapper;
    private final ApplicationEventPublisher eventPublisher;
    @Getter
    private final Set<Product> products;

    /**
     * Конструктор для инициализации сервиса.
     *
     * @param repository         Репозиторий для работы с рекомендациями.
     * @param dynamicRuleService Сервис для работы с динамическими правилами.
     * @param dynamicRuleMapper  Маппер для преобразования динамических правил.
     * @param eventPublisher     Публикатор событий приложения.
     * @param products           Набор продуктов для рекомендаций.
     */
    public RecommendationService(RecommendationRepository repository, DynamicRuleService dynamicRuleService, DynamicRuleMapper dynamicRuleMapper, ApplicationEventPublisher eventPublisher, Set<Product> products) {
        this.repository = repository;
        this.dynamicRuleService = dynamicRuleService;
        this.dynamicRuleMapper = dynamicRuleMapper;
        this.eventPublisher = eventPublisher;
        this.products = products;
    }

    /**
     * Асинхронно формирует и отправляет персонализированные рекомендации для пользователя по его идентификатору.
     *
     * @param userId Идентификатор пользователя.
     * @return CompletableFuture с DTO персонализированных рекомендаций.
     * @throws UserNotFoundException Если пользователь не найден.
     */
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
        PersonalRecommendationDto dto = new PersonalRecommendationDto(userId);

        dto.setRecommendations(getRecommendations(userId));

        return CompletableFuture.completedFuture(dto);
    }

    /**
     * Возвращает список всех идентификаторов пользователей.
     *
     * @return Список идентификаторов пользователей.
     */
    public List<UUID> getAllUserIds() {
        return repository.getAllUserIds();
    }


    /**
     * Асинхронно формирует и отправляет персонализированные рекомендации для пользователя по его имени в Telegram.
     *
     * @param username Имя пользователя в Telegram.
     * @return CompletableFuture с DTO персонализированных рекомендаций для Telegram.
     * @throws UsernameNotFoundException Если пользователь не найден по имени.
     */
    @Async
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

    private List<Product> getRecommendations(String userId) {
        List<Product> result = new ArrayList<>();
        addDynamicRules();
        for (Product pr : products) {
            if (repository.checkProductRules(userId, pr.getQuery())) {
                result.add(pr);
                eventPublisher.publishEvent(new SendRecommendationEvent(this, pr));
            }
        }
        return result;
    }

    private void addDynamicRules() {
        List<DynamicRule> dynamicRuleList = dynamicRuleService.getDynamicRules()
                .stream()
                .map(dynamicRuleMapper::toEntity)
                .toList();
        products.addAll(dynamicRuleList);
    }
}