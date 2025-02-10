package com.star.bank.repositories;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RecommendationRepository {

    private final JdbcTemplate jdbcTemplate;

    public RecommendationRepository(@Qualifier("h2JdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean checkProductRules(String userId, String query) {
        return jdbcTemplate.query(query, rs -> rs.next() && rs.getBoolean(1), userId);
    }
}