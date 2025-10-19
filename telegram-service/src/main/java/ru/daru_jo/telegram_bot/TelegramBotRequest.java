package ru.daru_jo.telegram_bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBotRequest implements LongPollingSingleThreadUpdateConsumer {

    private TelegramBotSend telegramBotSend;

    @Autowired
    public void setTelegramBotSend(TelegramBotSend telegramBotSend) {
        this.telegramBotSend = telegramBotSend;
    }
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
        if (request.hasMessage() && requestMessage.hasText()) {
            System.out.println("Working onUpdateReceived, request text[{}]");
            System.out.println(request.getMessage().getText());
        }
        try {
        if (requestMessage.getText().equals("/start")) {

                defaultMsg( chatId,"Напишите команду для показа списка мыслей: \n " + "/idea - показать мысли");

        }
        else if (requestMessage.getText().equals("/idea")) {
                onIdea(chatId);

        } else
            defaultMsg(chatId, "Я записал вашу мысль :) \n ");
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Working, text[{}]");
        System.out.println(requestMessage.getText());

        if (requestMessage.getText().startsWith("/")) {
            System.out.println("Команда: ");
            System.out.println(requestMessage.getText());
        } else {
            System.out.println("мысль: ");
            System.out.println(requestMessage.getText());
        }
    }

    /**
     * Метод отправки сообщения со списком мыслей - по команде "/idea"
     *
     * @param chatId - метод обработки сообщения
     */
    private void onIdea(String chatId) throws TelegramApiException {

        defaultMsg(chatId, "Вот список ваших мыслей: \n");
    }

    /**
     * Шабонный метод отправки сообщения пользователю
     *
     * @param chatId   - индификатор чата
     * @param msg      - сообщение
     */
    private void defaultMsg(String chatId, String msg) throws TelegramApiException {
        telegramBotSend.sendMessage(chatId, msg);
    }

}
