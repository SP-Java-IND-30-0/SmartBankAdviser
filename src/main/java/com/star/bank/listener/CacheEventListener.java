package com.star.bank.listener;

import com.star.bank.event.DbUpdateEvent;
import com.star.bank.event.RequestClearCacheEvent;
import com.star.bank.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CacheEventListener {

    private final CacheService cacheService;

    public CacheEventListener(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @EventListener(RequestClearCacheEvent.class)
    public void clearCache() {
        cacheService.clear();
    }

    @EventListener(DbUpdateEvent.class)
    public void handleEvent(DbUpdateEvent event) {
        log.info("The data of user with ID={} has been updated in the database", event.getUserId());
        cacheService.clearCacheRecordsByUserId(event.getUserId());
    }


}
