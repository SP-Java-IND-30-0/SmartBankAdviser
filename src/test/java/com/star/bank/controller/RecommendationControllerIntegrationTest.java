package com.star.bank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.star.bank.model.dto.PersonalRecommendationDto;
import com.star.bank.service.RecommendationService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
class RecommendationControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    RecommendationController recommendationController;

    @Autowired
    RecommendationService recommendationService;

    @Autowired
    ObjectMapper objectMapper;

    TestRestTemplate restTemplate;

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
    static void beforeAll() {
        cont.start();
    }

    @BeforeEach
    void setUp() {
        restTemplate = new TestRestTemplate();

        restTemplate.getRestTemplate().getMessageConverters().forEach(converter -> {
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                ((MappingJackson2HttpMessageConverter) converter).setObjectMapper(objectMapper);
            }
        });
    }


    @Test
    void test_getUserId() {
        String userId = "cd515076-5d8a-44be-930e-8d4fcb79f42d";

        ResponseEntity<PersonalRecommendationDto> responseEntity = restTemplate
                .getForEntity("http://localhost:" + port + "/recommendation/" + userId, PersonalRecommendationDto.class);

        PersonalRecommendationDto actual = responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertNotNull(actual);
        assertEquals(userId, actual.getUserId());
        assertEquals(2, actual.getRecommendations().size());

    }

    @Test
    void getUsername() {
    }
}