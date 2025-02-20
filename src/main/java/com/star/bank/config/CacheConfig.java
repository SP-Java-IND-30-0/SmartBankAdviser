package com.star.bank.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.jmx.support.RegistrationPolicy;

import javax.management.MXBean;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
@ManagedResource(objectName = "CacheStats:name=userProductRulesCache")
@MXBean
public class CacheConfig {

    private Cache<Object, Object> cache;

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("userProductRulesCache");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(10000)
                .expireAfterWrite(600, TimeUnit.SECONDS)
                .recordStats());

        org.springframework.cache.Cache cacheWrapper = cacheManager.getCache("userProductRulesCache");
        Object nativeCache = cacheWrapper.getNativeCache();

        @SuppressWarnings("unchecked")
        Cache<Object, Object> caffeineCache = (Cache<Object, Object>) nativeCache;
        this.cache = caffeineCache;

        return cacheManager;
    }


    @Bean
    public MBeanExporter mBeanExporter() {
        MBeanExporter exporter = new MBeanExporter();
        exporter.setBeans(Collections.singletonMap("com.star.bank:type=CacheStats", this));
        exporter.setRegistrationPolicy(RegistrationPolicy.IGNORE_EXISTING);
        return exporter;
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