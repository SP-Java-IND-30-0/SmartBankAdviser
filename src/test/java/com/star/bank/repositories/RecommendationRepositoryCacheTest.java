package com.star.bank.repositories;

import com.star.bank.SmartBankAdviserApplication;
import com.star.bank.config.CacheConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {SmartBankAdviserApplication.class, CacheConfig.class})
public class RecommendationRepositoryCacheTest {

    @Autowired
    private RecommendationRepository recommendationRepository;

    @Autowired
    private CacheManager cacheManager;

    private static final String USER_ID = "f37ba8a8-3cd5-4976-9f74-2b21f105da67";
    private static final String QUERY = "SELECT COUNT(*) > 0 FROM TRANSACTIONS T " +
            "JOIN PUBLIC.PRODUCTS P ON P.ID = T.PRODUCT_ID WHERE T.USER_ID = ? AND P.TYPE='DEBIT'";

    @BeforeAll
    public static void setUpDatabase() {
    }

    @AfterAll
    public static void cleanUp() {
    }

    @Test
    void testCacheUsage() {
        Boolean firstResult = recommendationRepository.checkProductRules(USER_ID, QUERY);
        assertNotNull(firstResult, "Первоначальный результат не должен быть null");

        Cache cache = cacheManager.getCache("userProductRulesCache");
        Cache.ValueWrapper cachedValue = cache.get(USER_ID + "_" + QUERY);

        System.out.println("Первый результат: " + firstResult);
        System.out.println("Значение в кеше: " + cachedValue);

        assertNotNull(cachedValue, "Результат должен быть закэширован");

        cache.evict(USER_ID + "_" + QUERY);

        Boolean secondResult = recommendationRepository.checkProductRules(USER_ID, QUERY);
        assertNotNull(secondResult, "Вторичный результат не должен быть null");

        System.out.println("Второй результат: " + secondResult);

        assertEquals(firstResult, secondResult, "Результаты должны быть одинаковыми");

        Cache.ValueWrapper secondCacheValue = cache.get(USER_ID + "_" + QUERY);
        assertNotNull(secondCacheValue, "Результат должен быть извлечен из кеша");
        assertEquals(firstResult, secondCacheValue.get(), "Значение в кеше должно быть равно первичному результату");
    }
    @Test
    void testCachePerformance() {
        long startTimeFirstRequest = System.currentTimeMillis();
        Boolean firstResult = recommendationRepository.checkProductRules(USER_ID, QUERY);
        long endTimeFirstRequest = System.currentTimeMillis();
        long timeFirstRequest = endTimeFirstRequest - startTimeFirstRequest;
        assertNotNull(firstResult, "Первоначальный результат не должен быть null");

        System.out.println("Время первого запроса (к БД): " + timeFirstRequest + " миллисекунд");

        long startTimeSecondRequest = System.currentTimeMillis();
        Boolean secondResult = recommendationRepository.checkProductRules(USER_ID, QUERY);
        long endTimeSecondRequest = System.currentTimeMillis();
        long timeSecondRequest = endTimeSecondRequest - startTimeSecondRequest;
        assertNotNull(secondResult, "Вторичный результат не должен быть null");

        System.out.println("Время второго запроса (из кеша): " + timeSecondRequest + " миллисекунд");

        assertTrue(timeSecondRequest < timeFirstRequest, "Время второго запроса должно быть меньше первого");

        assertEquals(firstResult, secondResult, "Результаты должны быть одинаковыми");
    }
}