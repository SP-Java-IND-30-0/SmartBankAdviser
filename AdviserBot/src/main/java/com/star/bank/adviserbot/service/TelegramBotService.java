package com.star.bank.adviserbot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TelegramBotService {

    private final BotService botService;
    private final TelegramBot bot;

    @Value("${telegram.bot.token}")
    private String token;

    public TelegramBotService(BotService botService) {
        this.botService = botService;
        this.bot = new TelegramBot(token);
        startBot();
    }

    private void startBot() {
        new Thread(() -> {
            while (true) {
                try {
                    for (Update update : bot.execute(new GetUpdates().limit(100).offset(0)).updates()) {
                        if (update.message() != null) {
                            handleUpdate(update.message());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void handleUpdate(Message message) {
        String text = message.text();
        long chatId = message.chat().id();

        if (text != null && text.startsWith("/recommend ")) {
            String username = text.replace("/recommend ", "").trim();

            if (!isValidNameFormat(username)) {
                bot.execute(new SendMessage(chatId, "Неверный формат данных. Пожалуйста, введите ФИО в формате: Иванов Иван Иванович или Иванов Иван."));
                return;
            }
            String recommendation = botService.getRecommendation(username);


            bot.execute(new SendMessage(chatId, recommendation));
        }
    }

    private boolean isValidNameFormat(String username) {
        // Регулярное выражение для проверки ФИО (Форматы: "Иванов Иван Иванович" или "Иванов Иван")
        return username.matches("^[А-Яа-яЁё]+\\s[А-Яа-яЁё]+(\\s[А-Яа-яЁё]+)?$");

    }




}
