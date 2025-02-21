package com.star.bank.controller;

import com.star.bank.model.dto.StatsDto;
import com.star.bank.service.StatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rule")
@Tag(name = "StatsController", description = "API для cтатистики срабатывания правил рекомендаций")
public class StatsController {

    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/stats")
    @Operation(summary = "Получить статистику срабатывания правил рекомендаций")
    @ApiResponse(responseCode = "200", description = "Статистика успешно получена")
    @ApiResponse(responseCode = "404", description = "Статистика не найдена")
    public ResponseEntity<StatsDto> getStats() {
        StatsDto stats = statsService.getStats();
        if (stats == null || stats.getStats().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new StatsDto(List.of()));
        }
        return ResponseEntity.ok(stats);
    }
}