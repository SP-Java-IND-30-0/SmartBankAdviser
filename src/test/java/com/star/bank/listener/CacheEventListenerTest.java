package com.star.bank.listener;

import com.star.bank.event.DbUpdateEvent;
import com.star.bank.service.CacheService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CacheEventListenerTest {

    @Mock
    private CacheService cacheService;

    @InjectMocks
    private CacheEventListener cacheEventListener;

    @BeforeEach
    void setUp() {
    }

    @Test
    void test_clearCache() {
        cacheEventListener.clearCache();
        verify(cacheService, times(1)).clear();
    }

    @Test
    void test_handleEvent() {

        DbUpdateEvent event = new DbUpdateEvent(new Object(), UUID.randomUUID());
        doNothing().when(cacheService).clearCacheRecordsByUserId(any());
        cacheEventListener.handleEvent(event);
        verify(cacheService, times(1)).clearCacheRecordsByUserId(event.getUserId());
    }
}