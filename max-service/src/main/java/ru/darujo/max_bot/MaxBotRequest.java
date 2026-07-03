package ru.darujo.max_bot;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.darujo.dto.information.ResultMes;
import ru.darujo.dto.information.SendAdminMessage;
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.integration.UserServiceIntegrationImp;
import ru.darujo.model.ChatInfo;
import ru.darujo.model.MessageReceive;
import ru.darujo.service.CommandType;
import ru.darujo.service.FileSaverService;
import ru.darujo.service.MenuService;
import ru.darujo.service.MessageReceiveService;
import ru.max.botapi.core.UpdateHandler;
import ru.max.botapi.model.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class MaxBotRequest implements UpdateHandler, AutoCloseable {

    private UserServiceIntegrationImp userServiceIntegration;

    @Autowired
    public void setUserServiceIntegration(UserServiceIntegrationImp userServiceIntegration) {
        this.userServiceIntegration = userServiceIntegration;
    }

    private FileSaverService fileSaverService;

    private String botName;

    @PostConstruct
    public void init() {
        fileSaverService.addFile("hi", fileSaverService.resourceToFile("hi.jpg"));
        fileSaverService.addFile("menu", fileSaverService.resourceToFile("menu.jpg"));

        botName = maxBotSend.getName();
        messageForAdmin("Бот @" + botName + " запущен");

    }

    @Autowired
    public void setFileService(FileSaverService fileSaverService) {
        this.fileSaverService = fileSaverService;
    }

    private MessageReceiveService messageReceiveService;

    @Autowired
    public void setMessageReceiveService(MessageReceiveService messageReceiveService) {
        this.messageReceiveService = messageReceiveService;
    }

    private MaxBotSend maxBotSend;

    @Autowired
    public void setMaxBotSend(MaxBotSend maxBotSend) {
        this.maxBotSend = maxBotSend;
    }

    private MenuService menuService;

    @Autowired
    public void setMenuService(MenuService menuService) {
        this.menuService = menuService;
    }

    Map<String, String> userLastCommand = new HashMap<>();


    /**
     * Этот метод вызывается при получении обновлений через метод GetUpdates.
     *
     * @param update Получено обновление
     */
    public void onUpdate(Update update) {

        if (update instanceof MessageCreatedUpdate msg) {
            log.info(msg.message().sender().username());
            ChatInfo chatInfo = getChatInfo(msg.message());
            saveMessage(msg.message());
            onUpdate(chatInfo, msg);
        } else if (update instanceof MessageCallbackUpdate msg) {
            ChatInfo chatInfo = getChatInfo(msg.message());
            saveMessage(msg.message());
            onUpdate(chatInfo, msg);
        } else if (update instanceof BotStartedUpdate msg) {
            ChatInfo chatInfo = new ChatInfo(msg.user().name(), msg.chatId(), null);

            onUpdate(chatInfo, msg);
        }
    }

    private @NonNull ChatInfo getChatInfo(Message msg) {
        ChatInfo chatInfo = new ChatInfo(msg.sender().username(), Long.toString(msg.recipient().chatId()), msg.body().mid());
        if (maxBotSend.SendAction(chatInfo)) {
            log.error("Не удалось уведомить пользователя, что я что-то делаю. (Max)");
        }
        return chatInfo;
    }

    private void saveMessage(Message msg) {
        messageReceiveService.saveMessageReceive(
                new MessageReceive(
                        msg.recipient().chatId(),
                        null,
                        msg.body().text(),
                        "@" + msg.sender().username(),
                        msg.sender().firstName(),
                        msg.sender().lastName(),
                        null,
                        "message",
                        null,
                        msg.recipient().chatType().equals(ChatType.CHANNEL),
                        msg.recipient().chatType().equals(ChatType.DIALOG),
                        msg.recipient().chatType().equals(ChatType.CHAT),
                        null));
    }

    public void onUpdate(ChatInfo chatInfo, BotStartedUpdate ignoredRequest) {
        commandStart(chatInfo);
    }


    public void onUpdate(ChatInfo chatInfo, MessageCreatedUpdate request) {

        Message requestMessage = request.message();
        boolean isCommand = requestMessage.body().text().startsWith("/");
//        try {
        log.info("Working onUpdateReceived, request.message");
        log.info(requestMessage.body().text());


        switch (requestMessage.body().text()) {
            case "/start" -> commandStart(chatInfo);
            case "/link" -> getLink(chatInfo);
            case "/menu" -> {
                maxBotSend.deleteMessage(chatInfo);
                chatInfo.setAuthor("Autoresponder");
                menuService.openMainMenu(chatInfo);
            }
            case "/stop" -> getStop(chatInfo);
            default -> {
                if (requestMessage.body().text().equals("/link@" + botName)) {
                    getLink(chatInfo);
                } else if (requestMessage.body().text().equals("/menu@" + botName)) {
                    maxBotSend.deleteMessage(chatInfo);
                    menuService.openMainMenu(new ChatInfo("Autoresponder", chatInfo.getChatId(), chatInfo.getOriginMessageId()));
                } else {
                    String lastCommand = userLastCommand.get(chatInfo.getChatId());
                    if (lastCommand != null
                            && !isCommand
                            && (lastCommand.startsWith("/link") || lastCommand.equals(CommandType.LINK.toString()))) {
                        try {

                            Integer code = Integer.parseInt(requestMessage.body().text());
                            ResultMes resultMes = userServiceIntegration.linkCodeMax(code, chatInfo.getChatId(), null);
                            if (resultMes.isOk()) {
                                maxBotSend.deleteMessage(chatInfo);
                                defaultMsg(chatInfo, "Вы успешно подключены к оповещениям");
                                userLastCommand.remove(chatInfo.getChatId());
                            } else {
                                defaultMsg(chatInfo, resultMes.getMessage());
                            }
                        } catch (NumberFormatException ex) {
                            defaultMsg(chatInfo, "Код должен быть числом");
                        } catch (ResourceNotFoundRunTime ex) {
                            defaultMsg(chatInfo, ex.getMessage());
                        }

                    } else
                        defaultMsg(chatInfo, "Я записал вашу мысль, не знаю что с ней делать.) \n ");
                }
            }
        }

        if (isCommand) {
            log.info("Команда: ");
            userLastCommand.put(chatInfo.getChatId(), requestMessage.body().text());
            log.info(requestMessage.body().text());
        } else {
            log.info("Сообщение: ");
            log.info(requestMessage.body().text());
        }
    }

    private void commandStart(ChatInfo chatInfo) {
        maxBotSend.sendPhoto(new ChatInfo("AutoHi", chatInfo.getChatId(), chatInfo.getOriginMessageId()),
                fileSaverService.getFile("hi")
                , """
                        Напишите команду для показа списка мыслей:\s
                         /link - подписаться на уведомления от сервиса учета трудозатрат\s
                         /stop - отвязать аккаунт от уведомлений\s
                         /menu - Вызвать меню действий""");
    }

    public void onUpdate(ChatInfo chatInfo, MessageCallbackUpdate request) {
        Callback callback = request.callback();
        try {
            if (CommandType.STOP.equals(CommandType.valueOf(callback.payload()))) {
                maxBotSend.deleteMessage(chatInfo);
                getStop(chatInfo);
                return;
            } else if (CommandType.LINK.equals(CommandType.valueOf(callback.payload()))) {
                getLink(chatInfo);
                return;
            }

        } catch (IllegalArgumentException ex) {
            log.info(String.valueOf(ex));
        }
        {
            menuService.getMenu(chatInfo, callback.payload(), fileSaverService.getFile("menu"));
        }
    }


    private void getLink(ChatInfo chatInfo) {
        maxBotSend.deleteMessage(chatInfo);
        menuService.openCancel(chatInfo, "Введите одноразовый код:");
    }

    private void getStop(ChatInfo chatInfo) {
        try {
            // toDo заменить на max
            if (userServiceIntegration.linkDeleteTelegram(Long.parseLong(chatInfo.getChatId()), null)) {
                if (chatInfo.getOriginMessageId() == null) {
                    defaultMsg(chatInfo, "Что-то пошло не так как хотелось бы.");
                } else {
                    maxBotSend.editMessage(chatInfo, "Вы успешно отключены", null);
                }
            } else {
                defaultMsg(chatInfo, "Что-то пошло не так как хотелось бы.");
            }
        } catch (ResourceNotFoundRunTime ex) {
            defaultMsg(chatInfo, "Сервис авторизации временно не доступен попробуйте позже");
        }
    }

    /**
     * Шаблонный метод отправки сообщения пользователю
     *
     * @param chatInfo - идентификатор чата
     * @param msg      - сообщение
     */
    private void defaultMsg(ChatInfo chatInfo, String msg) {
        chatInfo.setAuthor("Autoresponder");
        maxBotSend.sendMessage(chatInfo, msg);
    }

    @Override
    public void close() { // <-- 8
        messageForAdmin("⚠️ The bot @" + botName + " has stopped");
    }

    private void messageForAdmin(String text) {
        try {
            maxBotSend.sendMessageForAdmin(new SendAdminMessage() {
                @Override
                public String getTitle() {
                    return text;
                }

                @Override
                public String getText() {
                    return text;
                }
            });
            // todo проставить тип ошибки
        } catch (RuntimeException e) {
            log.error("Failed to send message while stopping the bot", e);
        }
    }
}
