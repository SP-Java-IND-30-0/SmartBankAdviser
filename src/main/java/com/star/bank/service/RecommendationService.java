package com.star.bank.service;

import com.star.bank.repository.TransactionRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendationService implements TransactionRepository {
    private  final TransactionRepository transactionRepository;
    private final JdbcTemplate jdbcTemplate;

    public RecommendationService(JdbcTemplate jdbcTemplate, TransactionRepository transactionRepository, JdbcTemplate jdbcTemplate1) {
        this.transactionRepository = transactionRepository;
        this.jdbcTemplate = jdbcTemplate1;
    }

    @Override
    public List<String> getRecommendation(String userId) {
        String result = "SELECT id FROM recommendations WHERE user_id = ?";
        return jdbcTemplate.queryForList(result, String.class, userId);
    }
}
