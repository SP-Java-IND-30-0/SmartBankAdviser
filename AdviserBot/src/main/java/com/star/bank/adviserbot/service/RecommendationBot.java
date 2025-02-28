package com.star.bank.adviserbot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import com.star.bank.adviserbot.dto.PersonalRecommendationTgDto;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


@Service
public class RecommendationBot {
    private static final Logger LOGGER = LoggerFactory.getLogger(RecommendationBot.class);
    private static final String RECOMMEND_COMMAND = "/recommend ";

    private TelegramBot bot;
    private final RestTemplate restTemplate;

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
        LOGGER.info("Бот запущен");
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

        LOGGER.info("Получено сообщение: {}", text);

        if (text.equals("/start")) {
            sendMessage(chatId, "Привет! Для получения рекомендаций по банковским продуктам, используйте команду /recommend Имя Фамилия!");
        } else if (text.startsWith(RECOMMEND_COMMAND)) {
            handleRecommendCommand(chatId, text);
        }
    }

    private void handleRecommendCommand(long chatId, String text) {

        if (!text.startsWith(RECOMMEND_COMMAND) || isInvalidName(text.substring(RECOMMEND_COMMAND.length()))) {
            LOGGER.warn("Неверный формат данных: {}", text);
            sendMessage(chatId, "Неверный формат данных. Используйте команду в формате: /recommend Имя Фамилия");
            return;
        }


        String username = text.substring(RECOMMEND_COMMAND.length());
        LOGGER.info("Запрос на рекомендации для: {}", username);

        sendMessage(chatId, getRecommendationsFromBank(username));
    }

    private String getRecommendationsFromBank(String username) {
        String url = String.format("%s/recommendation/username/%s", mainAppUrl, username);

        LOGGER.info("Запрос к сервису по URL: {}", url);

        try {
            return sendRecommendations(restTemplate.getForObject(url, PersonalRecommendationTgDto.class));
        } catch (RestClientException | NullPointerException e) {
            LOGGER.error("Ошибка при обращении к сервису: {}", e.getMessage());

            if (e instanceof HttpClientErrorException.NotFound) {
                LOGGER.warn("Пользователь не найден: {}", username);
                return String.format("Пользователь %s не найден", username);
            }
            return "Ошибка при обращении к сервису. Пожалуйста, попробуйте позже.";
        }
    }

    private String sendRecommendations(PersonalRecommendationTgDto recommendation) {

        LOGGER.info("Отправка рекомендаций для пользователя: {} {}", recommendation.firstName(), recommendation.lastName());

        StringBuilder messageText = new StringBuilder();
        messageText.append("Здравствуйте, ").append(recommendation.firstName()).append(" ").append(recommendation.lastName()).append("\n\n");

        if (recommendation.recommendations().isEmpty()) {
            messageText.append("Нет новых продуктов для вас.");
        } else {
            messageText.append("Новые продукты для вас:\n");
            for (PersonalRecommendationTgDto.Product product : recommendation.recommendations()) {
                messageText.append("- ").append(product.name()).append("\n");
                messageText.append(product.text()).append("\n\n");
            }
        }
        return messageText.toString();
    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage(chatId, text);
        SendResponse response = bot.execute(message);
        if (!response.isOk()) {
            LOGGER.error("Ошибка при отправке сообщения: {}", response.description());
        }
    }

    private boolean isInvalidName(String name) {
        return !name.matches("^[a-z]+\\.[a-z]+$");
    }
}
