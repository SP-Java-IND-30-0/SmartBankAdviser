package com.star.bank.repositories;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class RecommendationRepository {

    private final JdbcTemplate jdbcTemplate;

    public RecommendationRepository(@Qualifier("h2JdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Cacheable(value = "userProductRulesCache", key = "#userId + '_' + #query")
    public boolean checkProductRules(String userId, String query) {
        return Boolean.TRUE.equals(jdbcTemplate.query(query, rs -> rs.next() && rs.getBoolean(1), userId));
    }

    public List<UUID> getAllUserIds() {
        return jdbcTemplate.queryForList("SELECT id FROM users", UUID.class);
    }
}