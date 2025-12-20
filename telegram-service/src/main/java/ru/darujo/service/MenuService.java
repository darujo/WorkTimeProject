package ru.darujo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.darujo.dto.information.ResultMes;
import ru.darujo.integration.InfoServiceIntegration;
import ru.darujo.integration.UserServiceIntegration;
import ru.darujo.model.ChatInfo;
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

    public void openMainMenu(ChatInfo chatInfo) throws TelegramApiException {

        telegramBotSend.sendPhoto(chatInfo, fileService.getFile("menu"), "Чего желаете?", getMainMenu());
    }

    Map<Integer, MenuParam> paramMap = new HashMap<>();

    public void getMenu(ChatInfo chatInfo, String command, File file) throws TelegramApiException {
        try {
            CommandType commandType = CommandType.valueOf(command);
            getMenu(chatInfo, commandType, file);
        } catch (IllegalArgumentException ex) {
            MenuParam menuParam = getMenuParam(chatInfo);
            if (menuParam == null) {
                return;
            }
            try {

                menuParam.setReportTypeDto(ReportTypeDto.valueOf(command));
                telegramBotSend.EditPhoto(chatInfo, "Кому разослать результат по отчету " + command + "?", getMenuWorkStatus(), file);
            } catch (IllegalArgumentException illegalArgumentException) {
                reOpenMainMenu(chatInfo);
            }

        }

    }

    public void getMenu(ChatInfo chatInfo, CommandType command, File file) throws TelegramApiException {
        MenuParam menuParam = null;

        if (command.getNewParam()) {
            menuParam = new MenuParam();
            paramMap.put(chatInfo.getOriginMessageId(), menuParam);
        } else {
            if (command.getAvailParam()) {
                menuParam = getMenuParam(chatInfo);
            }

        }

        if (command.equals(CommandType.REPORT)) {
            ResultMes resultMes = userServiceIntegration.checkUserTelegram(Long.parseLong(chatInfo.getChatId()));
            if (resultMes.isOk()) {
                telegramBotSend.EditPhoto(chatInfo, "Какой отчет вы хотите построить?", getMenuReport(), file);
            } else {
                telegramBotSend.deleteMessage(chatInfo);
                telegramBotSend.sendMessage(chatInfo, resultMes.getMessage());
            }
        }
        if (command.equals(CommandType.SEND_ME)) {
            sendReport(Objects.requireNonNull(menuParam).getReportTypeDto(), chatInfo, true);
        }
        if (command.equals(CommandType.SEND_ALL)) {
            sendReport(Objects.requireNonNull(menuParam).getReportTypeDto(), chatInfo, false);
        }
        if (command.equals(CommandType.CANCEL)) {
            deleteMessage(chatInfo);
        }

    }

    private void deleteMessage(ChatInfo chatInfo) throws TelegramApiException {
        paramMap.remove(chatInfo.getOriginMessageId());
        telegramBotSend.deleteMessage(chatInfo);
    }

    private MenuParam getMenuParam(ChatInfo chatInfo) throws TelegramApiException {
        MenuParam menuParam = paramMap.get(chatInfo.getOriginMessageId());
        if (menuParam == null) {
            reOpenMainMenu(chatInfo);
        }
        return menuParam;
    }

    private void reOpenMainMenu(ChatInfo chatInfo) throws TelegramApiException {
        deleteMessage(chatInfo);
        telegramBotSend.sendMessage(chatInfo, "Извините Меню устарело. Начните с начала");
        openMainMenu(chatInfo);
    }

    private void sendReport(ReportTypeDto reportType, ChatInfo chatInfo, boolean sendMe) throws TelegramApiException {
        deleteMessage(chatInfo);
        try {
            ResultMes resultMes = userServiceIntegration.checkUserTelegram(Long.parseLong(chatInfo.getChatId()));
            if (resultMes.isOk()) {
                Message message = telegramBotSend.sendMessage(chatInfo, "Отчет \"" + reportType.getName() + "\" будет доставлен в ближайшее время");
                chatInfo.setOriginMessageId(message.getMessageId());
                if (sendMe) {
                    infoServiceIntegration.sendReport(reportType, chatInfo.getAuthor(), Long.parseLong(chatInfo.getChatId()), chatInfo.getThreadId(), chatInfo.getOriginMessageId());
                } else {
                    infoServiceIntegration.sendReport(reportType, chatInfo.getAuthor(), null, null, null);
                }
            } else {
                telegramBotSend.sendMessage(chatInfo, resultMes.getMessage());
            }
        } catch (RuntimeException ex) {
            telegramBotSend.deleteMessage(chatInfo);
            telegramBotSend.sendMessage(chatInfo, "Что-то пошло не так отчет не будет сформирован. Попробуйте позже или обратитесь к администратору");
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

    public void openCancel(ChatInfo chatInfo, String text) throws TelegramApiException {
        telegramBotSend.sendMessage(chatInfo, text, getMenuCancel());
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
