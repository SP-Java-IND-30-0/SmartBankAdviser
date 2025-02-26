package com.star.bank.controller;

import com.star.bank.model.dto.DynamicRuleDto;
import com.star.bank.model.dto.StatsDto;
import com.star.bank.service.DynamicRuleService;
import com.star.bank.service.StatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/rule")
@RequiredArgsConstructor
@Tag(name = "DynamicRule", description = "API для работы с динамическими правилами")
public class DynamicRuleController {

    private final DynamicRuleService dynamicRuleService;
    private final StatsService statsService;

    @PostMapping
    @Operation(summary = "Новое динамическое правило для пользователя")
    @ApiResponse(responseCode = "201", description = "Динамическое правило успешно создано")
    @ApiResponse(responseCode = "400", description = "Неверный формат запроса")
    public ResponseEntity<DynamicRuleDto> saveDynamicRule(@RequestBody DynamicRuleDto dynamicRuleDto) {
        dynamicRuleService.saveDynamicRule(dynamicRuleDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(dynamicRuleDto);
    }

    @GetMapping
    @Operation(summary = "Получить список всех динамических правил")
    @ApiResponse(responseCode = "200", description = "Список динамических правил успешно получен")
    @ApiResponse(responseCode = "404", description = "Ресурс не найден на сервере")
    public ResponseEntity<List<DynamicRuleDto>> getDynamicRules() {
        List<DynamicRuleDto> rules = dynamicRuleService.getDynamicRules();
        return ResponseEntity.ok(rules);
    }

    @DeleteMapping("/{ruleId}")
    @Operation(summary = "Получить список всех динамических правил")
    @ApiResponse(responseCode = "204", description = "Динамическое правило успешно удалено")
    @ApiResponse(responseCode = "404", description = "Ресурс не найден на сервере")
    public ResponseEntity<Void> deleteDynamicRule(@PathVariable String ruleId) {
        dynamicRuleService.deleteDynamicRule(ruleId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/stats")
    @Operation(summary = "Получить статистику срабатывания правил рекомендаций")
    @ApiResponse(responseCode = "200", description = "Запрос успешно обработан")
    public ResponseEntity<StatsDto> getStats() {
        StatsDto stats = statsService.getStats();
        return ResponseEntity.ok(stats);
    }
}
