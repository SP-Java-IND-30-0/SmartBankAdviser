package com.star.bank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.star.bank.model.dto.PersonalRecommendationDto;
import com.star.bank.model.product.*;
import com.star.bank.repositories.DynamicRuleRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
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

    @Autowired
    private DynamicRuleRepository dynamicRuleRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private TestRestTemplate restTemplate;

    private static final DynamicRule alwaysFalseRule = getAlwaysFalseProduct();
    private static final DynamicRule alwaysTrueRule = getAlwaysTrueProduct();
    private static final DynamicRule testRule = getTestProduct();
    private static Product staticTopSavingRule;
    private static Product staticInvest500Product;
    private static Product staticSimpleCreditProduct;

    private static final List<TestUser> testUsers = new ArrayList<>();

    @Container
    static PostgreSQLContainer<?> cont = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("test_db")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void properties(final DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", cont::getJdbcUrl);
        registry.add("spring.datasource.username", cont::getUsername);
        registry.add("spring.datasource.password", cont::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop"); // Убедитесь, что схема создается
        registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.PostgreSQLDialect");
    }

    @BeforeAll
    static void beforeAll(@Autowired Invest500Product invest500Product,
                          @Autowired SimpleCreditProduct simpleCreditProduct,
                          @Autowired TopSavingProduct topSavingProduct) {
        cont.start();
        staticInvest500Product = invest500Product;
        staticSimpleCreditProduct = simpleCreditProduct;
        staticTopSavingRule = topSavingProduct;
    }

    @BeforeEach
    void setUp() {

        restTemplate = new TestRestTemplate();

        restTemplate.getRestTemplate().getMessageConverters().forEach(converter -> {
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                ((MappingJackson2HttpMessageConverter) converter).setObjectMapper(objectMapper);
            }
        });

        restTemplate.getRestTemplate().setRequestFactory(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));

        dynamicRuleRepository.deleteAll();
        dynamicRuleRepository.flush();

        dynamicRuleRepository.saveAll(List.of(alwaysTrueRule, alwaysFalseRule, testRule));

    }

    static Stream<TestUser> provideDataForGetRecommendationByUserId() {
        testUsers.add(new TestUser(
                UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d"), "quintin.deckow",
                "Rolf", "Bogisich",
                Set.of(alwaysTrueRule, staticInvest500Product, staticTopSavingRule)));
        testUsers.add(new TestUser(
                UUID.fromString("1f9b149c-6577-448a-bc94-16bea229b71a"), "ramon.cassin",
                "Minna", "Sawayn",
                Set.of(alwaysTrueRule, staticSimpleCreditProduct, staticTopSavingRule)));
        testUsers.add(new TestUser(
                UUID.fromString("8a31d91d-87a4-4441-8ca0-88773064f364"), "cole.kuvalis",
                "Jacelyn", "Feeney",
                Set.of(alwaysTrueRule, staticTopSavingRule)));
        testUsers.add(new TestUser(
                UUID.fromString("a796f0c6-4bd5-4fb5-bd84-02cfcb381d57"), "randa.hessel",
                "Janise", "Braun",
                Set.of(alwaysTrueRule, testRule)));
        testUsers.add(new TestUser(
                UUID.fromString("78f80518-60ae-4d25-9c70-45981083f12e"), "sheila.wolff",
                "Letitia", "Simonis",
                Set.of(alwaysTrueRule, testRule, staticTopSavingRule)));
        testUsers.add(new TestUser(
                UUID.fromString("7ed37e4a-fefe-472e-8745-a7749e60c218"), "keneth.monahan",
                "Ana", "Mayer",
                Set.of(alwaysTrueRule)));
        return testUsers.stream();
    }


    @ParameterizedTest
    @MethodSource("provideDataForGetRecommendationByUserId")
    void test_getUserId_success(TestUser testUser) {
        String userId = testUser.id().toString();
        String url = "http://localhost:" + port + "/recommendation/" + userId;

        ResponseEntity<PersonalRecommendationDto> responseEntity = restTemplate
                .getForEntity(url, PersonalRecommendationDto.class);

        PersonalRecommendationDto actual = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertNotNull(actual);
        assertEquals(userId, actual.getUserId());
        assertEquals(testUser.products().size(), actual.getRecommendations().size());
        assertFalse(actual.getRecommendations().contains(alwaysFalseRule));

    }

//    @Test
//    void getUsername() {
//    }
}