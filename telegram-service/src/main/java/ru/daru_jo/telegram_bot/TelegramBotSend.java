package ru.daru_jo.telegram_bot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
@Component
public class TelegramBotSend {
    private final OkHttpTelegramClient tgClient;

    public TelegramBotSend(@Value("${telegram-bot.token}") String botToken) {
        tgClient = new OkHttpTelegramClient(botToken);
    }

    public void sendMessage (String chatId,  String text) throws TelegramApiException {
        SendMessage message =  new SendMessage(chatId, text);
        tgClient.execute(message);
    }
}
