package com.star.bank.service;


import com.star.bank.model.MockProduct;
import com.star.bank.model.PersonalRecommendationDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MockService implements RecommendationService {

    @Override
    public PersonalRecommendationDto sendRecommendation(String userId) {
        return new PersonalRecommendationDto(userId, List.of(new MockProduct("001", "Product1", "Description1")));
    }
}
