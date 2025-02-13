package com.star.bank;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import com.star.bank.service.RecommendationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.HashSet;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecommendationServiceTest {

    @Mock
    private RecommendationRepository recommendationRepository;
    private RecommendationService service;
    Set<Product> products = new HashSet<>();

    @BeforeEach
    void info(){
        recommendationRepository = mock(RecommendationRepository.class);
        service = new RecommendationService(recommendationRepository,products);

        products.add(() -> "firstProduct");
        products.add(() -> "secondProduct");

    }

    @Test
    void sendRecommendationTest(){
        String userId = "user";

        when(recommendationRepository.checkProductRules(userId, "firstProduct")).thenReturn(true);
        when(recommendationRepository.checkProductRules(userId, "secondProduct")).thenReturn(false);

        PersonalRecommendationDto recommendationDto = service.sendRecommendation(userId);

        Assertions.assertEquals(1, recommendationDto.getRecommendations().size());
        Assertions.assertEquals(userId, recommendationDto.getUserId());
        Assertions.assertEquals("firstProduct", recommendationDto.getRecommendations().get(0).getQuery());

        verify(recommendationRepository, times(1)).checkProductRules(userId, "firstProduct");
        verify(recommendationRepository, times(1)).checkProductRules(userId, "secondProduct");
        verifyNoMoreInteractions(recommendationRepository);
    }

    @Test
    @DisplayName("Когда не находим подходящий продукт - возвращаем пустой список рекомендаций")
    void nullRecommendationList() {
        String userId = "dkjskdfbsdjf";
        when(recommendationRepository.checkProductRules(any(), any())).thenReturn(false);

        PersonalRecommendationDto personalRecommendationDto = service.sendRecommendation(userId);

        assertThat(personalRecommendationDto).isNotNull();
        assertThat(personalRecommendationDto.getUserId()).isEqualTo(userId);
        assertThat(personalRecommendationDto.getRecommendations()).isNotNull().isEmpty();
    }




}
