package com.star.bank.config;

import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.jmx.export.annotation.ManagedResource;

import javax.management.MXBean;

@ManagedResource(objectName = "CacheStats:name=userProductRulesCache")
@MXBean
public class CacheStatsMBean {

    private final Cache<Object, Object> cache;

    public CacheStatsMBean(Cache<Object, Object> cache) {
        this.cache = cache;
    }

    public long getHitCount() {
        return cache.stats().hitCount();
    }

    public long getMissCount() {
        return cache.stats().missCount();
    }

    public double getHitRate() {
        return cache.stats().hitRate();
    }

    public long getEvictionCount() {
        return cache.stats().evictionCount();
    }
}