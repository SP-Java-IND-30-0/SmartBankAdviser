package com.star.bank.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
public class DbUpdateEvent extends ApplicationEvent {

    private final UUID userId;

    public DbUpdateEvent(Object source, UUID userId) {
        super(source);
        this.userId = userId;
    }
}
