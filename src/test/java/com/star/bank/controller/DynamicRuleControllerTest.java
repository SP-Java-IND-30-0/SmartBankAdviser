package com.star.bank.controller;

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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        String jsonContent = "{"
                + "\"product_id\": \"123e4567-e89b-12d3-a456-426614174000\","
                + "\"product_name\": \"Test Product\","
                + "\"product_text\": \"Test Recommendation\","
                + "\"rule\": []"
                + "}";

        doNothing().when(dynamicRuleService).saveDynamicRule(any(DynamicRuleDto.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/rule")
                        .content(jsonContent)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.product_id").value("123e4567-e89b-12d3-a456-426614174000"))
                .andExpect(jsonPath("$.product_name").value("Test Product"))
                .andExpect(jsonPath("$.product_text").value("Test Recommendation"))
                .andExpect(jsonPath("$.rule").isArray())
                .andExpect(jsonPath("$.rule").isEmpty());
    }

    @Test
    void test_getDynamicRules() throws Exception {
        DynamicRuleDto rule1 = new DynamicRuleDto();
        rule1.setProductId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
        rule1.setProductName("Test Product");
        rule1.setProductText("Test Recommendation");
        rule1.setRules(Collections.emptySet());

        List<DynamicRuleDto> mockRules = List.of(rule1);
        when(dynamicRuleService.getDynamicRules()).thenReturn(mockRules);

        mockMvc.perform(get("/rule")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].product_id").value("123e4567-e89b-12d3-a456-426614174000"))
                .andExpect(jsonPath("$[0].product_name").value("Test Product"))
                .andExpect(jsonPath("$[0].product_text").value("Test Recommendation"));
    }

    @Test
    void test_getDynamicRulesEmpty() throws Exception {
        when(dynamicRuleService.getDynamicRules()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/rule")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void test_deleteDynamicRule() throws Exception {
        String requestPath = "/rule";
        mockMvc.perform(delete(requestPath + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void test_getStats() throws Exception {
        StatsDto mockStats = new StatsDto(Collections.emptyList());

        when(statsService.getStats()).thenReturn(mockStats);

        mockMvc.perform(get("/rule/stats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stats").exists())
                .andExpect(jsonPath("$.stats").isArray())
                .andExpect(jsonPath("$.stats").isEmpty());
    }
}