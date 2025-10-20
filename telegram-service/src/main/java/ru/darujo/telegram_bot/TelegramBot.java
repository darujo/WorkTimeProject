package ru.darujo.telegram_bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
@Component
public class TelegramBot {
    @Autowired
    public TelegramBot(@Value("${telegram-bot.token}") String botToken,
                       TelegramBotRequest telegramBotRequest) {

        try {
            TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication();
            botsApplication.registerBot(botToken, telegramBotRequest);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }




}
