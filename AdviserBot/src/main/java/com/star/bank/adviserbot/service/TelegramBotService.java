package com.star.bank.adviserbot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TelegramBotService {

    private final BotService botService;
    private TelegramBot bot;

    @Value("${telegram.bot.token}")
    private String token;

    public TelegramBotService(BotService botService) {
        this.botService = botService;
    }
    @PostConstruct
    public void init() {
        this.bot = new TelegramBot(token);
        System.out.println("\"Бот запущен\" = " + "Бот запущен");
        startBot();
    }

    private void startBot() {
        new Thread(() -> {
            while (true) {
                try {
                    GetUpdates getUpdates = new GetUpdates().limit(100).offset(0);
                    var response = bot.execute(getUpdates);

                    if (response != null && response.updates() != null) {
                        for (Update update : response.updates()) {
                            if (update.message() != null) {
                                handleUpdate(update.message());
                            }
                        }
                    }
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void handleUpdate(Message message) {
        String text = message.text();
        long chatId = message.chat().id();

        if (text != null) {
            if (isValidNameFormat(text)) {
                String greeting = "Здравствуйте, " + text;
                bot.execute(new SendMessage(chatId, greeting));

                String recommendation = botService.getRecommendation(text);
                bot.execute(new SendMessage(chatId, recommendation));
            } else {
                bot.execute(new SendMessage(chatId, "Неверный формат данных. Пожалуйста, введите ФИО в формате: Иванов Иван Иванович или Иванов Иван."));
            }
        }
    }

    private boolean isValidNameFormat(String username) {
        return username.matches("^[А-Яа-яЁё]+\\s[А-Яа-яЁё]+(\\s[А-Яа-яЁё]+)?$");
    }
}