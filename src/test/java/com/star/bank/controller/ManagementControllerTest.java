package com.star.bank.controller;

import com.star.bank.config.AppConfig;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ManagementController.class)
class ManagementControllerTest {

    @Autowired
    MockMvc mockMvc;

    private final String requestPath = "/management";

    @Test
    void test_clearCaches() throws Exception {

        mockMvc.perform(post(requestPath + "/clear-caches")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

    }

    @Test
    void test_getInfo() throws Exception {

        try (MockedStatic<AppConfig> mockedAppConfig = Mockito.mockStatic(AppConfig.class)) {
            mockedAppConfig.when(AppConfig::getName).thenReturn("test");
            mockedAppConfig.when(AppConfig::getVersion).thenReturn("1.0.0");

            mockMvc.perform(get(requestPath + "/info")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("test"))
                    .andExpect(jsonPath("$.version").value("1.0.0"));
        }

    }
}