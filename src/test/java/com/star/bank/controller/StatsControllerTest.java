package com.star.bank.controller;

import com.star.bank.model.dto.StatsDto;
import com.star.bank.service.StatsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.lenient;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class StatsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private StatsService statsService;

    @InjectMocks
    private StatsController statsController;

    @Test
    void testGetStats() throws Exception {
        List<StatsDto.ProductStat> statsList = List.of();
        StatsDto mockStats = new StatsDto(statsList);

        lenient().when(statsService.getStats()).thenReturn(mockStats);

        mockMvc.perform(get("/rule/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stats").isArray())
                .andExpect(jsonPath("$.stats").isEmpty());
    }
}