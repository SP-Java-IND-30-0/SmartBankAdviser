package com.star.bank.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
public class DeleteDynamicRuleEvent extends ApplicationEvent {
    private final UUID ruleId;

    public DeleteDynamicRuleEvent(Object source, UUID ruleId) {
        super(source);
        this.ruleId = ruleId;
    }
}