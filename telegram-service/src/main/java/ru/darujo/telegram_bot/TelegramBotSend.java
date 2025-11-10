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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
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


    public void sendMessage(String author, String chatId, String text) throws TelegramApiException {
        SendMessage message = new SendMessage(chatId, text);
        message.enableHtml(true);
        tgClient.execute(message);
        messageSendService.saveMessageSend(new MessageSend(null, author, chatId, text));

    }
    public void sendPhoto(String author, String chatId, File file, String text) throws TelegramApiException {
        sendPhoto(author,chatId,file,text,null);
    }
    public void sendPhoto(String author, String chatId, File file, String text, InlineKeyboardMarkup menu) throws TelegramApiException {
        SendPhoto message = new SendPhoto(chatId, new InputFile(file));
        if (!text.isEmpty()) {
            message.setCaption(text);
            message.setReplyMarkup(menu);
        }
        try {
            tgClient.execute(message);
        } catch (TelegramApiException e) {
            sendMessage(author, chatId, text);
        }
    }

    public void sendDocument(String author, String chatId, String fileName, File file, String text) throws TelegramApiException {
        SendDocument message = new SendDocument(chatId, new InputFile(file, fileName));
        if (!text.isEmpty()) {
            message.setCaption(text);
        }
        try {
            tgClient.execute(message);
        } catch (TelegramApiException e) {
            sendMessage(author, chatId, text);
        }
    }

    // Клавиатура раздела с ключевыми словами для поиска
    public void sendMessage(String chatId, String text, InlineKeyboardMarkup menu) throws TelegramApiException {
        SendMessage message = new SendMessage(chatId, text);
        message.setReplyMarkup(menu);
        tgClient.execute(message);

    }

    public void deleteMessage(String chatId, int messageId) throws TelegramApiException {
        DeleteMessage delete = new DeleteMessage(chatId, messageId);
        tgClient.execute(delete);
    }

    public void editMessage (String chatId,int messageId,String newText, InlineKeyboardMarkup menu) throws TelegramApiException {
        EditMessageText edit = new EditMessageText(newText);
        edit.setChatId(chatId);
        edit.setMessageId(messageId);
        edit.setText(newText);
        edit.setReplyMarkup(menu);
        tgClient.execute(edit);
    }
    public void EditPhoto (String chatId,int messageId,String newText, InlineKeyboardMarkup menu,File file) throws TelegramApiException {
        EditMessageMedia edit = new EditMessageMedia(new InputMediaPhoto(file,"menu.jpg") );
        edit.setChatId(chatId);
        edit.setMessageId(messageId);
        edit.getMedia().setCaption(newText);
//        edit.(newText);
        edit.setReplyMarkup(menu);
        tgClient.execute(edit);
    }
}
