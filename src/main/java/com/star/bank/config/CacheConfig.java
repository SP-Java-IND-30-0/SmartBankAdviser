package com.star.bank.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.jmx.support.RegistrationPolicy;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    @Primary
    @Qualifier("caffeineCacheManager")
    public CaffeineCacheManager caffeineCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(10000)
                .expireAfterWrite(600, TimeUnit.SECONDS)
                .recordStats());
        return cacheManager;
    }

    @Bean
    @Qualifier("cacheManager")
    public CacheManager cacheManager(@Qualifier("caffeineCacheManager") CacheManager cacheManager) {
        return cacheManager;
    }

    @Bean
    public CacheStatsMBean cacheStatsMBean(CacheManager cacheManager) {
        if (!(cacheManager instanceof CaffeineCacheManager caffeineCacheManager)) {
            throw new IllegalStateException("CacheManager не является CaffeineCacheManager");
        }

        org.springframework.cache.Cache cacheWrapper = caffeineCacheManager.getCache("userProductRulesCache");

        Object nativeCache = cacheWrapper.getNativeCache();

        if (!(nativeCache instanceof Cache<?, ?>)) {
            throw new IllegalStateException("Неподдерживаемый тип кэша: " + nativeCache.getClass());
        }

        @SuppressWarnings("unchecked")
        Cache<Object, Object> cache = (Cache<Object, Object>) nativeCache;

        return new CacheStatsMBean(cache);
    }

    @Bean
    public MBeanExporter mBeanExporter(CacheStatsMBean cacheStatsMBean) {
        MBeanExporter exporter = new MBeanExporter();
        exporter.setBeans(Collections.singletonMap("com.example:type=CacheStats", cacheStatsMBean));
        exporter.setRegistrationPolicy(RegistrationPolicy.IGNORE_EXISTING);
        return exporter;
    }
}