package com.star.bank.repositories;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RecommendationRepositoryCacheTest {

    @Autowired
    private RecommendationRepository recommendationRepository;

    @Autowired
    private CacheManager cacheManager;

    private static final String USER_ID_1 = "f37ba8a8-3cd5-4976-9f74-2b21f105da67";
    private static final String USER_ID_2 = "147f6a0f-3b91-413b-ab99-87f081d60d5a";
    private static final String QUERY = "SELECT COUNT(*) > 0 FROM TRANSACTIONS T " +
            "JOIN PUBLIC.PRODUCTS P ON P.ID = T.PRODUCT_ID WHERE T.USER_ID = ? AND P.TYPE='DEBIT'";

    @Test
    void testCacheUsage() {
        Boolean firstResult = recommendationRepository.checkProductRules(USER_ID_1, QUERY);

        Cache cache = cacheManager.getCache("userProductRulesCache");
        Cache.ValueWrapper cachedValue = cache.get(USER_ID_1 + "_" + QUERY);

        System.out.println("Первичный результат: " + firstResult);
        System.out.println("Значение в кеше: " + cachedValue);

        assertNotNull(cachedValue, "Результат должен быть закэширован");

        Boolean secondResult = recommendationRepository.checkProductRules(USER_ID_1, QUERY);

        System.out.println("Вторичный результат: " + secondResult);

        assertEquals(firstResult, secondResult, "Результаты должны быть одинаковыми");

        Cache.ValueWrapper secondCacheValue = cache.get(USER_ID_1 + "_" + QUERY);
        assertNotNull(secondCacheValue, "Результат должен быть извлечен из кеша");
        assertEquals(firstResult, secondCacheValue.get(), "Значение в кеше должно быть равно первичному результату");
    }

    @Test
    @DirtiesContext
    void testCachePerformance() {
        long startTimeFirstRequest = System.currentTimeMillis();
        Boolean firstResult = recommendationRepository.checkProductRules(USER_ID_2, QUERY);
        long endTimeFirstRequest = System.currentTimeMillis();
        long timeFirstRequest = endTimeFirstRequest - startTimeFirstRequest;

        System.out.println("Время первого запроса (к БД): " + timeFirstRequest + " миллисекунд");

        long startTimeSecondRequest = System.currentTimeMillis();
        Boolean secondResult = recommendationRepository.checkProductRules(USER_ID_2, QUERY);
        long endTimeSecondRequest = System.currentTimeMillis();
        long timeSecondRequest = endTimeSecondRequest - startTimeSecondRequest;

        System.out.println("Время второго запроса (из кеша): " + timeSecondRequest + " миллисекунд");

        assertTrue(timeSecondRequest < timeFirstRequest, "Время второго запроса должно быть меньше первого");

        assertEquals(firstResult, secondResult, "Результаты должны быть одинаковыми");
    }
}