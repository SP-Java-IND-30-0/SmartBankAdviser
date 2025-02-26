package com.star.bank.controller;

import com.star.bank.config.AppConfig;
import com.star.bank.event.RequestClearCacheEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ManagementControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ApplicationEventPublisher publisher;

    private final String requestPath = "/management";

    @BeforeEach
    void setup() {
        ManagementController controller = new ManagementController(publisher);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void test_clearCaches() throws Exception {

        doNothing().when(publisher).publishEvent(any(RequestClearCacheEvent.class));

        mockMvc.perform(post(requestPath + "/clear-caches")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(publisher, times(1)).publishEvent(any(RequestClearCacheEvent.class));

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