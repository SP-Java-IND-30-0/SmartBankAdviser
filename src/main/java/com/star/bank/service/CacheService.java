package com.star.bank.service;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CacheService {

    private final CacheManager cacheManager;

    public CacheService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public void clear() {
        Objects.requireNonNull(cacheManager.getCache("userProductRulesCache")).clear();
    }
}
