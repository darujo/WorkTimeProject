package ru.darujo.telegram_bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.darujo.model.MessageSend;
import ru.darujo.service.MessageSendService;

import javax.transaction.Transactional;
import java.io.File;

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
        SendMessage message =  new SendMessage(chatId, text);
        tgClient.execute(message);
        messageSendService.saveMessageSend(new MessageSend(null,author,chatId,text));

    }
    public void sendPhoto (String chatId, File file, String text) throws TelegramApiException {
//        messageSendService.saveMessageSend(new MessageSend(null,author,chatId,text));
        SendPhoto message =  new SendPhoto(chatId, new InputFile(file));
        if (!text.isEmpty()){
            message.setCaption(text);
        }
        Message message1 =tgClient.execute(message);
    }
}
