package com.star.bank;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class RecommendationRepository {
    private final JdbcTemplate jdbcTemplate;

    private static final String DEPOSIT = "DEPOSIT";
    private static final String INVEST = "INVEST";
    private static final String SAVING = "SAVING";

    public RecommendationRepository(@Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<String> checkProductRules(UUID userId) {
        return jdbcTemplate.query(
                "SELECT r.product_name FROM recommendations r " +
                        "WHERE EXISTS (" +
                        "    SELECT 1 FROM transactions t " +
                        "    INNER JOIN products p ON t.product_id = p.id " +
                        "    WHERE t.user_id = ? " +
                        "    GROUP BY t.user_id " +
                        "    HAVING " +
                        "        SUM(CASE WHEN p.type = ? THEN 1 ELSE 0 END) >= 1 AND " +
                        "        SUM(CASE WHEN p.type = ? THEN 1 ELSE 0 END) = 0 AND " +
                        "        SUM(CASE WHEN p.type = ? AND t.type = ? THEN t.amount ELSE 0 END) > 1000" +
                        ")",
                (rs, rowNum) -> rs.getString("product_name"),
                userId, DEPOSIT, INVEST, SAVING
        );
    }
}