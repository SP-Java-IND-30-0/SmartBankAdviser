package com.star.bank.controller;

import com.star.bank.exception.UserNotFoundException;
import com.star.bank.model.MockProduct;
import com.star.bank.model.PersonalRecommendationDto;
import com.star.bank.service.RecommendationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RecommendationController.class)
class RecommendationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    RecommendationService recommendationService;

    @InjectMocks
    RecommendationController recommendationController;

    @BeforeEach
    void setUp() {
    }

    @Test
    void test_getUserId_whenRecommendationsExist() throws Exception {
        String userId = "user001";
        MockProduct product1 = new MockProduct("01", "product1", "description1");
        MockProduct product2 = new MockProduct("02", "product2", "description2");
        PersonalRecommendationDto dto = new PersonalRecommendationDto(userId, List.of(product1, product2));

        when(recommendationService.sendRecommendation(userId)).thenReturn(dto);

        mockMvc.perform(get("/recommendation/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.recommendations").isArray())
                .andExpect(jsonPath("$.recommendations.length()").value(2));
    }

    @Test
    void test_getUserId_whenRecommendationsIsEmpty() throws Exception {
        String userId = "user001";
        PersonalRecommendationDto dto = new PersonalRecommendationDto(userId, Collections.emptyList());

        when(recommendationService.sendRecommendation(userId)).thenReturn(dto);

        mockMvc.perform(get("/recommendation/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.recommendations").isArray())
                .andExpect(jsonPath("$.recommendations.length()").value(0));
    }


    @Test
    void test_getUserId_whenIncorrectUserId() throws Exception {
        String userId = "user001";

        when(recommendationService.sendRecommendation(userId)).thenThrow(UserNotFoundException.class);

        mockMvc.perform(get("/recommendation/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}