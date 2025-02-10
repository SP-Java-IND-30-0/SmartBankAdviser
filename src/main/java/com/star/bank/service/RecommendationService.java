package com.star.bank.service;

import com.star.bank.model.PersonalRecommendationDto;

public interface RecommendationService {
    PersonalRecommendationDto sendRecommendation(String userId);
}
