package com.star.bank.controller;

import com.star.bank.config.AppConfig;
import com.star.bank.event.RequestClearCacheEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Контроллер для выполнения управленческих задач.
 * Предоставляет API для очистки кэшей и получения информации о приложении.
 */
@RestController
@RequestMapping("/management")
public class ManagementController {

    private final ApplicationEventPublisher eventPublisher;

    /**
     * Конструктор для инициализации контроллера.
     *
     * @param eventPublisher Публикатор событий приложения.
     */
    public ManagementController(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    /**
     * Очищает кэши приложения.
     * Выполняется путем публикации события очистки кэша.
     */
    @PostMapping("/clear-caches")
    public void clearCaches() {
        eventPublisher.publishEvent(new RequestClearCacheEvent(this));
    }

    /**
     * Возвращает информацию о приложении.
     *
     * @return Карта с именем и версией приложения.
     */
    @GetMapping("/info")
    public Map<String, String> getInfo() {
        return Map.of("name", AppConfig.getName(), "version", AppConfig.getVersion());
    }
}
