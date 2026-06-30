package ru.darujo.service;

import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import ru.darujo.dto.information.SendMessage;
import ru.darujo.dto.information.SendServiceInt;
import ru.darujo.dto.information.UserSendMessage;
import ru.darujo.exceptions.ResourceNotFoundRunTime;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;

@Slf4j
public class DefaultEmailService implements SendServiceInt {

    @Value("${spring.mail.username}")
    private String senderEmail;

    @PostConstruct
    public void init() {
        String[] toAddress = new String[1];
        toAddress[0] = "radies@rambler.ru";
        try {
            sendSimpleEmail(toAddress, "Запуск", "тест");
        } catch (ResourceNotFoundRunTime ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    public JavaMailSender emailSender;

    public DefaultEmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendSimpleEmail(String[] toAddress, String subject, String message) throws RuntimeException {

        try {
            sendEmailWithAttachment(toAddress, subject, message, null, null);
        } catch (FileNotFoundException | MessagingException ex) {
            log.error("Не получилось отправить письмо", ex);
        }
    }

    public void sendEmailWithAttachment(String[] toAddress, String subject, String message, String fileName, byte[] file) throws FileNotFoundException, MessagingException {
        try {
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            messageHelper.setFrom(senderEmail);
            messageHelper.setTo(toAddress);
            messageHelper.setSubject(subject);
            messageHelper.setText(message, true);
            if (fileName != null && file != null) {
                messageHelper.addAttachment(fileName, () -> new ByteArrayInputStream(file));
            }
            emailSender.send(mimeMessage);
        } catch (MailSendException ex) {
            throw new ResourceNotFoundRunTime(ex.getMessage());
        }
    }

    @Override
    public boolean sendMessage(SendMessage sendMessage) throws RuntimeException {
        boolean flagOk = true;
        if (!sendMessage.getUserSendMessages().isEmpty()) {
            String[] to = sendMessage.getUserSendMessages().stream().map(UserSendMessage::getChatId).toList().toArray(new String[0]);
            try {

                if (sendMessage.isAttachFile()) {
                    try {
                        sendEmailWithAttachment(to, sendMessage.getTitle(), sendMessage.getText(), sendMessage.getFileName(), sendMessage.getFileBody());
                    } catch (FileNotFoundException e) {
                        sendSimpleEmail(to, sendMessage.getTitle(), sendMessage.getText() + " Вложения не удалось отправить.");
                    }
                } else {
                    sendSimpleEmail(to, sendMessage.getTitle(), sendMessage.getText());
                }
            } catch (MessagingException e) {
                flagOk = false;
            }
        }
        return flagOk;
    }
}
