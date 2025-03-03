package com.star.bank.controller;

import com.star.bank.TestUtils;
import com.star.bank.exception.*;
import com.star.bank.model.dto.DynamicRuleDto;
import com.star.bank.model.dto.StatsDto;
import com.star.bank.service.DynamicRuleService;
import com.star.bank.service.StatsService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.hamcrest.Matchers.containsString;

@WebMvcTest(controllers = DynamicRuleController.class)
class DynamicRuleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DynamicRuleService dynamicRuleService;

    @MockitoBean
    private StatsService statsService;

    @InjectMocks
    private DynamicRuleController dynamicRuleController;

    @Test
    void test_saveDynamicRule() throws Exception {
        String jsonContent = TestUtils.createJsonContent();

        doNothing().when(dynamicRuleService).saveDynamicRule(any(DynamicRuleDto.class));

        mockMvc.perform(post(TestUtils.RULES_ENDPOINT)
                        .content(jsonContent)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.product_id").value(TestUtils.PRODUCT_ID))
                .andExpect(jsonPath("$.product_name").value(TestUtils.PRODUCT_NAME))
                .andExpect(jsonPath("$.product_text").value(TestUtils.PRODUCT_TEXT))
                .andExpect(jsonPath("$.rule").isArray())
                .andExpect(jsonPath("$.rule").isEmpty());
    }

    @Test
    void test_saveDynamicRule_invalidRequest() throws Exception {
        String invalidJson = "{}";

        mockMvc.perform(post(TestUtils.RULES_ENDPOINT)
                        .content(invalidJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void test_saveDynamicRule_databaseError() throws Exception {
        String jsonContent = TestUtils.createJsonContent();

        doThrow(new DatabaseSaveException(new Exception()))
                .when(dynamicRuleService).saveDynamicRule(any(DynamicRuleDto.class));

        mockMvc.perform(post(TestUtils.RULES_ENDPOINT)
                        .content(jsonContent)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadGateway())
                .andExpect(content().string(containsString("Error saving data to the database")));
    }

    @Test
    void test_saveDynamicRule_invalidRuleData() throws Exception {
        String jsonContent = TestUtils.createJsonContent();

        doThrow(new InvalidRuleDataException(new Exception()))
                .when(dynamicRuleService).saveDynamicRule(any(DynamicRuleDto.class));

        mockMvc.perform(post(TestUtils.RULES_ENDPOINT)
                        .content(jsonContent)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Invalid rule data")));
    }

    @Test
    void test_getDynamicRules() throws Exception {
        DynamicRuleDto rule1 = TestUtils.createDynamicRuleDto();

        List<DynamicRuleDto> mockRules = List.of(rule1);
        when(dynamicRuleService.getDynamicRules()).thenReturn(mockRules);

        mockMvc.perform(get(TestUtils.RULES_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].product_id").value(TestUtils.PRODUCT_ID))
                .andExpect(jsonPath("$[0].product_name").value(TestUtils.PRODUCT_NAME))
                .andExpect(jsonPath("$[0].product_text").value(TestUtils.PRODUCT_TEXT));
    }

    @Test
    void test_getDynamicRulesEmpty() throws Exception {
        when(dynamicRuleService.getDynamicRules()).thenReturn(Collections.emptyList());

        mockMvc.perform(get(TestUtils.RULES_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void test_getDynamicRules_databaseError() throws Exception {
        when(dynamicRuleService.getDynamicRules()).thenThrow(new DatabaseAccessException("Database error", new Exception()));

        mockMvc.perform(get(TestUtils.RULES_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isServiceUnavailable())
                .andExpect(content().string(containsString("Database error")));
    }

    @Test
    void test_deleteDynamicRule() throws Exception {
        mockMvc.perform(delete(TestUtils.RULES_ENDPOINT + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void test_deleteDynamicRule_invalidId() throws Exception {
        String invalidId = "invalid-uuid";

        doThrow(new InvalidProductIdException(invalidId, new Exception()))
                .when(dynamicRuleService).deleteDynamicRule(invalidId);

        mockMvc.perform(delete(TestUtils.RULES_ENDPOINT + "/" + invalidId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Invalid product ID")));
    }

    @Test
    void test_deleteDynamicRule_notFound() throws Exception {
        String nonExistentId = "123e4567-e89b-12d3-a456-426614174999";

        doThrow(new ProductNotFoundException(UUID.fromString(nonExistentId), new Exception()))
                .when(dynamicRuleService).deleteDynamicRule(nonExistentId);

        mockMvc.perform(delete(TestUtils.RULES_ENDPOINT + "/" + nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("not found")));
    }

    @Test
    void test_getStats() throws Exception {
        StatsDto mockStats = new StatsDto(Collections.emptyList());

        when(statsService.getStats()).thenReturn(mockStats);

        mockMvc.perform(get(TestUtils.STATS_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stats").exists())
                .andExpect(jsonPath("$.stats").isArray())
                .andExpect(jsonPath("$.stats").isEmpty());
    }
}