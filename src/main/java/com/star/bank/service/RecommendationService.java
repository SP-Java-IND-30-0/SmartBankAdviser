package com.star.bank.service;

import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class RecommendationService implements TransactionRepository {
    private Logger logger = LoggerFactory.getLogger(TransactionRepository.class);
    private final Rule rule;
    private final Product product;
    private  final TransactionRepository transactionRepository;
    private final JdbcTemplate jdbcTemplate;

    private String productQuery = product.getQuery();
    private String ruleQuery = rule.getSubQuery;

    public RecommendationService(JdbcTemplate jdbcTemplate, TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

//    public List<Recommendation> insertRecommendation(String productQuery, String ruleQuery) {
//        return recommendation = jdbcTemplate.query(productQuery, ruleQuery);
//    }

    public List<Recommendation> getRecommendation(String userId) {
        String result = "SELECT id FROM recommendations WHERE user_id = ?";
        return jdbcTemplate.queryForList(result, Recommendation.class, userId);
    }

}
