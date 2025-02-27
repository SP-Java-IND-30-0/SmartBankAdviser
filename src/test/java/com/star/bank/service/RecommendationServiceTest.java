package com.star.bank.service;


import com.star.bank.MockProduct;
import com.star.bank.exception.UserNotFoundException;
import com.star.bank.exception.UsernameNotFoundException;
import com.star.bank.mapper.DynamicRuleMapper;
import com.star.bank.model.dto.DynamicRuleDto;
import com.star.bank.model.dto.PersonalRecommendationDto;
import com.star.bank.model.dto.PersonalRecommendationTgDto;
import com.star.bank.model.dto.UserDto;
import com.star.bank.model.product.DynamicRule;
import com.star.bank.model.product.Product;
import com.star.bank.repositories.RecommendationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.star.bank.TestUtils.createTestDynamicRule;
import static com.star.bank.TestUtils.createTestDynamicRuleDto;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecommendationServiceTest {

    @Mock
    private RecommendationRepository recommendationRepository;
    @Mock
    private DynamicRuleService dynamicRuleService;
    @Mock
    private DynamicRuleMapper dynamicRuleMapper;
    @Mock
    private ApplicationEventPublisher eventPublisher;

    private final Set<Product> products = new HashSet<>();

    private RecommendationService service;


    @BeforeEach
    void info() {
        service = new RecommendationService(
                recommendationRepository, dynamicRuleService, dynamicRuleMapper, eventPublisher, products
        );
    }

    @Test
    void test_sendRecommendation_successWithRecommendations() {
        String userId = UUID.randomUUID().toString();
        Product firstProduct = new MockProduct("1", " TestProduct 1", "Description for product 1", "firstProduct");
        Product secondProduct = new MockProduct("2", " TestProduct 2", "Description for product 2", "secondProduct");
        service.getProducts().add(firstProduct);
        service.getProducts().add(secondProduct);

        DynamicRuleDto mockDynamicRuleDto = createTestDynamicRuleDto()
                .id(UUID.randomUUID()).name("TestDynamicRule1").text("TestDynamicRule1").build();
        DynamicRule mockDynamicRule = createTestDynamicRule()
                .id(mockDynamicRuleDto.getProductId()).name("TestDynamicRule1").text("TestDynamicRule1").build();

        when(recommendationRepository.isUserExist(userId)).thenReturn(true);

        when(recommendationRepository.checkProductRules(userId, "firstProduct")).thenReturn(true);
        when(recommendationRepository.checkProductRules(userId, "secondProduct")).thenReturn(false);
        when(recommendationRepository.checkProductRules(userId, mockDynamicRule.getQuery())).thenReturn(true);
        when(dynamicRuleService.getDynamicRules()).thenReturn(List.of(mockDynamicRuleDto));
        when(dynamicRuleMapper.toEntity(mockDynamicRuleDto)).thenReturn(mockDynamicRule);
        doNothing().when(eventPublisher).publishEvent(any());

        CompletableFuture<PersonalRecommendationDto> futureRecommendationDto = service.sendRecommendation(userId);

        PersonalRecommendationDto recommendationDto = futureRecommendationDto.join();

        assertEquals(3,service.getProducts().size());

        assertEquals(2, recommendationDto.getRecommendations().size());
        assertEquals(userId, recommendationDto.getUserId());
        assertTrue(recommendationDto.getRecommendations().contains(firstProduct));
        assertFalse(recommendationDto.getRecommendations().contains(secondProduct));
        assertTrue(recommendationDto.getRecommendations().contains(mockDynamicRule));

        verify(recommendationRepository, times(1)).checkProductRules(userId, "firstProduct");
        verify(recommendationRepository, times(1)).checkProductRules(userId, "secondProduct");
        verify(recommendationRepository, times(1)).checkProductRules(userId, mockDynamicRule.getQuery());
        verifyNoMoreInteractions(recommendationRepository);

        verify(eventPublisher, times(2)).publishEvent(any());
    }

    @Test
    void test_sendRecommendation_successWithEmptyList() {
        String userId = UUID.randomUUID().toString();
        Product firstProduct = new MockProduct("1", " TestProduct 1", "Description for product 1", "firstProduct");
        Product secondProduct = new MockProduct("2", " TestProduct 2", "Description for product 2", "secondProduct");
        service.getProducts().add(firstProduct);
        service.getProducts().add(secondProduct);

        DynamicRuleDto mockDynamicRuleDto = createTestDynamicRuleDto()
                .id(UUID.randomUUID()).name("TestDynamicRule1").text("TestDynamicRule1").build();
        DynamicRule mockDynamicRule = createTestDynamicRule()
                .id(mockDynamicRuleDto.getProductId()).name("TestDynamicRule1").text("TestDynamicRule1").build();

        when(recommendationRepository.isUserExist(userId)).thenReturn(true);

        when(recommendationRepository.checkProductRules(userId, "firstProduct")).thenReturn(false);
        when(recommendationRepository.checkProductRules(userId, "secondProduct")).thenReturn(false);
        when(recommendationRepository.checkProductRules(userId, mockDynamicRule.getQuery())).thenReturn(false);
        when(dynamicRuleService.getDynamicRules()).thenReturn(List.of(mockDynamicRuleDto));
        when(dynamicRuleMapper.toEntity(mockDynamicRuleDto)).thenReturn(mockDynamicRule);

        CompletableFuture<PersonalRecommendationDto> futureRecommendationDto = service.sendRecommendation(userId);

        PersonalRecommendationDto recommendationDto = futureRecommendationDto.join();

        assertEquals(3,service.getProducts().size());

        assertTrue(recommendationDto.getRecommendations().isEmpty());
        assertEquals(userId, recommendationDto.getUserId());

        verify(recommendationRepository, times(1)).checkProductRules(userId, "firstProduct");
        verify(recommendationRepository, times(1)).checkProductRules(userId, "secondProduct");
        verify(recommendationRepository, times(1)).checkProductRules(userId, mockDynamicRule.getQuery());
        verifyNoMoreInteractions(recommendationRepository);

        verify(eventPublisher, times(0)).publishEvent(any());
    }


    @Test
    void test_sendRecommendation_whenIncorrectUserId() {
        String userId = "IncorrectUserId";

        assertThrows(UserNotFoundException.class, () -> service.sendRecommendation(userId));

        verify(recommendationRepository, times(0)).checkProductRules(anyString(), anyString());

        verify(eventPublisher, times(0)).publishEvent(any());
    }

    @Test
    void test_sendRecommendation_whenUserNotExist() {
        String userId = UUID.randomUUID().toString();

        when(recommendationRepository.isUserExist(userId)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> service.sendRecommendation(userId));

        verify(recommendationRepository, times(0)).checkProductRules(anyString(), anyString());

        verify(eventPublisher, times(0)).publishEvent(any());


    }


    @Test
    void test_sendRecommendationTg_successWithRecommendations() {

        UserDto user1 = UserDto.builder()
                .id(UUID.randomUUID().toString())
                .username("User1")
                .firstName("FirstName1")
                .lastName("LastName1")
                .build();

        Product firstProduct = new MockProduct("1", " TestProduct 1", "Description for product 1", "firstProduct");
        Product secondProduct = new MockProduct("2", " TestProduct 2", "Description for product 2", "secondProduct");
        service.getProducts().add(firstProduct);
        service.getProducts().add(secondProduct);

        DynamicRuleDto mockDynamicRuleDto = createTestDynamicRuleDto()
                .id(UUID.randomUUID()).name("TestDynamicRule1").text("TestDynamicRule1").build();
        DynamicRule mockDynamicRule = createTestDynamicRule()
                .id(mockDynamicRuleDto.getProductId()).name("TestDynamicRule1").text("TestDynamicRule1").build();


        when(recommendationRepository.getUser("User1")).thenReturn(List.of(user1));

        when(recommendationRepository.checkProductRules(user1.getId(), "firstProduct")).thenReturn(true);
        when(recommendationRepository.checkProductRules(user1.getId(), "secondProduct")).thenReturn(false);
        when(recommendationRepository.checkProductRules(user1.getId(), mockDynamicRule.getQuery())).thenReturn(true);
        when(dynamicRuleService.getDynamicRules()).thenReturn(List.of(mockDynamicRuleDto));
        when(dynamicRuleMapper.toEntity(mockDynamicRuleDto)).thenReturn(mockDynamicRule);
        doNothing().when(eventPublisher).publishEvent(any());

        CompletableFuture<PersonalRecommendationTgDto> futureRecommendationDto = service.sendRecommendationTg("User1");

        PersonalRecommendationTgDto recommendationDto = futureRecommendationDto.join();

        assertEquals(3,service.getProducts().size());

        assertEquals(2, recommendationDto.getRecommendations().size());
        assertEquals(user1.getFirstName(), recommendationDto.getFirstName());
        assertEquals(user1.getLastName(), recommendationDto.getLastName());
        assertTrue(recommendationDto.getRecommendations().contains(firstProduct));
        assertFalse(recommendationDto.getRecommendations().contains(secondProduct));
        assertTrue(recommendationDto.getRecommendations().contains(mockDynamicRule));

        verify(recommendationRepository, times(1)).checkProductRules(user1.getId(), "firstProduct");
        verify(recommendationRepository, times(1)).checkProductRules(user1.getId(), "secondProduct");
        verify(recommendationRepository, times(1)).checkProductRules(user1.getId(), mockDynamicRule.getQuery());
        verifyNoMoreInteractions(recommendationRepository);

        verify(eventPublisher, times(2)).publishEvent(any());
    }

    @Test
    void test_sendRecommendationTg_successWithEmptyRecommendations() {

        UserDto user1 = UserDto.builder()
                .id(UUID.randomUUID().toString())
                .username("User1")
                .firstName("FirstName1")
                .lastName("LastName1")
                .build();

        Product firstProduct = new MockProduct("1", " TestProduct 1", "Description for product 1", "firstProduct");
        Product secondProduct = new MockProduct("2", " TestProduct 2", "Description for product 2", "secondProduct");
        service.getProducts().add(firstProduct);
        service.getProducts().add(secondProduct);

        DynamicRuleDto mockDynamicRuleDto = createTestDynamicRuleDto()
                .id(UUID.randomUUID()).name("TestDynamicRule1").text("TestDynamicRule1").build();
        DynamicRule mockDynamicRule = createTestDynamicRule()
                .id(mockDynamicRuleDto.getProductId()).name("TestDynamicRule1").text("TestDynamicRule1").build();


        when(recommendationRepository.getUser("User1")).thenReturn(List.of(user1));

        when(recommendationRepository.checkProductRules(user1.getId(), "firstProduct")).thenReturn(false);
        when(recommendationRepository.checkProductRules(user1.getId(), "secondProduct")).thenReturn(false);
        when(recommendationRepository.checkProductRules(user1.getId(), mockDynamicRule.getQuery())).thenReturn(false);
        when(dynamicRuleService.getDynamicRules()).thenReturn(List.of(mockDynamicRuleDto));
        when(dynamicRuleMapper.toEntity(mockDynamicRuleDto)).thenReturn(mockDynamicRule);

        CompletableFuture<PersonalRecommendationTgDto> futureRecommendationDto = service.sendRecommendationTg("User1");

        PersonalRecommendationTgDto recommendationDto = futureRecommendationDto.join();

        assertEquals(3,service.getProducts().size());

        assertTrue(recommendationDto.getRecommendations().isEmpty());
        assertEquals(user1.getFirstName(), recommendationDto.getFirstName());
        assertEquals(user1.getLastName(), recommendationDto.getLastName());

        verify(recommendationRepository, times(1)).checkProductRules(user1.getId(), "firstProduct");
        verify(recommendationRepository, times(1)).checkProductRules(user1.getId(), "secondProduct");
        verify(recommendationRepository, times(1)).checkProductRules(user1.getId(), mockDynamicRule.getQuery());
        verifyNoMoreInteractions(recommendationRepository);

        verify(eventPublisher, times(0)).publishEvent(any());
    }

    @Test
    void test_sendRecommendationTg_whenFewUsersWithSomeUsername() {

        UserDto user1 = UserDto.builder()
                .id(UUID.randomUUID().toString())
                .username("User1")
                .firstName("FirstName1")
                .lastName("LastName1")
                .build();
        UserDto user2 = UserDto.builder()
                .id(UUID.randomUUID().toString())
                .username("User1")
                .firstName("FirstName2")
                .lastName("LastName2")
                .build();

        when(recommendationRepository.getUser("User1")).thenReturn(List.of(user1, user2));

        assertThrows(UsernameNotFoundException.class, () -> service.sendRecommendationTg("User1"));

        verify(recommendationRepository, times(0)).checkProductRules(anyString(), anyString());

        verify(eventPublisher, times(0)).publishEvent(any());
    }


    @Test
    void test_sendRecommendationTg_whenUserNotExist() {

        when(recommendationRepository.getUser("User1")).thenReturn(List.of());

        assertThrows(UsernameNotFoundException.class, () -> service.sendRecommendationTg("User1"));

        verify(recommendationRepository, times(0)).checkProductRules(anyString(), anyString());

        verify(eventPublisher, times(0)).publishEvent(any());
    }
}
