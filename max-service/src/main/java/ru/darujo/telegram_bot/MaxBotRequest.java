package ru.darujo.telegram_bot;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.darujo.dto.information.SendAdminMessage;
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.integration.UserServiceIntegrationImp;
import ru.darujo.model.ChatInfo;
import ru.darujo.service.FileService;
import ru.darujo.service.MenuService;
import ru.darujo.service.MessageReceiveService;
import ru.max.botapi.model.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class MaxBotRequest implements AutoCloseable {

    private UserServiceIntegrationImp userServiceIntegration;

    @Autowired
    public void setUserServiceIntegration(UserServiceIntegrationImp userServiceIntegration) {
        this.userServiceIntegration = userServiceIntegration;
    }

    private FileService fileService;

    private String botName;

    @PostConstruct
    public void init() {
        fileService.addFile("hi", fileService.resourceToFile("hi.jpg"));
        fileService.addFile("menu", fileService.resourceToFile("menu.jpg"));

        botName = maxBotSend.getName();
        messageForAdmin("Бот @" + botName + " запущен");

    }

    @Autowired
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    private MessageReceiveService messageReceiveService;

    @Autowired
    public void setMessageReceiveService(MessageReceiveService messageReceiveService) {
        this.messageReceiveService = messageReceiveService;
    }

    private MaxBotSend maxBotSend;

    @Autowired
    public void setTelegramBotSend(MaxBotSend maxBotSend) {
        this.maxBotSend = maxBotSend;
    }

    private MenuService menuService;

    @Autowired
    public void setMenuService(MenuService menuService) {
        this.menuService = menuService;
    }

    Map<Long, String> userLastCommand = new HashMap<>();


    /**
     * Этот метод вызывается при получении обновлений через метод GetUpdates.
     *
     * @param request Получено обновление
     */
    public void consume(MessageCreatedUpdate request) {


        Message requestMessage = request.message();

//        log.info(requestMessage.getChat().getUserName());
//        log.info(String.valueOf(requestMessage.getChatId()));
//        String chatId = Long.toString(requestMessage.getChatId());
//        Integer threadId = requestMessage.getMessageThreadId();
//        ChatInfo chatInfo = new ChatInfo(requestMessage.getFrom().getUserName(), chatId, threadId, requestMessage.getMessageId());
//        if (telegramBotSend.SendAction(chatInfo)) {
//            log.error("Не удалось уведомить пользователя, что я что-то делаю.");
//        }
//
//
//        messageReceiveService.saveMessageReceive(
//                new MessageReceive(
//                        requestMessage.getChatId(),
//                        requestMessage.getMessageThreadId(),
//                        requestMessage.getText(),
//                        "@" + requestMessage.getFrom().getUserName(),
//                        requestMessage.getFrom().getFirstName(),
//                        requestMessage.getFrom().getLastName(),
//                        requestMessage.getChat().getTitle(),
//                        requestMessage.getChat().getType(),
//                        requestMessage.getChat().getIsForum(),
//                        requestMessage.getChat().isChannelChat(),
//                        requestMessage.getChat().isUserChat(),
//                        requestMessage.getChat().isGroupChat(),
//                        requestMessage.getChat().isSuperGroupChat()));
//        try {
//            if (request.hasMessage() && requestMessage.hasText()) {
//                log.info("Working onUpdateReceived, request.message");
//                log.info(request.getMessage().getText());
//
//            } else {
//                if (request.getMessage().getNewChatTitle().isEmpty()) {
//                    defaultMsg(chatInfo, "Извините я пока не умею с этим работать.");
//                }
//                return;
//            }
//
//            switch (requestMessage.getText()) {
//                case "/start" ->
//                        telegramBotSend.sendPhoto(new ChatInfo("AutoHi", chatId, threadId, requestMessage.getMessageId()),
//                                fileService.getFile("hi")
//                                , """
//                                        Напишите команду для показа списка мыслей:\s
//                                         /link - подписаться на уведомления от сервиса учета трудозатрат\s
//                                         /stop - отвязать аккаунт от уведомлений""");
//                case "/link" -> getLink(chatInfo);
//                case "/menu" -> {
//                    telegramBotSend.deleteMessage(chatInfo);
//                    chatInfo.setAuthor("Autoresponder");
//                    menuService.openMainMenu(chatInfo);
//                }
//                case "/stop" -> getStop(chatInfo);
//                default -> {
//                    if (requestMessage.getText().equals("/link@" + botName)) {
//                        getLink(chatInfo);
//                    } else if (requestMessage.getText().equals("/menu@" + botName)) {
//                        telegramBotSend.deleteMessage(chatInfo);
//                        menuService.openMainMenu(new ChatInfo("Autoresponder", chatId, threadId, null));
//                    } else {
//                        String lastCommand = userLastCommand.get(requestMessage.getChatId());
//                        if (lastCommand != null
//                                && !requestMessage.getText().startsWith("/")
//                                && (lastCommand.startsWith("/link") || lastCommand.equals(CommandType.LINK.toString()))) {
//                            try {
//
//                                Integer code = Integer.parseInt(requestMessage.getText());
//                                ResultMes resultMes = userServiceIntegration.linkCodeTelegram(code, requestMessage.getChatId(), requestMessage.getMessageThreadId());
//                                if (resultMes.isOk()) {
//                                    telegramBotSend.deleteMessage(chatInfo);
//                                    defaultMsg(chatInfo, "Вы успешно подключены к оповещениям");
//                                    userLastCommand.remove(requestMessage.getChatId());
//                                } else {
//                                    defaultMsg(chatInfo, resultMes.getMessage());
//                                }
//                            } catch (NumberFormatException ex) {
//                                defaultMsg(chatInfo, "Код должен быть числом");
//                            } catch (ResourceNotFoundRunTime ex) {
//                                defaultMsg(chatInfo, ex.getMessage());
//                            }
//
//                        } else
//                            defaultMsg(chatInfo, "Я записал вашу мысль, не знаю что с ней делать.) \n ");
//                    }
//                }
//            }
//        } catch (TelegramApiException e) {
//            throw new RuntimeException(e);
//        }
//        log.info(requestMessage.getText());
//
//        if (requestMessage.getText().startsWith("/")) {
//            log.info("Команда: ");
//            userLastCommand.put(requestMessage.getChatId(), requestMessage.getText());
//            log.info(requestMessage.getText());
//        } else {
//            log.info("Сообщение: ");
//            log.info(requestMessage.getText());
//        }
    }

    public void consume(MessageCallbackUpdate request) {
        Callback callback = request.callback();
//        CallbackQuery callbackQuery = request.getCallbackQuery();
//        MaybeInaccessibleMessage requestMessage = callbackQuery.getMessage();
//        Integer threadId = callbackQuery.getMessage() instanceof Message ? ((Message) callbackQuery.getMessage()).getMessageThreadId() : null;
//        MessageReceive messageReceive = messageReceiveService.saveMessageReceive(
//                new MessageReceive(
//                        requestMessage.getChatId(),
//                        threadId,
//                        callbackQuery.getData(),
//                        "@" + callbackQuery.getFrom().getUserName(),
//                        callbackQuery.getFrom().getFirstName(),
//                        callbackQuery.getFrom().getLastName(),
//                        requestMessage.getChat().getTitle(),
//                        requestMessage.getChat().getType(),
//                        requestMessage.getChat().getIsForum(),
//                        requestMessage.getChat().isChannelChat(),
//                        requestMessage.getChat().isUserChat(),
//                        requestMessage.getChat().isGroupChat(),
//                        requestMessage.getChat().isSuperGroupChat()));
//        ChatInfo chatInfo = new ChatInfo(
//                messageReceive.getUserName(),
//                Long.toString(messageReceive.getChatId()),
//                messageReceive.getThreadId(),
//                requestMessage.getMessageId());
//        try {
//            if (CommandType.STOP.equals(CommandType.valueOf(callbackQuery.getData()))) {
//                try {
//                    telegramBotSend.deleteMessage(chatInfo);
//                    getStop(chatInfo);
//                } catch (TelegramApiException e) {
//                    throw new RuntimeException(e);
//                }
//            } else if (CommandType.LINK.equals(CommandType.valueOf(callbackQuery.getData()))) {
//                try {
//                    getLink(chatInfo);
//                } catch (TelegramApiException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        } catch (IllegalArgumentException ex) {
//            log.info(String.valueOf(ex));
//        }
//
//        try {
//            menuService.getMenu(chatInfo, callbackQuery.getData(), fileService.getFile("menu"));
//        } catch (TelegramApiException e) {
//            throw new RuntimeException(e);
//        }
    }


    private void getLink(ChatInfo chatInfo) {
        maxBotSend.deleteMessage(chatInfo);
        menuService.openCancel(chatInfo, "Введите одноразовый код:");
    }

    private void getStop(ChatInfo chatInfo) {
        try {
            if (userServiceIntegration.linkDeleteTelegram(Long.parseLong(chatInfo.getChatId()), chatInfo.getThreadId())) {
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
    private void defaultMsg(ChatInfo chatInfo, String msg){
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
