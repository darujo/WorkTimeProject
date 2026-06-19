package ru.darujo.max_bot;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.darujo.dto.information.SendAdminMessage;
import ru.darujo.model.ChatInfo;
import ru.darujo.model.MessageSend;
import ru.darujo.service.FileService;
import ru.darujo.service.MessageSendService;
import ru.max.botapi.client.MaxBotAPI;
import ru.max.botapi.client.MaxUploadAPI;
import ru.max.botapi.model.*;

import java.io.File;
import java.util.List;


@Component
@Slf4j
public class MaxBotSend {
//    private TelegramClient tgClient;
private MaxBotAPI api;

    @Autowired
    public void setApi(MaxBotAPI api) {
        this.api = api;
    }

    private MessageSendService messageSendService;

    @Autowired
    public void setMessageSendService(MessageSendService messageSendService) {
        this.messageSendService = messageSendService;
    }

    public SendMessageResult sendMessage(ChatInfo chatInfo, String text) {
        return sendMessage(chatInfo, text, null);
    }

    public void sendPhoto(ChatInfo chatInfo, File file, String text) {
        sendPhoto(chatInfo, file, text, null);
    }


    @PostConstruct
    public void setCommand() {
//        List<BotCommand> botCommands = new ArrayList<>();
//        botCommands.add(new BotCommand("/menu", "Открыть меню"));
//        botCommands.add(new BotCommand("/stop", "Отвязать аккаунт от уведомлений"));
//        botCommands.add(new BotCommand("/link", "Подписаться на уведомления от сервиса трудозатрат"));
//        SetMyCommands setMyCommands = new SetMyCommands(botCommands);
//        setMyCommands.setScope(new BotCommandScopeAllPrivateChats());
//        try {
//            tgClient.execute(setMyCommands);
//
//        } catch (TelegramApiException e) {
//            log.error(e.getMessage(), e);
//        }
//        setMyCommands.setScope(new BotCommandScopeAllGroupChats());
//        try {
//            tgClient.execute(setMyCommands);
//
//        } catch (TelegramApiException e) {
//            log.error(e.getMessage(), e);
//        }
    }

    public String getName() {
//        GetMe getMyName = new GetMe();
//
//        try {
//            User user = tgClient.execute(getMyName);
//            return user.getUserName();
//        } catch (TelegramApiException e) {
//            log.error(e.getMessage(), e);
//            return null;
//        }
      return "name bot";
    }

    public void sendPhoto(ChatInfo chatInfo, File file, String text, List<AttachmentRequest> menu) {
        try (
                MaxUploadAPI uploadApi = new MaxUploadAPI()) {
            SendAction(chatInfo, SenderAction.SENDING_PHOTO);
// Шаг 1: запросить endpoint для загрузки
            UploadEndpoint endpoint = api.getUploadUrl(UploadType.IMAGE).execute();

// Шаг 2: передать файл потоком (без буферизации в куче)
            FileUploadedInfo info = uploadApi.uploadFile(endpoint, file.toPath(), file.getName());
            AttachmentRequest attachmentRequest = new FileAttachmentRequest(new MediaRequestPayload(info.token()));
            if (menu == null) {
                menu = List.of(attachmentRequest);
            } else {
                menu.add(attachmentRequest);
            }

            sendMessage(chatInfo, text, menu);
        }
        //        SendPhoto message = new SendPhoto(chatInfo.getChatId(), new InputFile(file));
//
//        message.setMessageThreadId(chatInfo.getThreadId());
//        if (!text.isEmpty()) {
//            message.setCaption(text);
//            message.setReplyMarkup(menu);
//        }
//        try {
//            tgClient.execute(message);
//        } catch (TelegramApiException e) {
//            sendMessage(chatInfo, text);
//        }
    }

    public void sendDocument(ChatInfo chatInfo, String fileName, File file, String text)  {
        try (MaxUploadAPI uploadApi = new MaxUploadAPI()) {
            SendAction(chatInfo, SenderAction.SENDING_FILE);
// Шаг 1: запросить endpoint для загрузки
            UploadEndpoint endpoint = api.getUploadUrl(UploadType.FILE).execute();

// Шаг 2: передать файл потоком (без буферизации в куче)
            FileUploadedInfo info = uploadApi.uploadFile(endpoint, file.toPath(), fileName);
            AttachmentRequest attachmentRequest = new FileAttachmentRequest(new MediaRequestPayload(info.token()));


            sendMessage(chatInfo, text, List.of(attachmentRequest));
        }
//        SendDocument message = new SendDocument(chatInfo.getChatId(), new InputFile(file, fileName));
//        message.setMessageThreadId(chatInfo.getThreadId());
//        message.setReplyToMessageId(chatInfo.getOriginMessageId());
//        if (!text.isEmpty()) {
//            message.setCaption(text);
//        }
//        try {
//            tgClient.execute(message);
//        } catch (TelegramApiException e) {
//            sendMessage(chatInfo, text);
//        }
    }

