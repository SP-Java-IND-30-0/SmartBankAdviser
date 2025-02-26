package com.star.bank.controller;

import com.star.bank.MockProduct;
import com.star.bank.exception.UserNotFoundException;
import com.star.bank.exception.UsernameNotFoundException;
import com.star.bank.model.dto.PersonalRecommendationDto;
import com.star.bank.model.dto.PersonalRecommendationTgDto;
import com.star.bank.model.product.Product;
import com.star.bank.service.RecommendationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RecommendationController.class)
class RecommendationControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RecommendationService recommendationService;

    @Test
    void test_getUserId_whenRecommendationsExist() throws Exception {
        String userId = "user001";
        Product product1 = new MockProduct("01", "product1", "description1");
        Product product2 = new MockProduct("02", "product2", "description2");
        PersonalRecommendationDto dto = new PersonalRecommendationDto(userId);
        dto.setRecommendations(List.of(product1, product2));

        when(recommendationService.sendRecommendation(userId)).thenReturn(CompletableFuture.completedFuture(dto));

        MvcResult result = mockMvc.perform(get("/recommendation/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(request().asyncStarted())
                .andExpect(request().asyncResult(ResponseEntity.ok(dto)))
                .andReturn();

        result.getAsyncResult();

        mockMvc.perform(asyncDispatch(result))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.recommendations").isArray())
                .andExpect(jsonPath("$.recommendations.length()").value(2));

    }

    @Test
    void test_getUserId_whenRecommendationsIsEmpty() throws Exception {
        String userId = "user001";
        PersonalRecommendationDto dto = new PersonalRecommendationDto(userId);

        when(recommendationService.sendRecommendation(userId)).thenReturn(CompletableFuture.completedFuture(dto));

        MvcResult result = mockMvc.perform(get("/recommendation/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(request().asyncStarted())
                .andExpect(request().asyncResult(ResponseEntity.ok(dto)))
                .andReturn();

        result.getAsyncResult();

        mockMvc.perform(asyncDispatch(result))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.recommendations").isArray())
                .andExpect(jsonPath("$.recommendations.length()").value(0));
    }


    @Test
    void test_getUserId_whenIncorrectUserId() throws Exception {
        String userId = "user001";

        when(recommendationService.sendRecommendation(userId))
                .thenReturn(CompletableFuture.failedFuture(new UserNotFoundException(userId)));

        MvcResult result = mockMvc.perform(get("/recommendation/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(request().asyncStarted())
                .andReturn();

        result.getAsyncResult();

        mockMvc.perform(asyncDispatch(result))
                .andExpect(status().isNotFound());
    }

    @Test
    void test_getUsername_whenRecommendationsExist() throws Exception {
        String username = "username";
        Product product1 = new MockProduct("01", "product1", "description1");
        Product product2 = new MockProduct("02", "product2", "description2");
        PersonalRecommendationTgDto dto = PersonalRecommendationTgDto.builder()
                .firstName("testFirstName")
                .lastName("testLastName")
                .build();
        dto.setRecommendations(List.of(product1, product2));

        when(recommendationService.sendRecommendationTg(username)).thenReturn(CompletableFuture.completedFuture(dto));

        MvcResult result = mockMvc.perform(get("/recommendation/username/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(request().asyncStarted())
                .andExpect(request().asyncResult(ResponseEntity.ok(dto)))
                .andReturn();

        result.getAsyncResult();

        mockMvc.perform(asyncDispatch(result))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(dto.getFirstName()))
                .andExpect(jsonPath("$.recommendations").isArray())
                .andExpect(jsonPath("$.recommendations.length()").value(2));

    }

    @Test
    void test_getUsername_whenRecommendationsIsEmpty() throws Exception {
        String username = "username";
        PersonalRecommendationTgDto dto = PersonalRecommendationTgDto.builder()
                .firstName("testFirstName")
                .lastName("testLastName")
                .build();

        when(recommendationService.sendRecommendationTg(username)).thenReturn(CompletableFuture.completedFuture(dto));

        MvcResult result = mockMvc.perform(get("/recommendation/username/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(request().asyncStarted())
                .andExpect(request().asyncResult(ResponseEntity.ok(dto)))
                .andReturn();

        result.getAsyncResult();

        mockMvc.perform(asyncDispatch(result))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(dto.getFirstName()))
                .andExpect(jsonPath("$.recommendations").isArray())
                .andExpect(jsonPath("$.recommendations.length()").value(0));
    }


    @Test
    void test_getUsername_whenIncorrectUsername() throws Exception {
        String username = "username";

        when(recommendationService.sendRecommendationTg(username))
                .thenReturn(CompletableFuture.failedFuture(new UsernameNotFoundException(username)));

        MvcResult result = mockMvc.perform(get("/recommendation/username/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(request().asyncStarted())
                .andReturn();

        result.getAsyncResult();

        mockMvc.perform(asyncDispatch(result))
                .andExpect(status().isNotFound());
    }


}