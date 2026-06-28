package ru.darujo.max_bot;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.darujo.dto.information.SendAdminMessage;
import ru.darujo.model.ChatInfo;
import ru.darujo.model.MessageSend;
import ru.darujo.service.FileService;
import ru.darujo.service.MessageSendService;
import ru.max.botapi.client.MaxApiException;
import ru.max.botapi.client.MaxBotAPI;
import ru.max.botapi.client.MaxUploadAPI;
import ru.max.botapi.model.*;

import java.io.File;
import java.util.List;


@Service
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
        return api.getMyInfo().execute().name();
    }

    public void sendPhoto(ChatInfo chatInfo, File file, String text, List<AttachmentRequest> menu) {
        try (MaxUploadAPI uploadApi = new MaxUploadAPI()) {
            SendAction(chatInfo, SenderAction.SENDING_PHOTO);
            menu = attachmentPhoto(uploadApi, file, menu);
            sendMessage(chatInfo, text, menu);
        }
    }

    private @NonNull List<AttachmentRequest> attachmentPhoto(MaxUploadAPI uploadApi, File file, List<AttachmentRequest> menu) {
        return attachmentImage(uploadApi, UploadType.IMAGE, null, file, menu);
    }

    private List<AttachmentRequest> attachmentDocument(MaxUploadAPI uploadApi, String fileName, File file, List<AttachmentRequest> menu) {
        return attachmentFile(uploadApi, UploadType.FILE, fileName, file, menu);
    }

    private @NonNull List<AttachmentRequest> attachmentFile(MaxUploadAPI uploadApi, UploadType uploadType, String filename, File file, List<AttachmentRequest> menu) {

        // Шаг 1: запросить endpoint для загрузки
        UploadEndpoint endpoint = api.getUploadUrl(uploadType).execute();

        // Шаг 2: передать файл потоком (без буферизации в куче)
        FileUploadedInfo info = uploadApi.uploadFile(endpoint, file.toPath(), filename == null ? file.getName() : filename);
        AttachmentRequest attachmentRequest = new FileAttachmentRequest(new MediaRequestPayload(info.token()));
        try {
            Thread.sleep(2000); // пауза 2 секунды
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }

        if (menu == null) {
            menu = List.of(attachmentRequest);
        } else {
            menu.add(attachmentRequest);
        }
        return menu;
    }

    private @NonNull List<AttachmentRequest> attachmentImage(MaxUploadAPI uploadApi, UploadType uploadType, String filename, File file, List<AttachmentRequest> menu) {

        // Шаг 1: запросить endpoint для загрузки
        UploadEndpoint endpoint = api.getUploadUrl(uploadType).execute();

        // Шаг 2: передать файл потоком (без буферизации в куче)
        ImageUploadedInfo info = uploadApi.uploadImage(endpoint, file.toPath(), filename == null ? file.getName() : filename);
        ImageAttachmentRequest attachmentRequest = new ImageAttachmentRequest(
                new PhotoAttachmentRequestPayload(null, null, info.photos()));
        try {
            Thread.sleep(2000); // пауза 2 секунды
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
        if (menu == null) {
            menu = List.of(attachmentRequest);
        } else {
            menu.add(attachmentRequest);
        }
        return menu;
    }

    public void sendDocument(ChatInfo chatInfo, String fileName, File file, String text) {
        try (MaxUploadAPI uploadApi = new MaxUploadAPI()) {
            SendAction(chatInfo, SenderAction.SENDING_FILE);
            List<AttachmentRequest> attachmentRequest = attachmentDocument(uploadApi, fileName, file, null);

            sendMessage(chatInfo, text, attachmentRequest);
        }
    }


    public SendMessageResult sendMessage(ChatInfo chatInfo, String text, List<AttachmentRequest> menu) {
        SendMessageResult messageSend = api.sendMessage(new NewMessageBody(text, menu, null, null, null))
                .chatId(Long.parseLong(chatInfo.getChatId()))
                .execute();

        messageSendService.saveMessageSend(new MessageSend(chatInfo, text));
        return messageSend;

    }

    @Value("${max.bot.admin-id}")
    private String adminId;

    public void sendMessageForAdmin(SendAdminMessage message) {
        if (adminId.isEmpty()) {
            return;
        }

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

    public void deleteMessage(ChatInfo chatInfo) {
        if (chatInfo.getOriginMessageId() == null) {
            return;
        }
        try {
            api.deleteMessage(chatInfo.getOriginMessageId()).execute();
        } catch (MaxApiException maxApiException) {
            log.error(maxApiException.getMessage(), maxApiException);
        }
    }

    public void editMessage(ChatInfo chatInfo, String newText, List<AttachmentRequest> /*InlineKeyboardMarkup */ menu) {
        SendAction(chatInfo, SenderAction.TYPING_ON);
        api.editMessage(new NewMessageBody(newText, menu, null, null, null), chatInfo.getOriginMessageId()).execute();
    }

    public void EditPhoto(ChatInfo chatInfo, String newText, List<AttachmentRequest> /*InlineKeyboardMarkup*/ menu, File file) {
        SendAction(chatInfo, SenderAction.SENDING_PHOTO);
        try (
                MaxUploadAPI uploadApi = new MaxUploadAPI()) {

// Шаг 1: запросить endpoint для загрузки
            menu = attachmentPhoto(uploadApi, file, menu);

            editMessage(chatInfo, newText, menu);
        }

    }

    public boolean SendAction(ChatInfo chatInfo) {
        return SendAction(chatInfo, SenderAction.TYPING_ON);

    }

    public boolean SendAction(ChatInfo chatInfo, SenderAction actionType) {
        api.sendAction(new ActionRequestBody(actionType), Long.parseLong(chatInfo.getChatId())).execute();
        return false;
    }
}