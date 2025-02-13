package com.star.bank.repositories;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

class RecommendationRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private RecommendationRepository recommendationRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnTrueWhenProductRuleExists() throws SQLException {
        String userId = "123";
        String query = "SELECT is_active FROM users WHERE user_id = ?";

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getBoolean(1)).thenReturn(true);

        ArgumentCaptor<ResultSetExtractor<Boolean>> captor = ArgumentCaptor.forClass(ResultSetExtractor.class);
        when(jdbcTemplate.query(eq(query), captor.capture(), eq(userId))).thenReturn(true);

        boolean result = recommendationRepository.checkProductRules(userId, query);

        ResultSetExtractor<Boolean> extractor = captor.getValue();
        Boolean extractedValue = extractor.extractData(resultSet);

        assertTrue(result);
        assertTrue(extractedValue);
    }

    @Test
    void shouldReturnFalseWhenProductRuleIsFalse() throws SQLException {
        String userId = "123";
        String query = "SELECT is_active FROM users WHERE user_id = ?";

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getBoolean(1)).thenReturn(false);

        @SuppressWarnings("unchecked")
        ArgumentCaptor<ResultSetExtractor<Boolean>> captor =
                ArgumentCaptor.forClass((Class<ResultSetExtractor<Boolean>>) (Class<?>) ResultSetExtractor.class);

        when(jdbcTemplate.query(eq(query), captor.capture(), eq(userId))).thenReturn(false);

        boolean result = recommendationRepository.checkProductRules(userId, query);

        ResultSetExtractor<Boolean> extractor = captor.getValue();
        Boolean extractedValue = extractor.extractData(resultSet);

        assertFalse(result);
        assertFalse(extractedValue);
    }

    @Test
    void shouldReturnFalseWhenNoProductRuleExists() throws SQLException {
        String userId = "123";
        String query = "SELECT is_active FROM users WHERE user_id = ?";

        when(resultSet.next()).thenReturn(false);

        @SuppressWarnings("unchecked")
        ArgumentCaptor<ResultSetExtractor<Boolean>> captor =
                ArgumentCaptor.forClass((Class<ResultSetExtractor<Boolean>>) (Class<?>) ResultSetExtractor.class);
        when(jdbcTemplate.query(eq(query), captor.capture(), eq(userId))).thenReturn(false);

        boolean result = recommendationRepository.checkProductRules(userId, query);

        ResultSetExtractor<Boolean> extractor = captor.getValue();
        Boolean extractedValue = extractor.extractData(resultSet);

        assertFalse(result);
        assertFalse(extractedValue);
    }
}