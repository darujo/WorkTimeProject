package ru.darujo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.darujo.dto.information.ResultMes;
import ru.darujo.integration.InfoServiceIntegrationImp;
import ru.darujo.integration.UserServiceIntegrationImp;
import ru.darujo.max_bot.MaxBotSend;
import ru.darujo.model.ChatInfo;
import ru.darujo.type.MessageSenderType;
import ru.darujo.type.ReportType;
import ru.darujo.type.TypeEnum;
import ru.max.botapi.model.*;

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

    private FileSaverService fileSaverService;

    @Autowired
    public void setFileService(FileSaverService fileSaverService) {
        this.fileSaverService = fileSaverService;
    }

    private InlineKeyboardAttachmentRequest createMenu(List<List<Button>> rows) {
        InlineKeyboardAttachment.KeyboardPayload payload =
                new InlineKeyboardAttachment.KeyboardPayload(rows);
        return new InlineKeyboardAttachmentRequest(payload);
    }

    private List<Button> createRow(List<TypeEnum> commands) {
        List<Button> buttons = new ArrayList<>();

        commands.forEach((command) ->
                buttons.add(new CallbackButton(command.getName(), command.toString(), ButtonIntent.POSITIVE)

                ));
        return buttons;
    }

    public InlineKeyboardAttachmentRequest getMainMenu() {
        List<List<Button>> rows = new LinkedList<>();

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

    public void openMainMenu(ChatInfo chatInfo) {
        List<AttachmentRequest> menu = new ArrayList<>();
        menu.add(getMainMenu());
        maxBotSend.sendPhoto(chatInfo, fileSaverService.getFile("menu"), "Чего желаете?", menu);
    }

    Map<String, MenuParam> paramMap = new HashMap<>();

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
                List<AttachmentRequest> menu = new ArrayList<>();
                menu.add(getMenuWorkStatus());
                maxBotSend.EditPhoto(chatInfo, "Кому разослать результат по отчету " + command + "?", menu, file);
            } catch (IllegalArgumentException illegalArgumentException) {
                reOpenMainMenu(chatInfo);
            }

        }

    }

    public void getMenu(ChatInfo chatInfo, CommandType command, File file) {
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
            ResultMes resultMes = userServiceIntegration.checkUserTelegram(MessageSenderType.Max, Long.parseLong(chatInfo.getChatId()));
            if (resultMes.isOk()) {
                List<AttachmentRequest> menu = new ArrayList<>();
                menu.add(getMenuReport());
                maxBotSend.EditPhoto(chatInfo, "Какой отчет вы хотите построить?", menu, file);
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

    private void sendReport(ReportType reportType, ChatInfo chatInfo, boolean sendMe) {
        deleteMessage(chatInfo);
        try {
            ResultMes resultMes = userServiceIntegration.checkUserTelegram(MessageSenderType.Max, Long.parseLong(chatInfo.getChatId()));
            if (resultMes.isOk()) {
                SendMessageResult message = maxBotSend.sendMessage(chatInfo, "Отчет \"" + reportType.getName() + "\" будет доставлен в ближайшее время");
                // todo на что заменить?
                chatInfo.setOriginMessageId(message.message().body().mid());
                if (sendMe) {
                    infoServiceIntegration.sendReport(reportType, chatInfo.getAuthor(), MessageSenderType.Max, chatInfo.getChatId(), null, chatInfo.getOriginMessageId());
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

    private InlineKeyboardAttachmentRequest getMenuWorkStatus() {
        List<List<Button>> rows = new LinkedList<>();

        List<TypeEnum> row = new LinkedList<>();
        row.add(CommandType.SEND_ME);
        row.add(CommandType.SEND_ALL);
        rows.add(createRow(row));
        addRowCancel(rows);
        return createMenu(rows);

    }

    private InlineKeyboardAttachmentRequest getMenuReport() {
        List<List<Button>> rows = new LinkedList<>();
        for (ReportType typeDto : ReportType.values()) {
            List<TypeEnum> row = new LinkedList<>();
            row.add(typeDto);
            rows.add(createRow(row));
        }
        addRowCancel(rows);
        return createMenu(rows);

    }

    private InlineKeyboardAttachmentRequest getMenuCancel() {
        List<List<Button>> rows = new LinkedList<>();

        rows.add(createRowCancel());

        return createMenu(rows);

    }

    public void openCancel(ChatInfo chatInfo, String text) {
        List<AttachmentRequest> menu = new ArrayList<>();
        menu.add(getMenuCancel());
        maxBotSend.sendMessage(chatInfo, text, menu);
    }

    private List<Button> createRowCancel() {
        List<TypeEnum> row = new LinkedList<>();
        row.add(CommandType.CANCEL);
        return createRow(row);
    }

    private void addRowCancel(List<List<Button>> rows) {
        rows.add(createRowCancel());

    }
}
