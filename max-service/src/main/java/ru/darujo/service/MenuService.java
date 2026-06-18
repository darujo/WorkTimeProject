package ru.darujo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.darujo.dto.information.ResultMes;
import ru.darujo.integration.InfoServiceIntegrationImp;
import ru.darujo.integration.UserServiceIntegrationImp;
import ru.darujo.model.ChatInfo;
import ru.darujo.telegram_bot.MaxBotSend;
import ru.darujo.type.MessageSenderType;
import ru.darujo.type.ReportType;
import ru.darujo.type.TypeEnum;
import ru.max.botapi.model.Message;

import java.io.File;
import java.util.*;

@Service
public class MenuService {
    private MaxBotSend maxBotSend;

    @Autowired
    public void setTelegramBotSend(MaxBotSend maxBotSend) {
        this.maxBotSend = maxBotSend;
    }

    private InfoServiceIntegrationImp infoServiceIntegration;

    @Autowired
    public void setInfoServiceIntegration(InfoServiceIntegrationImp infoServiceIntegration) {
        this.infoServiceIntegration = infoServiceIntegration;
    }

    private UserServiceIntegrationImp userServiceIntegration;

    @Autowired
    public void setUserServiceIntegration(UserServiceIntegrationImp userServiceIntegration) {
        this.userServiceIntegration = userServiceIntegration;
    }

    private FileService fileService;

    @Autowired
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

//    private InlineKeyboardMarkup createMenu(List<InlineKeyboardRow> rows) {
//        return new InlineKeyboardMarkup(rows);
//    }
//
//    private InlineKeyboardRow createRow(List<TypeEnum> commands) {
//        List<InlineKeyboardButton> buttons = new ArrayList<>();
//
//        commands.forEach((command) ->
//                buttons.add(InlineKeyboardButton.builder()
//                        .text(command.getName())
//                        .callbackData(command.toString())
//                        .build()));
//        return new InlineKeyboardRow(buttons);
//    }
//
//    public InlineKeyboardMarkup getMainMenu() {
//        List<InlineKeyboardRow> rows = new LinkedList<>();
//
//        List<TypeEnum> row = new LinkedList<>();
//        row.add(CommandType.LINK);
//        row.add(CommandType.STOP);
//        rows.add(createRow(row));
//
//        row = new LinkedList<>();
//        row.add(CommandType.REPORT);
//        rows.add(createRow(row));
//        addRowCancel(rows);
//        return createMenu(rows);
//
//    }

    public void openMainMenu(ChatInfo chatInfo) {
//todo исправить меню
        maxBotSend.sendPhoto(chatInfo, fileService.getFile("menu"), "Чего желаете?", /*getMainMenu()*/ null);
    }

    Map<Integer, MenuParam> paramMap = new HashMap<>();

    public void getMenu(ChatInfo chatInfo, String command, File file) {
        try {
            CommandType commandType = CommandType.valueOf(command);
            getMenu(chatInfo, commandType, file);
        } catch (IllegalArgumentException ex) {
            MenuParam menuParam = getMenuParam(chatInfo);
            if (menuParam == null) {
                return;
            }
            try {

                menuParam.setReportType(ReportType.valueOf(command));
                // todo меню рассылок
                maxBotSend.EditPhoto(chatInfo, "Кому разослать результат по отчету " + command + "?", null/*getMenuWorkStatus()*/, file);
            } catch (IllegalArgumentException illegalArgumentException) {
                reOpenMainMenu(chatInfo);
            }

        }

    }

