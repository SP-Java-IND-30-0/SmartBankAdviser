package com.star.bank.adviserbot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class TelegramBotService {

    private final BotService botService;
    private TelegramBot bot;
    private final Set<Long> processedMessages = new HashSet<>();
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
                                handleUpdate(update);
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

    public void handleUpdate(Update update) {
        if (update.message() == null) return;

        Message message = update.message();
        long chatId = message.chat().id();
        long messageId = message.messageId();

        if (processedMessages.contains(messageId)) return;
        processedMessages.add(messageId);

        String text = message.text();
        if (text == null || !isValidNameFormat(text)) {
            bot.execute(new SendMessage(chatId, "Неверный формат данных. Введите: Иванов Иван."));
            return;
        }

        String[] parts = text.split("\\s+");
        String firstName = parts[0];
        String lastName = parts[1];

        String response = botService.getRecommendation(firstName, lastName);
        bot.execute(new SendMessage(chatId, response));
    }

    private boolean isValidNameFormat(String username) {
        return username.matches("^[А-Яа-яЁё]+\\s[А-Яа-яЁё]+(\\s[А-Яа-яЁё]+)?$");
    }
}