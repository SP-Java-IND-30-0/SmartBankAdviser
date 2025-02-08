package com.star.bank.repository;

import com.star.bank.model.Recommendation;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendationServiceRepository {
    List <Recommendation> findAllRecommendations(Long id);
}
