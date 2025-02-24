package com.star.bank.adviserbot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

@Service
public class BotServiceImpl implements BotService {

    private final RestTemplate restTemplate;

    @Value("${main.app.url}")
    private String mainApp;

    public BotServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String getRecommendation(String username) {
        String url = String.format("%s/recommendations/%s", mainApp, username);

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return response.getBody();

        } catch (HttpClientErrorException e) {
            return "Пользователь не найден";

        } catch (RestClientException e) {
            return "Сервис временно недоступен, попробуйте позже.";
        }
    }
}
