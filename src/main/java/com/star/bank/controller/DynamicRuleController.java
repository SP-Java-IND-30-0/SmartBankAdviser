import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/rule")
@RequiredArgsConstructor
@Slf4j
public class DynamicRuleController {

    private final DynamicRuleService dynamicRuleService;

    @PostMapping
    public ResponseEntity<DynamicRuleDto> saveDynamicRule(@RequestBody DynamicRuleDto dynamicRuleDto) {
        dynamicRuleService.saveDynamicRule(dynamicRuleDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(dynamicRuleDto); // 201 Created + возврат переданного объекта
    }

    @GetMapping
    public ResponseEntity<List<DynamicRuleDto>> getDynamicRules() {;
        List<DynamicRuleDto> rules = dynamicRuleService.getDynamicRules();
        return ResponseEntity.ok(rules);
    }

    @DeleteMapping("/{ruleId}")
    public ResponseEntity<Void> deleteDynamicRule(@PathVariable String ruleId) {
        dynamicRuleService.deleteDynamicRule(ruleId);
        return ResponseEntity.noContent().build();
    }
}