    public SendMessageResult sendMessage(ChatInfo chatInfo, String text, List<AttachmentRequest> menu) {
        SendMessageResult messageSend = api.sendMessage(new NewMessageBody(text, menu, null, null, null))
                .chatId(Long.parseLong(chatInfo.getChatId()))
                .execute();

        //        SendMessage message = new SendMessage(chatInfo.getChatId(), text);
//
//        message.setMessageThreadId(chatInfo.getThreadId());
//        message.enableHtml(true);
////
//        if (chatInfo.getOriginMessageId() != null) {
//            message.setReplyParameters(
//                    ReplyParameters
//                            .builder()
////                        .chatId(chatInfo.getChatId())
//                            .messageId(chatInfo.getOriginMessageId())
//                            .build());
//        }
//        message.setReplyMarkup(menu);
//        Message messageSend = tgClient.execute(message);
        messageSendService.saveMessageSend(new MessageSend(chatInfo, text));
        return messageSend;

    }

    @Value("${telegram-bot.admin-id}")
    private String adminId;

    public void sendMessageForAdmin(SendAdminMessage message) {
        ChatInfo chatInfo = new ChatInfo(null, adminId, null);
        if (message.isAttachFile()) {
            sendDocument(
                    chatInfo,
                    message.getFileName(),
                    FileService.getFile(message.toString(), message.getFileName(), message.getFileBody()),
                    message.getText());
        } else {
            sendMessage(chatInfo, message.getText());
        }
    }

    public void deleteMessage(ChatInfo chatInfo)  {
        if (chatInfo.getOriginMessageId() == null) {
            return;
        }
        api.deleteMessage(chatInfo.getOriginMessageId()).execute();
//        DeleteMessage delete = new DeleteMessage(chatInfo.getChatId(), chatInfo.getOriginMessageId());
//        chatInfo.setOriginMessageId(null);
//        tgClient.execute(delete);
    }

    public void editMessage(ChatInfo chatInfo, String newText, List<AttachmentRequest> /*InlineKeyboardMarkup */ menu) {
        SendAction(chatInfo, SenderAction.TYPING_ON);
        api.editMessage(new NewMessageBody(newText, menu, null, null, null), chatInfo.getOriginMessageId()).execute();
//        EditMessageText edit = new EditMessageText(newText);
//        edit.setChatId(chatInfo.getChatId());
//        edit.setMessageId(chatInfo.getOriginMessageId());
//        edit.setText(newText);
//        edit.setReplyMarkup(menu);
//
//        tgClient.execute(edit);
    }

    public void EditPhoto(ChatInfo chatInfo, String newText, List<AttachmentRequest> /*InlineKeyboardMarkup*/ menu, File file) {
        SendAction(chatInfo, SenderAction.SENDING_PHOTO);
        try (
                MaxUploadAPI uploadApi = new MaxUploadAPI()) {

// Шаг 1: запросить endpoint для загрузки
            UploadEndpoint endpoint = api.getUploadUrl(UploadType.IMAGE).execute();

// Шаг 2: передать файл потоком (без буферизации в куче)
            FileUploadedInfo info = uploadApi.uploadFile(endpoint, file.toPath(), file.getName());
            AttachmentRequest attachmentRequest = new FileAttachmentRequest(new MediaRequestPayload(info.token()));
            if (menu == null) {
                menu = List.of(attachmentRequest);
            } else {
                menu.add(attachmentRequest);
            }

            editMessage(chatInfo, newText, menu);
        }

        //        EditMessageMedia edit = new EditMessageMedia(new InputMediaPhoto(file, "menu.jpg"));
//        edit.setChatId(chatInfo.getChatId());
//        edit.setMessageId(chatInfo.getOriginMessageId());
//        edit.getMedia().setCaption(newText);
////        edit.(newText);
//        edit.setReplyMarkup(menu);
//        tgClient.execute(edit);
    }

    public boolean SendAction(ChatInfo chatInfo) {
        //todo тип отправки
        return SendAction(chatInfo, SenderAction.TYPING_ON);

    }

    public boolean SendAction(ChatInfo chatInfo, SenderAction actionType) {
        api.sendAction(new ActionRequestBody(actionType), Long.parseLong(chatInfo.getChatId())).execute();
        //        SendChatAction sendChatAction = new SendChatAction(chatInfo.getChatId(), actionType.toString());
//        sendChatAction.setChatId(chatInfo.getChatId());
//        sendChatAction.setMessageThreadId(chatInfo.getThreadId());
//
//        try {
//            return tgClient.execute(sendChatAction);
//        } catch (TelegramApiException e) {
//            log.error(e.getMessage(), e);
//        }
        return false;
    }
}