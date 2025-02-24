package com.star.bank.service;

import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
public class CacheService {

    private final CacheManager cacheManager;

    public CacheService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public void clear() {
        Objects.requireNonNull(cacheManager.getCache("userProductRulesCache")).clear();
    }

    public void clearCacheRecordsByUserId(UUID userId) {
        var obj = cacheManager.getCache("userProductRulesCache");
        Cache<Object, Object> nativeCache;
        if (obj != null) {
            if (obj.getNativeCache() instanceof Cache) {
                nativeCache = (Cache<Object,Object>) obj.getNativeCache();
                nativeCache.asMap().keySet().forEach(key -> {
                    if (key instanceof String keyString) {
                        if (keyString.startsWith(userId.toString())) {
                            nativeCache.invalidate(key);
                        }
                    }
                });
            }
        }
    }
}
