package com.star.bank.adviserbot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import com.star.bank.model.dto.PersonalRecommendationTgDto;
import com.star.bank.model.product.Product;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;


@Service
public class RecommendationBot {
    private static final Logger logger = LoggerFactory.getLogger(RecommendationBot.class);

    private TelegramBot bot;
    private RestTemplate restTemplate;

    @Value("${telegram.bot.token}")
    private String token;

    @Value("${main.app.url}")
    private String mainAppUrl;

    public RecommendationBot(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    public void init() {
        this.bot = new TelegramBot(token);
        logger.info("Бот запущен");
        startBot();
    }

    private void startBot() {
        bot.setUpdatesListener(updates -> {
            for (Update update : updates) {
                if (update.message() != null && update.message().text() != null) {
                    handleUpdate(update);
                }
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private void handleUpdate(Update update) {
        String text = update.message().text();
        long chatId = update.message().chat().id();

        logger.info("Получено сообщение: {}", text);

        if (text.equals("/start")) {
            sendMessage(chatId, "Привет! Для получения рекомендаций по банковским продуктам, используйте команду /recommend Имя Фамилия!");
        } else if (text.startsWith("/recommend ")) {
            handleRecommendCommand(chatId, text);
        }
    }

    private void handleRecommendCommand(long chatId, String text) {
        String[] parts = text.split(" ", 3);
        if (parts.length < 3 || !isValidName(parts[1]) || !isValidName(parts[2])) {
            logger.warn("Неверный формат данных: {}", text);
            sendMessage(chatId, "Неверный формат данных. Используйте команду в формате: /recommend Имя Фамилия");
            return;
        }

        String firstName = parts[1];
        String lastName = parts[2];

        logger.info("Запрос на рекомендации для: {} {}", firstName, lastName);

        PersonalRecommendationTgDto response = getRecommendationsFromBank(firstName, lastName);
        if (response != null) {
            sendRecommendations(chatId, response);
        } else {
            sendMessage(chatId, "Сервис временно недоступен. Пожалуйста, попробуйте позже");
        }
    }

    private PersonalRecommendationTgDto getRecommendationsFromBank(String firstName, String lastName) {
        String url = String.format("%s/recommendation/username/%s", mainAppUrl, firstName);

        logger.info("Запрос к сервису по URL: {}", url);

        try {
            return restTemplate.getForObject(url, PersonalRecommendationTgDto.class);
        } catch (RestClientException e) {
            logger.error("Ошибка при обращении к сервису: {}", e.getMessage());

            if (e.getCause() instanceof HttpClientErrorException.NotFound) {
                logger.warn("Пользователь не найден: {}", firstName, lastName);
                return null;
            }
            return null;
        }
    }

    private void sendRecommendations(long chatId, PersonalRecommendationTgDto recommendation) {

        logger.info("Отправка рекомендаций для пользователя: {} {}", recommendation.getFirstName(), recommendation.getLastName());

        StringBuilder messageText = new StringBuilder();
        messageText.append("Здравствуйте, ").append(recommendation.getFirstName()).append(" ").append(recommendation.getLastName()).append("\n\n");
        messageText.append("Новые продукты для вас:\n");

        if (recommendation.getRecommendations().isEmpty()) {
            messageText.append("Нет новых продуктов для вас.");
        } else {
            for (Product product : recommendation.getRecommendations()) {
                messageText.append("- ").append(product.getName()).append("\n");
                messageText.append(product.getText()).append("\n\n");
            }
        }

        sendMessage(chatId, messageText.toString());
    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage(chatId, text);
        SendResponse response = bot.execute(message);
        if (!response.isOk()) {
            logger.error("Ошибка при отправке сообщения: {}", response.description());
        }
    }

    private boolean isValidName(String name) {
        return name.matches("[A-Za-zА-Яа-яЁё]+");
    }
}
