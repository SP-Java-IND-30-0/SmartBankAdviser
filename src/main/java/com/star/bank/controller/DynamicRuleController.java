import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rule")
@RequiredArgsConstructor
@Slf4j
public class DynamicRuleController {

    private final DynamicRuleService dynamicRuleService;

    @PostMapping
    public ResponseEntity<DynamicRuleDto> addDynamicRule(@RequestBody DynamicRuleDto dynamicRuleDto) {
        log.info("Добавление нового динамического правила: {}", dynamicRuleDto);
        DynamicRuleDto createdRule = dynamicRuleService.addDynamicRule(dynamicRuleDto);
        return ResponseEntity.ok(createdRule);
    }

    @GetMapping
    public ResponseEntity<List<DynamicRuleDto>> getDynamicRules() {
        log.info("Получение списка всех динамических правил");
        List<DynamicRuleDto> rules = dynamicRuleService.getDynamicRules();
        return ResponseEntity.ok(rules);
    }

    @DeleteMapping("/{ruleId}")
    public ResponseEntity<Void> deleteDynamicRule(@PathVariable String ruleId) {
        log.info("Удаление динамического правила с ID: {}", ruleId);
        dynamicRuleService.deleteDynamicRule(ruleId);
        return ResponseEntity.noContent().build();
    }
}
