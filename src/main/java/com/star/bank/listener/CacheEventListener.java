package com.star.bank.listener;

import com.star.bank.event.DbUpdateEvent;
import com.star.bank.event.RequestClearCacheEvent;
import com.star.bank.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Слушатель событий, связанных с кэшем приложения.
 * Обрабатывает события очистки кэша и обновления данных в базе данных.
 */
@Component
@Slf4j
public class CacheEventListener {

    private final CacheService cacheService;

    /**
     * Слушатель событий, связанных с кэшем приложения.
     * Обрабатывает события очистки кэша и обновления данных в базе данных.
     */
    public CacheEventListener(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    /**
     * Обрабатывает событие запроса на очистку кэша.
     * Вызывает метод очистки кэша в сервисе.
     */
    @EventListener(RequestClearCacheEvent.class)
    public void clearCache() {
        cacheService.clear();
    }

    /**
     * Обрабатывает событие обновления данных в базе данных.
     * Очищает записи кэша для пользователя, чьи данные были обновлены.
     *
     * @param event Событие обновления данных.
     */
    @EventListener(DbUpdateEvent.class)
    public void handleEvent(DbUpdateEvent event) {
        log.info("The data of user with ID={} has been updated in the database", event.getUserId());
        cacheService.clearCacheRecordsByUserId(event.getUserId());
    }


}
