package ru.darujo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.darujo.dto.information.ResultMes;
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.integration.InfoServiceIntegration;
import ru.darujo.integration.UserServiceIntegration;
import ru.darujo.telegram_bot.TelegramBotSend;

import java.io.File;
import java.util.*;

@Service
public class MenuService {
    private TelegramBotSend telegramBotSend;

    @Autowired
    public void setTelegramBotSend(TelegramBotSend telegramBotSend) {
        this.telegramBotSend = telegramBotSend;
    }

    private InfoServiceIntegration infoServiceIntegration;

    @Autowired
    public void setInfoServiceIntegration(InfoServiceIntegration infoServiceIntegration) {
        this.infoServiceIntegration = infoServiceIntegration;
    }

    private UserServiceIntegration userServiceIntegration;

    @Autowired
    public void setUserServiceIntegration(UserServiceIntegration userServiceIntegration) {
        this.userServiceIntegration = userServiceIntegration;
    }

    private InlineKeyboardMarkup createMenu(List<InlineKeyboardRow> rows) {
        return new InlineKeyboardMarkup(rows);
    }

    private InlineKeyboardRow createRow(List<CommandType> commands) {
        List<InlineKeyboardButton> buttons = new ArrayList<>();

        commands.forEach((command) ->
                buttons.add(InlineKeyboardButton.builder()
                        .text(command.getName())
                        .callbackData(command.toString())
                        .build()));
        return new InlineKeyboardRow(buttons);
    }

    public InlineKeyboardMarkup getMainMenu() {
        List<InlineKeyboardRow> rows = new LinkedList<>();

        List<CommandType> row = new LinkedList<>();
        row.add(CommandType.LINK);
        row.add(CommandType.STOP);
        rows.add(createRow(row));

        row = new LinkedList<>();
        row.add(CommandType.REPORT);
        rows.add(createRow(row));
        addRowCancel(rows);
        return createMenu(rows);

    }

    public void openMainMenu(String author, String chatId, File file) throws TelegramApiException {
        telegramBotSend.sendPhoto(author, chatId, file, "Менюшечка. Чего желаете?", getMainMenu());
    }

    public void getMenu(String author, String charId, Integer messageId, CommandType command, File file) throws TelegramApiException {
        if (command.equals(CommandType.REPORT)) {
            telegramBotSend.EditPhoto(charId, messageId, "Какой отчет вы хотите построить?", getMenuReport(), file);
        } else if (command.equals(CommandType.WORK_STATUS)) {
            telegramBotSend.EditPhoto(charId, messageId, "Кому разослать результат по отчету " + command + "?", getMenuWorkStatus(), file);
        }
        if (command.equals(CommandType.WORK_STATUS_ME)) {
            sendWorkStatus(author, charId, messageId, true);

        }
        if (command.equals(CommandType.WORK_STATUS_ALL)) {
            sendWorkStatus(author, charId, messageId, false);
        }
        if (command.equals(CommandType.CANCEL)) {
            telegramBotSend.deleteMessage(charId, messageId);
        }

    }

    private void sendWorkStatus(String author, String charId, Integer messageId, boolean sendMe) throws TelegramApiException {
        try {
            ResultMes resultMes = userServiceIntegration.checkUserTelegram(Long.parseLong(charId));
            if (resultMes.isOk()) {
                infoServiceIntegration.sendWorkStatus(author, sendMe ? Long.parseLong(charId) : null);
            }
            telegramBotSend.deleteMessage(charId, messageId);
            telegramBotSend.sendMessage(null, charId, resultMes.isOk() ? "Отчет будет доставлен в ближайшее время" : resultMes.getMessage());

        } catch (ResourceNotFoundRunTime ex) {
            telegramBotSend.sendMessage(null, charId, "Что-то пошло не так отчет не будет сформирован. Попробуйте позже или обратитесь к администратуру");
        }
    }

    private InlineKeyboardMarkup getMenuWorkStatus() {
        List<InlineKeyboardRow> rows = new LinkedList<>();

        List<CommandType> row = new LinkedList<>();
        row.add(CommandType.WORK_STATUS_ME);
        row.add(CommandType.WORK_STATUS_ALL);
        rows.add(createRow(row));
        addRowCancel(rows);
        return createMenu(rows);

    }

    private InlineKeyboardMarkup getMenuReport() {
        List<InlineKeyboardRow> rows = new LinkedList<>();

        List<CommandType> row = new LinkedList<>();
        row.add(CommandType.WORK_STATUS);
        rows.add(createRow(row));
        addRowCancel(rows);
        return createMenu(rows);

    }

    private InlineKeyboardMarkup getMenuCancel() {
        List<InlineKeyboardRow> rows = new LinkedList<>();

        rows.add(createRowCancel());

        return createMenu(rows);

    }

    public void openCancel(String chatId, String text) throws TelegramApiException {
        telegramBotSend.sendMessage(chatId, text, getMenuCancel());
    }

    private InlineKeyboardRow createRowCancel() {
        List<CommandType> row = new LinkedList<>();
        row.add(CommandType.CANCEL);
        return createRow(row);
    }

    private void addRowCancel(List<InlineKeyboardRow> rows) {
        rows.add(createRowCancel());

    }
}
