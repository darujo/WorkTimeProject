package ru.darujo.service;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.darujo.dto.information.CodeTelegramMes;
import ru.darujo.dto.information.MessageType;
import ru.darujo.dto.information.ResultMes;
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.model.User;
import ru.darujo.model.UserInfoType;

import java.sql.Timestamp;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
@Controller
public class LinkService {
    @SuppressWarnings("FieldCanBeLocal")
    private final Integer TIME_CODE = 5;

    private final Map<Integer, SingleCode> mapCode = new ConcurrentHashMap<>();
    private UserService userService;

    @PostConstruct
    public void init() {
        this.userService = UserService.getINSTANCE();
    }

    private UserInfoTypeService userInfoTypeService;

    @Autowired
    public void setUserInfoTypeService(UserInfoTypeService userInfoTypeService) {
        this.userInfoTypeService = userInfoTypeService;
    }

    @Transactional
    public CodeTelegramMes getGenSingleCode(String login, String messageType) {

        if (login == null) {
            throw new ResourceNotFoundRunTime("пройдите авторизацию");
        }
        userService.findByNikName(login).orElseThrow(() -> new ResourceNotFoundRunTime("Нет пользователя с логином"));
        if (messageType != null) {
            try {
                MessageType.valueOf(messageType);
            } catch (IllegalArgumentException ex) {
                throw new ResourceNotFoundRunTime("Нет типа уведомлений " + messageType);
            }
        }
        clearMapCode(login, messageType);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis() + TIME_CODE * 60 * 1000);
        int code = (int) ((99999999 * Math.random()));
        SingleCode singleCode = new SingleCode(login, messageType, timestamp);
        mapCode.put(code, singleCode);
        return new CodeTelegramMes(true, "t.me/DaruWorkBot", code, TIME_CODE);

    }

    @Transactional
    public void clearMapCode(String login, String messageType) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        for (Map.Entry<Integer, SingleCode> entry : mapCode.entrySet()) {
            if (entry.getValue().getTimestamp().before(timestamp)
                    || (entry.getValue().getMessageType().equals(messageType)
                    && entry.getValue().getLogin().equals(login))) {
                mapCode.remove(entry.getKey());
            }
        }
    }

    public CodeTelegramMes getCode(String login, String messageType) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        for (Map.Entry<Integer, SingleCode> entry : mapCode.entrySet()) {
            if ((entry.getValue().getMessageType().equals(messageType) && entry.getValue().getLogin().equals(login))) {
                return new CodeTelegramMes(true,
                        "t.me/DaruWorkBot",
                        entry.getKey(),
                        Math.toIntExact(TimeUnit.MILLISECONDS.toMinutes(entry.getValue().getTimestamp().getTime() - timestamp.getTime())));
            }
        }
        return null;
    }

    @Transactional
    public ResultMes linkCodeTelegram(Integer code, Long telegramId, Integer threadId) {

        clearMapCode(null, null);
        SingleCode singleCode = mapCode.get(code);
        if (singleCode == null) {
            return new ResultMes(false, "Не такого кода авторизации или он просрочен ");
        }
        User user = userService.findByNikName(singleCode.getLogin()).orElse(null);
        if (user == null) {
            return new ResultMes(false, "Пользователь не найден.");
        }

        if (singleCode.getMessageType() == null) {
            user.setTelegramId(telegramId);
            userService.saveUser(user);
        } else {
            UserInfoType userInfoType = userInfoTypeService
                    .getInfoTypeForUser(user, null, null, singleCode.getMessageType())
                    .orElse(new UserInfoType(singleCode.getMessageType(), user));
            userInfoType.setTelegramId(telegramId);
            userInfoType.setThreadId(threadId);
            userInfoTypeService.save(userInfoType);

        }
        mapCode.remove(code);
        if (userService.setMessageTypeListMap()) {
            return new ResultMes(true, "");
        }
        return new ResultMes(false, "Пользователь добавлен, но что-то не так и уведомления будут приходить, после перезапуска сервиса уведомлений, Обратитесь к администратору или ждите");

    }

    @Transactional
    public void linkDeleteTelegram(Long telegramId, Integer threadId) {
        if (threadId == null) {
            userService.getUserList(null, null, null, null, null, null, null, telegramId, null)
                    .forEach(user -> {
                        user.setTelegramId(null);
                        userInfoTypeService.getInfoTypes(user, null, null, null)
                                .forEach(userInfoType -> {
                                    userInfoType.setTelegramId(null);
                                    userInfoType.setThreadId(null);
                                    userInfoTypeService.save(userInfoType);
                                });
                        userService.saveUser(user);
                    });

        }
        userInfoTypeService.getInfoTypes(null, telegramId, threadId, null)
                .forEach(userInfoType -> {
                    userInfoType.setTelegramId(null);
                    userInfoType.setThreadId(null);
                    userInfoTypeService.save(userInfoType);
                });
        userService.setMessageTypeListMap();

    }

    public ResultMes checkUserTelegram(Long chatId) {
        if (chatId == null) {
            return new ResultMes(false, "Нет ни одного пользователя с таким телеграмм");
        }
        boolean flag = userService.exists(chatId);
        if (!flag) {
            flag = userInfoTypeService.exists(chatId);
        }
        return new ResultMes(flag, flag ? "" : "Нет ни одного пользователя с таким телеграмм");
    }

    public void linkDeleteTelegram(String nikName, String messageType) {
        User user = userService.findByNikName(nikName).orElseThrow(() -> new ResourceNotFoundRunTime("Не найден с логином " + nikName));

        userInfoTypeService.getInfoTypes(user, null, null, messageType)
                .forEach(userInfoType -> {
                    userInfoType.setTelegramId(null);
                    userInfoType.setThreadId(null);
                    userInfoTypeService.save(userInfoType);
                });
        userService.setMessageTypeListMap();
    }
}
