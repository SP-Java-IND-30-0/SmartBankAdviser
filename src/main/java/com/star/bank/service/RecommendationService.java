package com.star.bank.service;

import com.star.bank.model.Recommendation;
import com.star.bank.repository.RecommendationServiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendationService implements RecommendationServiceRepository {

    private  final RecommendationServiceRepository repository;

    public RecommendationService(RecommendationServiceRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Recommendation> findAllRecommendations(Long id) {
        return repository.findAllRecommendations(id);
    }

}
