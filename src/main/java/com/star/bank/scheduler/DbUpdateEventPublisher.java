package com.star.bank.scheduler;

import com.star.bank.event.DbUpdateEvent;
import com.star.bank.service.RecommendationService;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class DbUpdateEventPublisher {

    private final ApplicationEventPublisher eventPublisher;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final Random random = new Random();

    private final RecommendationService recommendationService;

    @Getter
    private List<UUID> userIds;

    public DbUpdateEventPublisher(ApplicationEventPublisher eventPublisher, RecommendationService recommendationService) {
        this.eventPublisher = eventPublisher;
        this.recommendationService = recommendationService;
    }

    @PostConstruct
    public void init() {
        userIds = recommendationService.getAllUserIds();
        schedulerNextEvent();
    }

    private void schedulerNextEvent() {
        int delay = 1 + random.nextInt(5);
        scheduler.schedule(this::publishEvent, delay, TimeUnit.SECONDS);
    }

    private void publishEvent() {
        UUID uuid = userIds.get(random.nextInt(userIds.size()));
        DbUpdateEvent event = new DbUpdateEvent(this, uuid);
        eventPublisher.publishEvent(event);
        schedulerNextEvent();
    }

}
