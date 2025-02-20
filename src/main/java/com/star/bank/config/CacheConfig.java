package com.star.bank.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
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
public class CacheConfig {

    @Bean
    public CacheManager caffeineCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(10000)
                .expireAfterWrite(600, TimeUnit.SECONDS)
                .recordStats());
        return cacheManager;
    }

    @Bean
    public CacheStatsMBean cacheStatsMBean(CacheManager cacheManager) {
        if (!(cacheManager instanceof CaffeineCacheManager caffeineCacheManager)) {
            throw new IllegalStateException("CacheManager не является CaffeineCacheManager");
        }

        org.springframework.cache.Cache cacheWrapper = caffeineCacheManager.getCache("userProductRulesCache");

        if (!(cacheWrapper.getNativeCache() instanceof Cache<?, ?>)) {
            throw new IllegalStateException(
                    "Неподдерживаемый тип кэша: " + cacheWrapper.getNativeCache().getClass()
            );
        }
        @SuppressWarnings("unchecked")
        Cache<Object, Object> cache = (Cache<Object, Object>) cacheWrapper.getNativeCache();

        return new CacheStatsMBean(cache);
    }

    @Bean
    public MBeanExporter mBeanExporter(CacheStatsMBean cacheStatsMBean) {
        MBeanExporter exporter = new MBeanExporter();
        exporter.setBeans(Collections.singletonMap("com.star.bank:type=CacheStats", cacheStatsMBean));
        exporter.setRegistrationPolicy(RegistrationPolicy.IGNORE_EXISTING);
        return exporter;
    }

    @ManagedResource(objectName = "CacheStats:name=userProductRulesCache")
    @MXBean
    public static class CacheStatsMBean {

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
}