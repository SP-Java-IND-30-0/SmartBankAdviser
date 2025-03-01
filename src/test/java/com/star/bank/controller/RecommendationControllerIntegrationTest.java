package com.star.bank.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.star.bank.mapper.DynamicRuleMapper;
import com.star.bank.model.dto.DynamicRuleDto;
import com.star.bank.model.product.Invest500Product;
import com.star.bank.model.product.Product;
import com.star.bank.model.product.SimpleCreditProduct;
import com.star.bank.model.product.TopSavingProduct;
import com.star.bank.service.DynamicRuleService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.*;
import java.util.stream.Stream;

import static com.star.bank.TestDynamicRule.*;
import static com.star.bank.TestUtils.TestUser;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
class RecommendationControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private final TestRestTemplate restTemplate = new TestRestTemplate();

    private static Product staticAlwaysFalseRule;
    private static Product staticAlwaysTrueRule;
    private static Product staticTestRule;
    private static Product staticTopSavingRule;
    private static Product staticInvest500Product;
    private static Product staticSimpleCreditProduct;

    @Container
    static PostgreSQLContainer<?> cont = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("test_db")
            .withUsername("test")
            .withPassword("test");

    @BeforeAll
    static void init(@Autowired DynamicRuleMapper mapper,
                     @Autowired DynamicRuleService service,
                     @Autowired Invest500Product invest500Product,
                     @Autowired SimpleCreditProduct simpleCreditProduct,
                     @Autowired TopSavingProduct topSavingProduct) {

        cont.start();

        DynamicRuleDto alwaysTrueRule = getAlwaysTrueProduct();
        DynamicRuleDto testRule = getTestProduct();
        DynamicRuleDto alwaysFalseRule = getAlwaysFalseProduct();

        service.saveDynamicRule(alwaysTrueRule);
        service.saveDynamicRule(alwaysFalseRule);
        service.saveDynamicRule(testRule);

        staticAlwaysTrueRule = mapper.toEntity(alwaysTrueRule);
        staticAlwaysFalseRule = mapper.toEntity(alwaysFalseRule);
        staticTestRule = mapper.toEntity(testRule);
        staticTopSavingRule = topSavingProduct;
        staticInvest500Product = invest500Product;
        staticSimpleCreditProduct = simpleCreditProduct;

    }

    @DynamicPropertySource
    static void properties(final DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", cont::getJdbcUrl);
        registry.add("spring.datasource.username", cont::getUsername);
        registry.add("spring.datasource.password", cont::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop"); // Убедитесь, что схема создается
        registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.PostgreSQLDialect");
    }


    static Stream<TestUser> provideDataForGetRecommendation() {
        List<TestUser> testUsers = new ArrayList<>();
        testUsers.add(new TestUser(
                UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d"), "quintin.deckow",
                "Rolf", "Bogisich",
                Set.of(staticAlwaysTrueRule, staticInvest500Product, staticTopSavingRule)));
        testUsers.add(new TestUser(
                UUID.fromString("1f9b149c-6577-448a-bc94-16bea229b71a"), "ramon.cassin",
                "Minna", "Sawayn",
                Set.of(staticAlwaysTrueRule, staticSimpleCreditProduct, staticTopSavingRule)));
        testUsers.add(new TestUser(
                UUID.fromString("8a31d91d-87a4-4441-8ca0-88773064f364"), "cole.kuvalis",
                "Jacelyn", "Feeney",
                Set.of(staticAlwaysTrueRule, staticTopSavingRule)));
        testUsers.add(new TestUser(
                UUID.fromString("a796f0c6-4bd5-4fb5-bd84-02cfcb381d57"), "randa.hessel",
                "Janise", "Braun",
                Set.of(staticAlwaysTrueRule, staticTestRule)));
        testUsers.add(new TestUser(
                UUID.fromString("78f80518-60ae-4d25-9c70-45981083f12e"), "sheila.wolff",
                "Letitia", "Simonis",
                Set.of(staticAlwaysTrueRule, staticTestRule, staticTopSavingRule)));
        testUsers.add(new TestUser(
                UUID.fromString("7ed37e4a-fefe-472e-8745-a7749e60c218"), "keneth.monahan",
                "Ana", "Mayer",
                Set.of(staticAlwaysTrueRule)));
        return testUsers.stream();
    }


    @ParameterizedTest
    @MethodSource("provideDataForGetRecommendation")
    void test_getUserId_success(TestUser testUser) throws JsonProcessingException {
        String userId = testUser.id().toString();
        String url = "http://localhost:" + port + "/recommendation/" + userId;

        ResponseEntity<String> responseEntity = restTemplate
                .getForEntity(url, String.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        ObjectMapper tempMapper = new ObjectMapper();
        JsonNode recommendationNode = tempMapper.readTree(responseEntity.getBody()).path("recommendations");
        assertTrue(recommendationNode.isArray());
        assertEquals(testUser.products().size(), recommendationNode.size());
        for (Product product : testUser.products()) {
            assertTrue(Stream.of(recommendationNode).anyMatch(node -> node.toString().contains(product.getId())));
        }
        assertFalse(Stream.of(recommendationNode).toString().contains(staticAlwaysFalseRule.getId()));
        assertEquals(userId, tempMapper.readTree(responseEntity.getBody()).path("userId").asText());
    }

    @ParameterizedTest
    @MethodSource("provideDataForGetRecommendation")
    void test_getUsername_success(TestUser testUser) throws JsonProcessingException {
        String username = testUser.username();
        String url = "http://localhost:" + port + "/recommendation/username/" + username;
        ResponseEntity<String> responseEntity = restTemplate
                .getForEntity(url, String.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        ObjectMapper tempMapper = new ObjectMapper();
        JsonNode recommendationNode = tempMapper.readTree(responseEntity.getBody()).path("recommendations");
        assertTrue(recommendationNode.isArray());
        assertEquals(testUser.products().size(), recommendationNode.size());
        for (Product product : testUser.products()) {
            assertTrue(Stream.of(recommendationNode).anyMatch(node -> node.toString().contains(product.getId())));
        }
        assertFalse(Stream.of(recommendationNode).toString().contains(staticAlwaysFalseRule.getId()));
        assertEquals(testUser.firstName(), tempMapper.readTree(responseEntity.getBody()).path("firstName").asText());
        assertEquals(testUser.lastName(), tempMapper.readTree(responseEntity.getBody()).path("lastName").asText());

    }

    @Test
    void test_getUserId_notFound() {
        String userId = UUID.randomUUID().toString();
        String url = "http://localhost:" + port + "/recommendation/" + userId;

        ResponseEntity<String> responseEntity = restTemplate
                .getForEntity(url, String.class);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void test_getUsername_notFound() {
        String username = UUID.randomUUID().toString();
        String url = "http://localhost:" + port + "/recommendation/username/" + username;

        ResponseEntity<String> responseEntity = restTemplate
                .getForEntity(url, String.class);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void test_getUserId_badRequest() {
        String userId = "invalid";
        String url = "http://localhost:" + port + "/recommendation/" + userId;

        ResponseEntity<String> responseEntity = restTemplate
                .getForEntity(url, String.class);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void test_getStats() throws JsonProcessingException {
        String url = "http://localhost:" + port + "/recommendation/";
        String urlStats = "http://localhost:" + port + "/rule/stats";

        provideDataForGetRecommendation().forEach(testUser -> {
            String userId = testUser.id().toString();
            restTemplate
                    .getForEntity(url + userId, String.class);
        });

        Map<String, Integer> expectedStats = new HashMap<>();
        expectedStats.put(staticAlwaysTrueRule.getId(), 6);
        expectedStats.put(staticTestRule.getId(), 2);
        expectedStats.put(staticAlwaysFalseRule.getId(), 0);
        expectedStats.put(staticTopSavingRule.getId(), 4);
        expectedStats.put(staticInvest500Product.getId(), 1);
        expectedStats.put(staticSimpleCreditProduct.getId(), 1);

        ResponseEntity<String> responseEntity = restTemplate
                .getForEntity(urlStats, String.class);


        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        ObjectMapper tempMapper = new ObjectMapper();
        JsonNode statsNode = tempMapper.readTree(responseEntity.getBody()).path("stats");

        assertTrue(statsNode.isArray());
        assertEquals(expectedStats.size(), statsNode.size());

        for (JsonNode statNode : statsNode) {
            String ruleId = statNode.path("rule_id").asText();
            int count = statNode.path("count").asInt();
            assertEquals(expectedStats.get(ruleId), count);
        }

    }
}