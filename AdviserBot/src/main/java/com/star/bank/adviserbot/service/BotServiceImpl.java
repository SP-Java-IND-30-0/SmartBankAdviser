package com.star.bank.adviserbot.service;

import com.star.bank.model.dto.PersonalRecommendationTgDto;
import com.star.bank.model.dto.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import java.util.stream.Collectors;

@Service
public class BotServiceImpl implements BotService {

    private final RestTemplate restTemplate;

    @Value("${main.app.url}")
    private String mainApp;

    public BotServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String getRecommendation(String firstName, String lastName) {
        String url = String.format("%s/users/find?firstName=%s&lastName=%s", mainApp, firstName, lastName);

        try {
            ResponseEntity<UserDto> response = restTemplate.getForEntity(url, UserDto.class);
            UserDto user = response.getBody();

            if (user == null) {
                return "❌ Пользователь не найден. Проверьте корректность данных.";
            }


            String recommendationsUrl = String.format("%s/recommendation/%s", mainApp, user.getId());
            ResponseEntity<PersonalRecommendationTgDto> recResponse = restTemplate.getForEntity(recommendationsUrl, PersonalRecommendationTgDto.class);

            PersonalRecommendationTgDto recommendations = recResponse.getBody();
            if (recommendations == null || recommendations.getRecommendations().isEmpty()) {
                return String.format("👋 Здравствуйте, %s %s!\n\nПока для вас нет персональных рекомендаций. Попробуйте позже.",
                        user.getFirstName(), user.getLastName());
            }

            String productList = recommendations.getRecommendations().stream()
                    .map(product -> String.format("%s - %s\n", product.getName(), product.getText()))
                    .collect(Collectors.joining("\n"));

            return String.format("👋 Здравствуйте, %s %s!\n\n🎯 Вам могут подойти:\n%s",
                    user.getFirstName(), user.getLastName(), productList);

        } catch (HttpClientErrorException.NotFound e) {
            return "❌ Пользователь не найден. Проверьте корректность данных.";
        } catch (RestClientException e) {
            return "⚠️ Сервис временно недоступен, попробуйте позже.";
        }
    }

}
