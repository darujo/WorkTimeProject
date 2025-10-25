package ru.darujo.telegram_bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.darujo.dto.information.ResultMes;
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.integration.UserServiceIntegration;
import ru.darujo.model.MessageReceive;
import ru.darujo.service.MessageReceiveService;

import java.io.File;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class TelegramBotRequest implements LongPollingSingleThreadUpdateConsumer {
    private UserServiceIntegration userServiceIntegration;

    @Autowired
    public void setUserServiceIntegration(UserServiceIntegration userServiceIntegration) {
        this.userServiceIntegration = userServiceIntegration;
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

    Map<Long, String> userLastCommand = new HashMap<>();

    /**
     * Этот метод вызывается при получении обновлений через метод GetUpdates.
     *
     * @param request Получено обновление
     */
    @Override
    public void consume(Update request) {
        Message requestMessage = request.getMessage();
        System.out.println(requestMessage.getChat().getUserName());
        System.out.println(requestMessage.getChatId());
        String chatId = Long.toString(requestMessage.getChatId());


        messageReceiveService.saveMessageReceive(
                new MessageReceive(
                        requestMessage.getChatId(),
                        requestMessage.getText(),
                        requestMessage.getChat().getUserName(),
                        requestMessage.getChat().getFirstName(),
                        requestMessage.getChat().getLastName(),
                        requestMessage.getChat().getTitle(),
                        requestMessage.getChat().getType(),
                        requestMessage.getChat().getIsForum(),
                        requestMessage.getChat().isChannelChat(),
                        requestMessage.getChat().isUserChat(),
                        requestMessage.getChat().isGroupChat(),
                        requestMessage.getChat().isSuperGroupChat()));
        try {
            if (request.hasMessage() && requestMessage.hasText()) {
                System.out.println("Working onUpdateReceived, request text[{}]");
                System.out.println(request.getMessage().getText());

            } else{
                defaultMsg(chatId, "Извините я пока не умею с этим работать.");
                return;
            }
            // ToDo document
            if (requestMessage.getText().equals("/start")) {
                try {
                telegramBotSend.sendPhoto(chatId, new File(Objects.requireNonNull(this.getClass().getResource("/hi.jpg")).toURI()),"""
                        Напишите команду для показа списка мыслей:\s
                         /link - подписаться на уведомления от сервиса учета трудо затрат\s
                         /stop - отвязать акаунт от уведомлений""");
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }

            } else if (requestMessage.getText().equals("/link")) {
                defaultMsg(chatId, "Ведите однаразовый код:");

            } else if (requestMessage.getText().equals("/stop")) {
                try {
                    if (userServiceIntegration.linkDeleteTelegram(requestMessage.getChatId())) {
                        defaultMsg(chatId, "Вы успешно отключены");
                    } else {
                        defaultMsg(chatId, "Что-то пошло не так как хотелось бы.");
                    }
                }catch (ResourceNotFoundRunTime ex){
                    defaultMsg(chatId, "Сервис авторизации времено не доступен попробуйте позже");
                }

            } else {
                String lastCommand = userLastCommand.get(requestMessage.getChatId());
                if (lastCommand != null && lastCommand.equals("/link")) {
                    try {
                        Integer code = Integer.parseInt(requestMessage.getText());
                        ResultMes resultMes =userServiceIntegration.linkCodeTelegram(code, requestMessage.getChatId());
                        if (resultMes.isOk()) {
                            defaultMsg(chatId, "Вы успешно подключены к оповещениям");
                        } else {
                            defaultMsg(chatId, resultMes.getMessage());
                        }
                    } catch (NumberFormatException ex) {
                        defaultMsg(chatId, "Код должен быть числом");
                    } catch (ResourceNotFoundRunTime ex){
                        defaultMsg(chatId, ex.getMessage());
                    }

                } else
                    defaultMsg(chatId, "Я записал вашу мысль :) \n ");
            }
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Working, text[{}]");
        System.out.println(requestMessage.getText());

        if (requestMessage.getText().startsWith("/")) {
            System.out.println("Команда: ");
            userLastCommand.put(requestMessage.getChatId(), requestMessage.getText());
            System.out.println(requestMessage.getText());
        } else {
            System.out.println("мысль: ");
            System.out.println(requestMessage.getText());
        }
    }

    /**
     * Шабонный метод отправки сообщения пользователю
     *
     * @param chatId - индификатор чата
     * @param msg    - сообщение
     */
    private void defaultMsg(String chatId, String msg) throws TelegramApiException {
        telegramBotSend.sendMessage("DaruWorkBot", chatId, msg);
    }

}
