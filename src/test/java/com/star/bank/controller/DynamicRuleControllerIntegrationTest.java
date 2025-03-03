package com.star.bank.controller;

import com.star.bank.model.dto.DynamicRuleDto;
import com.star.bank.service.DynamicRuleService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.star.bank.TestDynamicRule.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
class DynamicRuleControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private final TestRestTemplate restTemplate = new TestRestTemplate();

    @Autowired
    private DynamicRuleService dynamicRuleService;

    @Container
    static final PostgreSQLContainer<?> cont = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("test_db")
            .withUsername("test")
            .withPassword("test");

    private static DynamicRuleDto testRule;

    @BeforeAll
    static void startContainer() {
        cont.start();
    }

    @AfterAll
    static void stopContainer() {
        cont.stop();
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
    void test_getAllDynamicRules_success(@Autowired DynamicRuleService service) {
        DynamicRuleDto alwaysTrueRule = getAlwaysTrueProduct();
        DynamicRuleDto testRule = getTestProduct();
        DynamicRuleDto alwaysFalseRule = getAlwaysFalseProduct();

        testRule.setRules(testRule.getRules() == null ? new HashSet<>() : testRule.getRules());
        alwaysFalseRule.setRules(alwaysFalseRule.getRules() == null ? new HashSet<>() : alwaysFalseRule.getRules());

        service.saveDynamicRule(alwaysTrueRule);
        service.saveDynamicRule(alwaysFalseRule);
        service.saveDynamicRule(testRule);

        List<DynamicRuleDto> rulesInDb = dynamicRuleService.getDynamicRules();
        Assertions.assertEquals(3, rulesInDb.size(), "Количество правил в БД не совпадает с ожиданиями");

        List<String> dbNames = rulesInDb.stream()
                .map(DynamicRuleDto::getProductName)
                .toList();

        Assertions.assertTrue(dbNames.contains(alwaysTrueRule.getProductName()), "В БД отсутствует alwaysTrueRule");
        Assertions.assertTrue(dbNames.contains(testRule.getProductName()), "В БД отсутствует testRule");
        Assertions.assertTrue(dbNames.contains(alwaysFalseRule.getProductName()), "В БД отсутствует alwaysFalseRule");

        String url = "http://localhost:" + port + "/rule";
        ResponseEntity<DynamicRuleDto[]> response = restTemplate.getForEntity(url, DynamicRuleDto[].class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(), "Неверный статус-код ответа");

        Assertions.assertNotNull(response.getBody(), "Ответ API пустой");

        DynamicRuleDto[] responseBody = response.getBody();
        Assertions.assertEquals(3, responseBody.length, "Количество правил в API-ответе не совпадает с ожиданиями");

        List<String> returnedNames = Arrays.stream(responseBody)
                .map(DynamicRuleDto::getProductName)
                .toList();

        Assertions.assertTrue(returnedNames.contains(alwaysTrueRule.getProductName()), "Ответ API не содержит alwaysTrueRule");
        Assertions.assertTrue(returnedNames.contains(testRule.getProductName()), "Ответ API не содержит testRule");
        Assertions.assertTrue(returnedNames.contains(alwaysFalseRule.getProductName()), "Ответ API не содержит alwaysFalseRule");
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
        DynamicRuleDto ruleToDelete = getTestProduct();
        ruleToDelete.setProductId(UUID.randomUUID());
        dynamicRuleService.saveDynamicRule(ruleToDelete);

        String url = "http://localhost:" + port + "/rule/" + ruleToDelete.getProductId();

        ResponseEntity<DynamicRuleDto[]> responseBeforeDelete = restTemplate.getForEntity("http://localhost:" + port + "/rule", DynamicRuleDto[].class);
        boolean existsBeforeDelete = Arrays.stream(responseBeforeDelete.getBody())
                .anyMatch(rule -> rule.getProductId().equals(ruleToDelete.getProductId()));
        assertTrue(existsBeforeDelete, "Rule should exist before deletion");

        ResponseEntity<Void> responseEntity = restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.NO_CONTENT));

        ResponseEntity<DynamicRuleDto[]> responseAfterDelete = restTemplate.getForEntity("http://localhost:" + port + "/rule", DynamicRuleDto[].class);
        boolean existsAfterDelete = Arrays.stream(responseAfterDelete.getBody())
                .anyMatch(rule -> rule.getProductId().equals(ruleToDelete.getProductId()));
        assertFalse(existsAfterDelete, "Rule should be deleted");
    }

    @Test
    void test_createDynamicRule_badRequest() {
        String url = "http://localhost:" + port + "/rule";

        ResponseEntity<DynamicRuleDto> responseEntity = restTemplate
                .postForEntity(url, null, DynamicRuleDto.class);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    void test_createDynamicRule_invalidProductName() {
        DynamicRuleDto dynamicRuleDto = new DynamicRuleDto();
        dynamicRuleDto.setProductName(null);

        String url = "http://localhost:" + port + "/rule";

        ResponseEntity<String> responseEntity = restTemplate
                .postForEntity(url, dynamicRuleDto, String.class);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    void test_createDynamicRule_invalidJsonFormat() {
        String invalidJson = "{" +
                "\"product_id\": \"not a uuid\"," +
                "\"product_name\": \"Some product name\"," +
                "\"product_text\": \"Some product text\"," +
                "\"rule\": []" +
                "}";

        String url = "http://localhost:" + port + "/rule";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(invalidJson, headers);

        ResponseEntity<String> responseEntity = restTemplate
                .postForEntity(url, entity, String.class);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        Assertions.assertNotNull(responseEntity.getBody(), "Response body should not be null");
        Assertions.assertTrue(responseEntity.getBody().contains("error"), "Error message should contain 'error'");
    }
}