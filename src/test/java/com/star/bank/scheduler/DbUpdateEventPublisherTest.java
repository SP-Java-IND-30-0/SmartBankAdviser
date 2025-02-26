package com.star.bank.scheduler;

import com.star.bank.service.RecommendationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DbUpdateEventPublisherTest {

    @Mock
    private ApplicationEventPublisher publisher;

    @Mock
    private RecommendationService recommendationService;

    @InjectMocks
    private DbUpdateEventPublisher dbUpdateEventPublisher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dbUpdateEventPublisher = new DbUpdateEventPublisher(publisher, recommendationService);
    }

    @Test
    void test_init() {

        List<UUID> testUuids = List.of(UUID.randomUUID(), UUID.randomUUID());

        when(recommendationService.getAllUserIds()).thenReturn(testUuids);

        dbUpdateEventPublisher.init();

        assertNotNull(dbUpdateEventPublisher.getUserIds());
        assertIterableEquals(testUuids, dbUpdateEventPublisher.getUserIds());
        assertEquals(testUuids.size(), dbUpdateEventPublisher.getUserIds().size());

        verify(recommendationService, times(1)).getAllUserIds();

    }
}