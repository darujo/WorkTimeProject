package ru.darujo.telegram_bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.darujo.model.ChatInfo;
import ru.darujo.model.MessageSend;
import ru.darujo.service.MessageSendService;

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


    public Message sendMessage(ChatInfo chatInfo, String text) throws TelegramApiException {
        return sendMessage(chatInfo, text, null);
    }

    public void sendPhoto(ChatInfo chatInfo, File file, String text) throws TelegramApiException {
        sendPhoto(chatInfo, file, text, null);
    }

    public void sendPhoto(ChatInfo chatInfo, File file, String text, InlineKeyboardMarkup menu) throws TelegramApiException {
        SendPhoto message = new SendPhoto(chatInfo.getChatId(), new InputFile(file));

        message.setMessageThreadId(chatInfo.getThreadId());
        if (!text.isEmpty()) {
            message.setCaption(text);
            message.setReplyMarkup(menu);
        }
        try {
            tgClient.execute(message);
        } catch (TelegramApiException e) {
            sendMessage(chatInfo, text);
        }
    }

    public void sendDocument(ChatInfo chatInfo, String fileName, File file, String text) throws TelegramApiException {
        SendDocument message = new SendDocument(chatInfo.getChatId(), new InputFile(file, fileName));
        message.setMessageThreadId(chatInfo.getThreadId());
        message.setReplyToMessageId(chatInfo.getOriginMessageId());
        if (!text.isEmpty()) {
            message.setCaption(text);
        }
        try {
            tgClient.execute(message);
        } catch (TelegramApiException e) {
            sendMessage(chatInfo, text);
        }
    }

    public Message sendMessage(ChatInfo chatInfo, String text, InlineKeyboardMarkup menu) throws TelegramApiException {
        SendMessage message = new SendMessage(chatInfo.getChatId(), text);
        message.setMessageThreadId(chatInfo.getThreadId());
        message.enableHtml(true);
        message.setReplyToMessageId(chatInfo.getOriginMessageId());
        message.setReplyMarkup(menu);
        Message messageSend = tgClient.execute(message);
        messageSendService.saveMessageSend(new MessageSend(chatInfo, text));
        return messageSend;
    }

    public void deleteMessage(ChatInfo chatInfo) throws TelegramApiException {
        if (chatInfo.getOriginMessageId() == null) {
            return;
        }
        DeleteMessage delete = new DeleteMessage(chatInfo.getChatId(), chatInfo.getOriginMessageId());
        chatInfo.setOriginMessageId(null);
        tgClient.execute(delete);
    }

    public void editMessage(ChatInfo chatInfo, String newText, InlineKeyboardMarkup menu) throws TelegramApiException {
        EditMessageText edit = new EditMessageText(newText);
        edit.setChatId(chatInfo.getChatId());
        edit.setMessageId(chatInfo.getOriginMessageId());
        edit.setText(newText);
        edit.setReplyMarkup(menu);

        tgClient.execute(edit);
    }

    public void EditPhoto(ChatInfo chatInfo, String newText, InlineKeyboardMarkup menu, File file) throws TelegramApiException {
        EditMessageMedia edit = new EditMessageMedia(new InputMediaPhoto(file, "menu.jpg"));
        edit.setChatId(chatInfo.getChatId());
        edit.setMessageId(chatInfo.getOriginMessageId());
        edit.getMedia().setCaption(newText);
//        edit.(newText);
        edit.setReplyMarkup(menu);
        tgClient.execute(edit);
    }
}
