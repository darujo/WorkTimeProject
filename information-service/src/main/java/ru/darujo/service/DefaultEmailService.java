package ru.darujo.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.ResourceUtils;
import ru.darujo.dto.information.SendMessage;
import ru.darujo.dto.information.SendServiceInt;
import ru.darujo.dto.information.UserSendMessage;

import java.io.FileNotFoundException;

public class DefaultEmailService implements SendServiceInt {

    @Value("${spring.mail.username}")
    private String senderEmail;
    public JavaMailSender emailSender;

    public DefaultEmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendSimpleEmail(String[] toAddress, String subject, String message) {

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(senderEmail);
        simpleMailMessage.setTo(toAddress);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);
        emailSender.send(simpleMailMessage);
    }

    public void sendEmailWithAttachment(String[] toAddress, String subject, String message, String attachment) throws FileNotFoundException, MessagingException {

        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
        messageHelper.setTo(toAddress);
        messageHelper.setSubject(subject);
        messageHelper.setText(message);
        FileSystemResource file = new FileSystemResource(ResourceUtils.getFile(attachment));
        messageHelper.addAttachment("Purchase Order", file);
        emailSender.send(mimeMessage);
    }

    @Override
    public boolean sendMessage(SendMessage sendMessage) throws RuntimeException {
        boolean flagOk = true;
        if (!sendMessage.getUserSendMessages().isEmpty()) {
            String[] to = (String[]) sendMessage.getUserSendMessages().stream().map(UserSendMessage::getChatId).toArray();
            try {

                if (sendMessage.isAttachFile()) {
                    try {
                        sendEmailWithAttachment(to, sendMessage.getTitle(), sendMessage.getText(), sendMessage.getFileName());
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
