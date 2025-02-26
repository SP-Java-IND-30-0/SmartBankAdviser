package com.star.bank.event;

import org.springframework.context.ApplicationEvent;

public class RequestClearCacheEvent extends ApplicationEvent {

    public RequestClearCacheEvent(Object source) {
        super(source);
    }
}
