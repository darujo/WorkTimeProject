package ru.darujo.telegram_bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.MaybeInaccessibleMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.darujo.dto.information.ResultMes;
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.integration.UserServiceIntegration;
import ru.darujo.model.MessageReceive;
import ru.darujo.service.CommandType;
import ru.darujo.service.FileService;
import ru.darujo.service.MenuService;
import ru.darujo.service.MessageReceiveService;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class TelegramBotRequest implements LongPollingSingleThreadUpdateConsumer {
    private UserServiceIntegration userServiceIntegration;

    @Autowired
    public void setUserServiceIntegration(UserServiceIntegration userServiceIntegration) {
        this.userServiceIntegration = userServiceIntegration;
    }

    private FileService fileService;
    @Value("${telegram-bot.name}")
    private String botName;

    @Autowired
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    private MessageReceiveService messageReceiveService;

    @Autowired
    public void setMessageReceiveService(MessageReceiveService messageReceiveService) {
        this.messageReceiveService = messageReceiveService;
    }

    private TelegramBotSend telegramBotSend;

    @Autowired
    public void setTelegramBotSend(TelegramBotSend telegramBotSend) {
        this.telegramBotSend = telegramBotSend;
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
    @Override
    public void consume(Update request) {
        if (request.hasMessage()) {
            Message requestMessage = request.getMessage();

            log.info(requestMessage.getChat().getUserName());
            log.info(String.valueOf(requestMessage.getChatId()));
            String chatId = Long.toString(requestMessage.getChatId());


            messageReceiveService.saveMessageReceive(
                    new MessageReceive(
                            requestMessage.getChatId(),
                            requestMessage.getText(),
                            requestMessage.getFrom().getUserName(),
                            requestMessage.getFrom().getFirstName(),
                            requestMessage.getFrom().getLastName(),
                            requestMessage.getChat().getTitle(),
                            requestMessage.getChat().getType(),
                            requestMessage.getChat().getIsForum(),
                            requestMessage.getChat().isChannelChat(),
                            requestMessage.getChat().isUserChat(),
                            requestMessage.getChat().isGroupChat(),
                            requestMessage.getChat().isSuperGroupChat()));
            try {
                if (request.hasMessage() && requestMessage.hasText()) {
                    log.info("Working onUpdateReceived, request.message");
                    log.info(request.getMessage().getText());

                } else {
                    if (request.getMessage().getNewChatTitle().isEmpty()) {
                        defaultMsg(chatId, "Извините я пока не умею с этим работать.");
                    }
                    return;
                }

                switch (requestMessage.getText()) {
                    case "/start" -> telegramBotSend.sendPhoto("AutoHi", chatId,
                            fileService.getFile("hi")
                            , """
                                    Напишите команду для показа списка мыслей:\s
                                     /link - подписаться на уведомления от сервиса учета трудозатрат\s
                                     /stop - отвязать аккаунт от уведомлений""");
                    case "/link" -> getLink(chatId);
                    case "/menu" -> {
                        telegramBotSend.deleteMessage(chatId, requestMessage.getMessageId());
                        menuService.openMainMenu("Autoresponder", chatId);
                    }
                    case "/stop" -> getStop(chatId, requestMessage.getMessageId());
                    default -> {
                        if (requestMessage.getText().equals("/link@" + botName)) {
                            getLink(chatId);
                        } else if (requestMessage.getText().equals("/menu@" + botName)) {
                            telegramBotSend.deleteMessage(chatId, requestMessage.getMessageId());
                            menuService.openMainMenu("Autoresponder", chatId);
                        } else {
                            String lastCommand = userLastCommand.get(requestMessage.getChatId());
                            if (lastCommand != null
                                    && !requestMessage.getText().startsWith("/")
                                    && (lastCommand.startsWith("/link") || lastCommand.equals(CommandType.LINK.toString()))) {
                                try {

                                    Integer code = Integer.parseInt(requestMessage.getText());
                                    ResultMes resultMes = userServiceIntegration.linkCodeTelegram(code, requestMessage.getChatId());
                                    if (resultMes.isOk()) {
                                        telegramBotSend.deleteMessage(chatId, requestMessage.getMessageId());
                                        defaultMsg(chatId, "Вы успешно подключены к оповещениям");
                                    } else {
                                        defaultMsg(chatId, resultMes.getMessage());
                                    }
                                } catch (NumberFormatException ex) {
                                    defaultMsg(chatId, "Код должен быть числом");
                                } catch (ResourceNotFoundRunTime ex) {
                                    defaultMsg(chatId, ex.getMessage());
                                }

                            } else
                                defaultMsg(chatId, "Я записал вашу мысль, не знаю что с ней делать.) \n ");
                        }
                    }
                }
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
            log.info(requestMessage.getText());

            if (requestMessage.getText().startsWith("/")) {
                log.info("Команда: ");
                userLastCommand.put(requestMessage.getChatId(), requestMessage.getText());
                log.info(requestMessage.getText());
            } else {
                log.info("Сообщение: ");
                log.info(requestMessage.getText());
            }
        } else if (request.hasCallbackQuery()) {
            CallbackQuery callbackQuery = request.getCallbackQuery();
            MaybeInaccessibleMessage requestMessage = callbackQuery.getMessage();
            log.info(callbackQuery.getFrom().getUserName());
            log.info(String.valueOf(requestMessage.getChatId()));

            MessageReceive messageReceive = messageReceiveService.saveMessageReceive(
                    new MessageReceive(
                            requestMessage.getChatId(),
                            callbackQuery.getData(),
                            "@" + callbackQuery.getFrom().getUserName(),
                            callbackQuery.getFrom().getFirstName(),
                            callbackQuery.getFrom().getLastName(),
                            requestMessage.getChat().getTitle(),
                            requestMessage.getChat().getType(),
                            requestMessage.getChat().getIsForum(),
                            requestMessage.getChat().isChannelChat(),
                            requestMessage.getChat().isUserChat(),
                            requestMessage.getChat().isGroupChat(),
                            requestMessage.getChat().isSuperGroupChat()));
            try {
                if (CommandType.STOP.equals(CommandType.valueOf(callbackQuery.getData()))) {
                    try {
                        telegramBotSend.deleteMessage(requestMessage.getChatId().toString(), requestMessage.getMessageId());
                        getStop(requestMessage.getChatId().toString(), null);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                } else if (CommandType.LINK.equals(CommandType.valueOf(callbackQuery.getData()))) {
                    try {
                        telegramBotSend.deleteMessage(requestMessage.getChatId().toString(), requestMessage.getMessageId());
                        getLink(requestMessage.getChatId().toString());
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
            } catch (IllegalArgumentException ex) {
                log.info(String.valueOf(ex));
            }

            try {
                menuService.getMenu(messageReceive.getUserName(),
                        messageReceive.getChatId().toString(), requestMessage.getMessageId(), callbackQuery.getData(), fileService.getFile("menu"));
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void getLink(String chatId) throws TelegramApiException {
        menuService.openCancel(chatId, "Введите одноразовый код:");
    }

    private void getStop(String chatId, Integer messageId) throws TelegramApiException {
        try {
            if (userServiceIntegration.linkDeleteTelegram(Long.parseLong(chatId))) {
                if (messageId == null) {
                    defaultMsg(chatId, "Что-то пошло не так как хотелось бы.");
                } else {
                    telegramBotSend.editMessage(chatId, messageId, "Вы успешно отключены", null);
                }
            } else {
                defaultMsg(chatId, "Что-то пошло не так как хотелось бы.");
            }
        } catch (ResourceNotFoundRunTime ex) {
            defaultMsg(chatId, "Сервис авторизации временно не доступен попробуйте позже");
        }
    }

    /**
     * Шаблонный метод отправки сообщения пользователю
     *
     * @param chatId - индификатор чата
     * @param msg    - сообщение
     */
    private void defaultMsg(String chatId, String msg) throws TelegramApiException {
        telegramBotSend.sendMessage("Autoresponder", chatId, msg);
    }


}
