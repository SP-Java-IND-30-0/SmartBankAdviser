package com.star.bank.controller;

import com.star.bank.mapper.DynamicRuleMapper;
import com.star.bank.model.dto.DynamicRuleDto;
import com.star.bank.service.DynamicRuleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashSet;
import java.util.UUID;

import static com.star.bank.TestDynamicRule.*;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
class DynamicRuleControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private final TestRestTemplate restTemplate = new TestRestTemplate();

    @Container
    static PostgreSQLContainer<?> cont = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("test_db")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private DynamicRuleService dynamicRuleService;

    @Autowired
    private DynamicRuleMapper dynamicRuleMapper;

    private static DynamicRuleDto testRule;

    @BeforeAll
    static void init(@Autowired DynamicRuleMapper mapper,
                     @Autowired DynamicRuleService service) {

        DynamicRuleDto alwaysTrueRule = getAlwaysTrueProduct();
        DynamicRuleDto testRule = getTestProduct();
        DynamicRuleDto alwaysFalseRule = getAlwaysFalseProduct();

        if (alwaysTrueRule.getRules() == null) {
            alwaysTrueRule.setRules(new HashSet<>());
        }
        if (testRule.getRules() == null) {
            testRule.setRules(new HashSet<>());
        }
        if (alwaysFalseRule.getRules() == null) {
            alwaysFalseRule.setRules(new HashSet<>());
        }

        service.saveDynamicRule(alwaysTrueRule);
        service.saveDynamicRule(alwaysFalseRule);
        service.saveDynamicRule(testRule);
    }

    @DynamicPropertySource
    static void properties(final DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", cont::getJdbcUrl);
        registry.add("spring.datasource.username", cont::getUsername);
        registry.add("spring.datasource.password", cont::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.PostgreSQLDialect");
    }

    @Test
    void test_createDynamicRule_success() {
        String url = "http://localhost:" + port + "/rule";

        DynamicRuleDto newRule = new DynamicRuleDto();
        newRule.setProductId(UUID.fromString(UUID.randomUUID().toString()));
        newRule.setProductName("New Dynamic Rule");

        newRule.setRules(new HashSet<>());

        ResponseEntity<DynamicRuleDto> response = restTemplate
                .postForEntity(url, newRule, DynamicRuleDto.class);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(newRule.getProductName(), response.getBody().getProductName());
    }

    @Test
    void test_getAllDynamicRules_success() {
        String url = "http://localhost:" + port + "/rule";

        ResponseEntity<DynamicRuleDto[]> response = restTemplate
                .getForEntity(url, DynamicRuleDto[].class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertTrue(response.getBody().length > 0);
    }

    @Test
    void test_deleteDynamicRule_success() {
        String ruleId = UUID.randomUUID().toString();
        DynamicRuleDto ruleToDelete = new DynamicRuleDto();
        ruleToDelete.setProductId(UUID.fromString(ruleId));
        ruleToDelete.setProductName("Rule to delete");
        ruleToDelete.setRules(new HashSet<>());

        dynamicRuleService.saveDynamicRule(ruleToDelete);

        String url = "http://localhost:" + port + "/rule/" + ruleId;
        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void test_deleteDynamicRule_notFound() {
        String ruleId = UUID.randomUUID().toString();
        String url = "http://localhost:" + port + "/rule/" + ruleId;

        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void test_createDynamicRule_badRequest() {
        String url = "http://localhost:" + port + "/rule";

        DynamicRuleDto invalidRule = new DynamicRuleDto();
        invalidRule.setProductId(UUID.randomUUID());
        invalidRule.setProductName("Invalid Product");
        invalidRule.setProductText("");
        invalidRule.setRules(new HashSet<>());

        ResponseEntity<String> response = restTemplate
                .postForEntity(url, invalidRule, String.class);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void test_createDynamicRule_invalidProductName() {
        String url = "http://localhost:" + port + "/rule";

        DynamicRuleDto invalidRule = new DynamicRuleDto();
        invalidRule.setProductId(UUID.randomUUID());
        invalidRule.setProductName("");
        invalidRule.setProductText("Valid text");
        invalidRule.setRules(new HashSet<>());

        ResponseEntity<String> response = restTemplate
                .postForEntity(url, invalidRule, String.class);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertTrue(response.getBody().contains("Product name cannot be empty"));
    }
}