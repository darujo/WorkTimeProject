package ru.darujo.integration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import ru.darujo.dto.information.SendAdminMessage;
import ru.darujo.dto.information.SendMessage;
import ru.darujo.dto.information.SendServiceInt;
import ru.darujo.exceptions.ResourceNotFoundRunTime;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public abstract class MessagerServiceIntegrationImp extends ServiceIntegrationImp<ServiceType> implements SendServiceInt, AdminInfoService {

    public void sendMessage(
            String author,
            String chatId,
            Integer threadId,
            String originMessageId,
            String text) {
        try {
            if (text == null) {
                return;
            }
            StringBuilder sb = new StringBuilder();
            addTeg(sb, "threadId", threadId);
            addTeg(sb, "originMessageId", originMessageId);
            String uri = "/" + chatId + "/notifications" + sb;
            log.debug(uri);
            webClient.post().uri(uri)
                    .header("username", author)
                    .bodyValue(text)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            cR -> getMessage(cR, uri + " Что-то пошло не так не удалось получить ответ от сервиса telegram"))
                    .bodyToMono(Void.class)
                    .doOnError(throwable -> log.error(throwable.getMessage()))
                    .block();
        } catch (RuntimeException ex) {
            log.error(ex.getMessage(), ex);
            throw new ResourceNotFoundRunTime("(Api-Telegram) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }

    public void sendMessageForAdmin(SendAdminMessage message) {
        try {
            String uri = "/send/admin";
            log.debug(uri);
            webClient.post().uri(uri)
                    .bodyValue(message)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            cR -> getMessage(cR, uri + " Что-то пошло не так не удалось получить ответ от сервиса telegram"))
                    .bodyToMono(Void.class)
                    .doOnError(throwable -> log.error(throwable.getMessage()))
                    .block();
        } catch (RuntimeException ex) {
            log.error(ex.getMessage(), ex);
            throw new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить работы (Api-Telegram) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }

    private void addFile(
            String fileName,
            byte[] textFile) {
        try {
            StringBuilder sb = new StringBuilder();
            addTeg(sb, "fileName", fileName);
            String uri = "/file" + sb;
            log.debug(uri);
            webClient.post().uri(uri)
                    .bodyValue(textFile)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            cR -> getMessage(cR, uri + " Что-то пошло не так не удалось получить ответ от сервиса telegram"))
                    .bodyToMono(Void.class)
                    .doOnError(throwable -> log.error(throwable.getMessage()))
                    .block();
        } catch (RuntimeException ex) {
            log.error(ex.getMessage(), ex);
            throw new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить работы (Api-Telegram) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }

    private void sendFile(
            String author,
            String chatId,
            Integer threadId,
            String originMessageId,
            String fileName,

            String text) {
        try {
            StringBuilder sb = new StringBuilder();
            addTeg(sb, "fileName", fileName);
            addTeg(sb, "threadId", threadId);
            addTeg(sb, "originMessageId", originMessageId);
            String uri = "/" + chatId + "/file" + sb;
            log.debug(uri);

            webClient.post().uri(uri)
                    .header("username", author)
                    .bodyValue(text)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            cR -> getMessage(cR, uri + " Что-то пошло не так не удалось получить ответ от сервиса telegram"))
                    .bodyToMono(Void.class)
                    .doOnError(throwable -> log.error(throwable.getMessage()))
                    .block();
        } catch (RuntimeException ex) {
            log.error(ex.getMessage(), ex);
            throw new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить работы (Api-Telegram) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }

    private void deleteFile(
            String fileName) {
        try {
            StringBuilder sb = new StringBuilder();
            addTeg(sb, "fileName", fileName);
            String uri = "/file" + sb;
            log.debug(uri);
            webClient.delete().uri(uri)
                    .retrieve()
                    .onStatus(httpStatus -> httpStatus.value() == HttpStatus.NOT_FOUND.value(),
                            cR -> getMessage(cR, uri + " Что-то пошло не так не удалось получить ответ от сервиса telegram"))
                    .bodyToMono(Void.class)
                    .doOnError(throwable -> log.error(throwable.getMessage()))
                    .block();
        } catch (RuntimeException ex) {
            log.error(ex.getMessage(), ex);
            throw new ResourceNotFoundRunTime("Что-то пошло не так не удалось получить работы (Api-Telegram) не доступен подождите или обратитесь к администратору " + ex.getMessage());
        }
    }

    public boolean sendMessage(SendMessage sendMessage) {
        if (sendMessage.isAttachFile()) {
            return sendFile(sendMessage);
        } else {
            AtomicBoolean flagOk = new AtomicBoolean(true);
            sendMessage.getUserSendMessages().forEach(userSendMessage -> {
                try {
                    sendMessage(sendMessage.getAuthor(), userSendMessage.getChatId(), userSendMessage.getThreadId(), userSendMessage.getOriginMessageId(), sendMessage.getText());
                } catch (ResourceNotFoundRunTime ex) {
                    flagOk.set(false);
                }
            });
            return flagOk.get();
        }


    }

    private boolean sendFile(SendMessage sendMessage) {
        boolean flagSendFile = false;
        try {
            addFile(sendMessage.getFileName(), sendMessage.getFileBody());
            flagSendFile = true;
            AtomicReference<Boolean> flagError = new AtomicReference<>(false);
            sendMessage.getUserSendMessages()
                    .forEach(userSend -> {
                        try {
                            sendFile(sendMessage.getAuthor(), userSend.getChatId(), userSend.getThreadId(), userSend.getOriginMessageId(), sendMessage.getFileName(), sendMessage.getText());
                            userSend.setSend();
                        } catch (ResourceNotFoundRunTime ex) {
                            log.error("Сбой отправки файла пользователю с chatId {}", userSend.getChatId(), ex);
                            flagError.set(true);
                        }
                    });

            return !flagError.get();

        } catch (ResourceNotFoundRunTime ex) {
            log.error("Сбой при отправке файла {} {}", sendMessage.getFileName(), ex.getMessage(), ex);
            return false;
        } finally {
            if (flagSendFile) {
                try {
                    deleteFile(sendMessage.getFileName());
                } catch (ResourceNotFoundRunTime ex) {
                    log.error("Сбой при удаление файл из сервиса {} {}", sendMessage.getFileName(), ex.getMessage(), ex);
                }
            }

        }
    }

}
