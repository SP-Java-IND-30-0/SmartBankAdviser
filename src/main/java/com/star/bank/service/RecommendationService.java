package com.star.bank.service;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RecommendationService implements TransactionRepository {
    private  final TransactionRepository transactionRepository;
    private final List<Product> productList;

    public RecommendationService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void sendRecommendation(String userId) {



    }

}
