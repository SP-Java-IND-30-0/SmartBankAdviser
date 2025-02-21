package com.star.bank.controller;

import com.star.bank.config.AppConfig;
import com.star.bank.event.RequestClearCacheEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/management")
public class ManagementController {

    private final ApplicationEventPublisher eventPublisher;

    public ManagementController(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @PostMapping("/clear-caches")
    public void clearCaches() {
        eventPublisher.publishEvent(new RequestClearCacheEvent(this));
    }

    @GetMapping("/info")
    public Map<String, String> getInfo() {
        return Map.of("name", AppConfig.getName(), "version", AppConfig.getVersion());
    }
}
