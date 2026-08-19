package ru.darujo.service;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.darujo.dto.information.CodeTelegramMes;
import ru.darujo.dto.information.ResultMes;
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.model.User;
import ru.darujo.model.UserInfoType;
import ru.darujo.type.MessageSenderType;
import ru.darujo.type.MessageType;

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
    public CodeTelegramMes getGenSingleCode(String login, String senderType, String messageType, Long projectId) {

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
        SingleCode singleCode = new SingleCode(login, projectId, messageType, timestamp);
        mapCode.put(code, singleCode);
        if (senderType.equalsIgnoreCase("max")) {
            return new CodeTelegramMes(true, "max.ru/se13305836_bot", code, TIME_CODE);
        }
        return new CodeTelegramMes(true, "t.me/DaruWorkBot", code, TIME_CODE);

    }

    @Transactional
    public void clearMapCode(String login, String messageType) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        for (Map.Entry<Integer, SingleCode> entry : mapCode.entrySet()) {
            if (entry.getValue().timestamp().before(timestamp)
                    || (entry.getValue().messageType() != null && (entry.getValue().messageType().equals(messageType))
                    && entry.getValue().login().equals(login))) {
                mapCode.remove(entry.getKey());
            }
        }
    }

    public CodeTelegramMes getCode(String login, String messageType) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        for (Map.Entry<Integer, SingleCode> entry : mapCode.entrySet()) {
            if ((entry.getValue().messageType() != null && entry.getValue().messageType().equals(messageType) && entry.getValue().login().equals(login))) {
                return new CodeTelegramMes(true,
                        "t.me/DaruWorkBot",
                        entry.getKey(),
                        Math.toIntExact(TimeUnit.MILLISECONDS.toMinutes(entry.getValue().timestamp().getTime() - timestamp.getTime())));
            }
        }
        return null;
    }

    @Transactional
    public ResultMes linkCodeTelegram(Integer code, String senderType, String chatId, Integer threadId) {

        clearMapCode(null, null);
        SingleCode singleCode = mapCode.get(code);
        if (singleCode == null) {
            return new ResultMes(false, "Не такого кода авторизации или он просрочен ");
        }
        User user = userService.findByNikName(singleCode.login()).orElse(null);
        if (user == null) {
            return new ResultMes(false, "Пользователь не найден.");
        }

        if (singleCode.messageType() == null) {
            if (senderType.equalsIgnoreCase(MessageSenderType.Max.toString())) {
                user.setMaxId(chatId);
                userService.saveUser(user);
            } else if (senderType.equalsIgnoreCase(MessageSenderType.Telegram.toString())) {
                user.setTelegramId(chatId);
                userService.saveUser(user);
            }
        } else {
            UserInfoType userInfoType = userInfoTypeService
                    .getInfoTypeForUser(user, senderType, singleCode.projectId(), null, null, singleCode.messageType())
                    .orElse(new UserInfoType(singleCode.projectId(), singleCode.messageType(), user));
            userInfoType.setChatId(chatId);
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
    public void linkDeleteMessager(String senderType, String chatId, Integer threadId) {
        if (threadId == null) {
            if (senderType.equalsIgnoreCase(MessageSenderType.Telegram.toString())) {
                userService.getUserList(null, null, null, null, null, null, null, chatId, null, null, null, null)
                        .forEach(user -> {
                            user.setTelegramId(null);
                            userInfoTypeService.getInfoTypes(user, senderType, chatId, null, null)
                                    .forEach(userInfoType -> {
                                        userInfoType.setChatId(null);
                                        userInfoType.setThreadId(null);
                                        userInfoTypeService.save(userInfoType);
                                    });
                            userService.saveUser(user);
                        });
            } else if (senderType.equalsIgnoreCase(MessageSenderType.Max.toString())) {
                userService.getUserList(null, null, null, null, null, null, null, null, chatId, null, null, null)
                        .forEach(user -> {
                            user.setMaxId(null);
                            userInfoTypeService.getInfoTypes(user, senderType, chatId, null, null)
                                    .forEach(userInfoType -> {
                                        userInfoType.setChatId(null);
                                        userInfoType.setThreadId(null);
                                        userInfoTypeService.save(userInfoType);
                                    });
                            userService.saveUser(user);
                        });
            }

        }
        userInfoTypeService.getInfoTypes(null, senderType, chatId, threadId, null)
                .forEach(userInfoType -> {
                    userInfoType.setChatId(null);
                    userInfoType.setThreadId(null);
                    userInfoTypeService.save(userInfoType);
                });
        userService.setMessageTypeListMap();

    }

    public ResultMes checkUserMessager(String senderType, String chatId) {
        if (chatId == null) {
            return new ResultMes(false, "Нет ни одного пользователя с таким " + senderType);
        }
        boolean flag = userService.exists(senderType, chatId);
        if (!flag) {
            flag = userInfoTypeService.exists(senderType, chatId);
        }
        return new ResultMes(flag, flag ? "" : "Нет ни одного пользователя с таким " + senderType);
    }

    @Transactional
    public void linkDeleteMessager(String nikName, String senderType, String messageType) {
        User user = userService.findByNikName(nikName).orElseThrow(() -> new ResourceNotFoundRunTime("Не найден с логином " + nikName));
        if (senderType.equalsIgnoreCase(MessageSenderType.Max.toString())) {
            if (messageType == null) {
                user.setMaxId(null);
                userService.saveUser(user);
            }
        } else {
            if (messageType == null) {
                user.setTelegramId(null);
                userService.saveUser(user);
            }
        }
        userInfoTypeService.getInfoTypes(user, senderType, null, null, messageType)
                .forEach(userInfoType -> {
                    userInfoType.setChatId(null);
                    userInfoType.setThreadId(null);
                    userInfoTypeService.save(userInfoType);
                });
        userService.setMessageTypeListMap();
    }
}
