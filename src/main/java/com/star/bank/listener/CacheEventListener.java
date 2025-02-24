package com.star.bank.listener;

import com.star.bank.event.RequestClearCacheEvent;
import com.star.bank.service.CacheService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CacheEventListener {

    private final CacheService cacheService;

    public CacheEventListener(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @EventListener(RequestClearCacheEvent.class)
    public void clearCache() {
        cacheService.clear();
    }
}
