package com.star.bank.service;

import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

/**
 * Сервис для работы с кэшем приложения.
 * Предоставляет методы для очистки кэша и удаления записей по идентификатору пользователя.
 */
@Service
public class CacheService {

    private final CacheManager cacheManager;

    /**
     * Конструктор для инициализации сервиса.
     *
     * @param cacheManager Менеджер кэша приложения.
     */
    public CacheService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    /**
     * Очищает весь кэш "userProductRulesCache".
     */
    public void clear() {
        Objects.requireNonNull(cacheManager.getCache("userProductRulesCache")).clear();
    }

    /**
     * Удаляет записи из кэша "userProductRulesCache", связанные с указанным идентификатором пользователя.
     *
     * @param userId Идентификатор пользователя, для которого необходимо удалить записи из кэша.
     */
    public void clearCacheRecordsByUserId(UUID userId) {
        var obj = cacheManager.getCache("userProductRulesCache");
        Cache<Object, Object> nativeCache;
        if (obj != null && obj.getNativeCache() instanceof Cache) {
                nativeCache = (Cache<Object,Object>) obj.getNativeCache();
                nativeCache.asMap().keySet().forEach(key -> {
                    if (key instanceof String keyString && keyString.startsWith(userId.toString())) {
                            nativeCache.invalidate(key);
                        }

                });
            }

    }
}
