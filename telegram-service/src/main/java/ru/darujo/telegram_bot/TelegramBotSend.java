package ru.darujo.telegram_bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.darujo.model.MessageSend;
import ru.darujo.service.MessageSendService;

@Component
public class TelegramBotSend {
    private final OkHttpTelegramClient tgClient;

    private MessageSendService messageSendService;

    @Autowired
    public void setMessageSendService(MessageSendService messageSendService) {
        this.messageSendService = messageSendService;
    }

    public TelegramBotSend(@Value("${telegram-bot.token}") String botToken) {
        tgClient = new OkHttpTelegramClient(botToken);
    }

    public void sendMessage (String author, String chatId,  String text) throws TelegramApiException {
        messageSendService.saveMessageSend(new MessageSend(null,author,chatId,text));
        SendMessage message =  new SendMessage(chatId, text);
        tgClient.execute(message);
    }
}
