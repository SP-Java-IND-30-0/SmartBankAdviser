package com.star.bank.repositories;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RecommendationRepository {

    public RecommendationRepository(@Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate) {}

    public boolean checkProductRules(String userId, Product product) {
        String query = product.getQuery(userId);
        Integer result = jdbcTemplate.queryForObject(query, Integer.class);
        return result != null && result > 0;
    }
}