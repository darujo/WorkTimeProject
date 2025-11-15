package ru.darujo.service;

import lombok.Getter;
import lombok.Setter;
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
import ru.darujo.type.ReportTypeDto;
import ru.darujo.type.TypeEnum;

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

    private FileService fileService;

    @Autowired
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    private InlineKeyboardMarkup createMenu(List<InlineKeyboardRow> rows) {
        return new InlineKeyboardMarkup(rows);
    }

    private InlineKeyboardRow createRow(List<TypeEnum> commands) {
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

        List<TypeEnum> row = new LinkedList<>();
        row.add(CommandType.LINK);
        row.add(CommandType.STOP);
        rows.add(createRow(row));

        row = new LinkedList<>();
        row.add(CommandType.REPORT);
        rows.add(createRow(row));
        addRowCancel(rows);
        return createMenu(rows);

    }

    public void openMainMenu(String author, String chatId) throws TelegramApiException {

        telegramBotSend.sendPhoto(author, chatId, fileService.getFile("menu"), "Менюшечка. Чего желаете?", getMainMenu());
    }

    Map<Integer, MenuParam> paramMap = new HashMap<>();

    @Getter
    @Setter
    private class MenuParam {
        ReportTypeDto reportTypeDto;

    }

    public void getMenu(String author, String charId, Integer messageId, String command, File file) throws TelegramApiException {
        try {
            CommandType commandType = CommandType.valueOf(command);
            getMenu(author, charId, messageId, commandType, file);
        } catch (IllegalArgumentException ex) {
            MenuParam menuParam = getMenuParam(author, charId, messageId);
            if (menuParam == null) {
                return;
            }
            try {

                menuParam.setReportTypeDto(ReportTypeDto.valueOf(command));
                telegramBotSend.EditPhoto(charId, messageId, "Кому разослать результат по отчету " + command + "?", getMenuWorkStatus(), file);
            } catch (IllegalArgumentException illegalArgumentException) {
                reOpenMainMenu(author, charId, messageId);
            }

        }

    }

    public void getMenu(String author, String charId, Integer messageId, CommandType command, File file) throws TelegramApiException {
        MenuParam menuParam;
        if (command.getNewParam()) {
            menuParam = new MenuParam();
            paramMap.put(messageId, menuParam);
        } else {
            menuParam = getMenuParam(author, charId, messageId);
        }

        if (command.equals(CommandType.REPORT)) {
            telegramBotSend.EditPhoto(charId, messageId, "Какой отчет вы хотите построить?", getMenuReport(), file);

        }
        if (command.equals(CommandType.SEND_ME)) {
            sendReport(menuParam.getReportTypeDto(), author, charId, messageId, true);
        }
        if (command.equals(CommandType.SEND_ALL)) {
            sendReport(menuParam.getReportTypeDto(), author, charId, messageId, false);
        }
        if (command.equals(CommandType.CANCEL)) {
            deleteMessage(charId, messageId);
        }

    }

    private void deleteMessage(String charId, Integer messageId) throws TelegramApiException {
        paramMap.remove(messageId);
        telegramBotSend.deleteMessage(charId, messageId);
    }

    private MenuParam getMenuParam(String author, String charId, Integer messageId) throws TelegramApiException {
        MenuParam menuParam = paramMap.get(messageId);
        if (menuParam == null) {
            reOpenMainMenu(author, charId, messageId);
        }
        return menuParam;
    }

    private void reOpenMainMenu(String author, String charId, Integer messageId) throws TelegramApiException {
        deleteMessage(charId, messageId);
        telegramBotSend.sendMessage(author, charId, "Извините Меню устарело. Начните с начала");
        openMainMenu(author, charId);
    }

    private void sendReport(ReportTypeDto reportType, String author, String charId, Integer messageId, boolean sendMe) throws TelegramApiException {
        try {
            ResultMes resultMes = userServiceIntegration.checkUserTelegram(Long.parseLong(charId));
            if (resultMes.isOk()) {
                infoServiceIntegration.sendReport(reportType, author, sendMe ? Long.parseLong(charId) : null);
            }

            telegramBotSend.sendMessage(null, charId, resultMes.isOk() ? "Отчет будет доставлен в ближайшее время" : resultMes.getMessage());

        } catch (ResourceNotFoundRunTime ex) {
            telegramBotSend.sendMessage(null, charId, "Что-то пошло не так отчет не будет сформирован. Попробуйте позже или обратитесь к администратуру");
        }finally {
            deleteMessage(charId, messageId);
        }
    }

    private InlineKeyboardMarkup getMenuWorkStatus() {
        List<InlineKeyboardRow> rows = new LinkedList<>();

        List<TypeEnum> row = new LinkedList<>();
        row.add(CommandType.SEND_ME);
        row.add(CommandType.SEND_ALL);
        rows.add(createRow(row));
        addRowCancel(rows);
        return createMenu(rows);

    }

    private InlineKeyboardMarkup getMenuReport() {
        List<InlineKeyboardRow> rows = new LinkedList<>();
        for (ReportTypeDto typeDto : ReportTypeDto.values()) {
            List<TypeEnum> row = new LinkedList<>();
            row.add(typeDto);
            rows.add(createRow(row));
        }
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
        List<TypeEnum> row = new LinkedList<>();
        row.add(CommandType.CANCEL);
        return createRow(row);
    }

    private void addRowCancel(List<InlineKeyboardRow> rows) {
        rows.add(createRowCancel());

    }
}