    public void getMenu(ChatInfo chatInfo, CommandType command, File file){
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
                // todo меню отчетов
                maxBotSend.EditPhoto(chatInfo, "Какой отчет вы хотите построить?", /*getMenuReport()*/ null, file);
            } else {
                maxBotSend.deleteMessage(chatInfo);
                maxBotSend.sendMessage(chatInfo, resultMes.getMessage());
            }
        }
        if (command.equals(CommandType.SEND_ME)) {
            sendReport(Objects.requireNonNull(menuParam).getReportType(), chatInfo, true);
        }
        if (command.equals(CommandType.SEND_ALL)) {
            sendReport(Objects.requireNonNull(menuParam).getReportType(), chatInfo, false);
        }
        if (command.equals(CommandType.CANCEL)) {
            deleteMessage(chatInfo);
        }

    }

    private void deleteMessage(ChatInfo chatInfo) {
        paramMap.remove(chatInfo.getOriginMessageId());
        maxBotSend.deleteMessage(chatInfo);
    }

    private MenuParam getMenuParam(ChatInfo chatInfo) {
        MenuParam menuParam = paramMap.get(chatInfo.getOriginMessageId());
        if (menuParam == null) {
            reOpenMainMenu(chatInfo);
        }
        return menuParam;
    }

    private void reOpenMainMenu(ChatInfo chatInfo) {
        deleteMessage(chatInfo);
        maxBotSend.sendMessage(chatInfo, "Извините Меню устарело. Начните с начала");
        openMainMenu(chatInfo);
    }

    private void sendReport(ReportType reportType, ChatInfo chatInfo, boolean sendMe)  {
        deleteMessage(chatInfo);
        try {
            ResultMes resultMes = userServiceIntegration.checkUserTelegram(Long.parseLong(chatInfo.getChatId()));
            if (resultMes.isOk()) {
                Message message = maxBotSend.sendMessage(chatInfo, "Отчет \"" + reportType.getName() + "\" будет доставлен в ближайшее время");
 // todo на что заменить?
                // chatInfo.setOriginMessageId(message.getMessageId());
                if (sendMe) {
                    infoServiceIntegration.sendReport(reportType, chatInfo.getAuthor(), MessageSenderType.Telegram, chatInfo.getChatId(), chatInfo.getThreadId(), chatInfo.getOriginMessageId());
                } else {
                    infoServiceIntegration.sendReport(reportType, chatInfo.getAuthor(), null, null, null, null);
                }
            } else {
                maxBotSend.sendMessage(chatInfo, resultMes.getMessage());
            }
        } catch (RuntimeException ex) {
            maxBotSend.deleteMessage(chatInfo);
            maxBotSend.sendMessage(chatInfo, "Что-то пошло не так отчет не будет сформирован. Попробуйте позже или обратитесь к администратору");
        }
    }

//    private InlineKeyboardMarkup getMenuWorkStatus() {
//        List<InlineKeyboardRow> rows = new LinkedList<>();
//
//        List<TypeEnum> row = new LinkedList<>();
//        row.add(CommandType.SEND_ME);
//        row.add(CommandType.SEND_ALL);
//        rows.add(createRow(row));
//        addRowCancel(rows);
//        return createMenu(rows);
//
//    }
//
//    private InlineKeyboardMarkup getMenuReport() {
//        List<InlineKeyboardRow> rows = new LinkedList<>();
//        for (ReportType typeDto : ReportType.values()) {
//            List<TypeEnum> row = new LinkedList<>();
//            row.add(typeDto);
//            rows.add(createRow(row));
//        }
//        addRowCancel(rows);
//        return createMenu(rows);
//
//    }
//
//    private InlineKeyboardMarkup getMenuCancel() {
//        List<InlineKeyboardRow> rows = new LinkedList<>();
//
//        rows.add(createRowCancel());
//
//        return createMenu(rows);
//
//    }

    public void openCancel(ChatInfo chatInfo, String text)  {
        // todo отмена
        maxBotSend.sendMessage(chatInfo, text, /*getMenuCancel()*/ null);
    }

//    private InlineKeyboardRow createRowCancel() {
//        List<TypeEnum> row = new LinkedList<>();
//        row.add(CommandType.CANCEL);
//        return createRow(row);
//    }
//
//    private void addRowCancel(List<InlineKeyboardRow> rows) {
//        rows.add(createRowCancel());
//
//    }
}
