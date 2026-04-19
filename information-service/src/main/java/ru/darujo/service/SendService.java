package ru.darujo.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.darujo.dto.information.SendServiceInt;
import ru.darujo.model.MessageInformation;
import ru.darujo.model.SendMessageImp;
import ru.darujo.model.UserSend;
import ru.darujo.repository.MessageInformationRepository;
import ru.darujo.repository.UserSendRepository;
import ru.darujo.specifications.Specifications;
import ru.darujo.type.MessageSenderType;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@Slf4j
public class SendService {
    private UserSendRepository userSendRepository;
    private FileService fileService;
    private MessageInformationRepository messageInformationRepository;
    Map<MessageSenderType, SendServiceInt> senderList = new HashMap<>();

    @Autowired
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    @Autowired
    public void setUserSendRepository(UserSendRepository userSendRepository) {
        this.userSendRepository = userSendRepository;
    }

    @Autowired
    public void setTelegramServiceIntegration(SendServiceInt telegramServiceIntegration) {
        senderList.put(MessageSenderType.Telegram, telegramServiceIntegration);
    }

    @Autowired
    @Qualifier("mailServiceIntegration")
    public void setMailServiceIntegration(SendServiceInt mailServiceIntegration) {
        senderList.put(MessageSenderType.Email, mailServiceIntegration);
    }

    @Autowired

    public void setMessageInformationRepository(MessageInformationRepository messageInformationRepository) {
        this.messageInformationRepository = messageInformationRepository;
    }

    @Value("${file.save-into}")
    private String pathSave;

    public boolean sendMessage(MessageInformation messageInformation) {
        if (messageInformation.getFileForDisk() != null) {
            byte[] body = fileService.getFileBody(pathSave + messageInformation.getFileForDisk());
            if (body != null && body.length > 0) {
                return fileSend(messageInformation, messageInformation.getFileForDisk(), body);
            } else {
                return fileSend(messageInformation, null, null);
            }
        } else {
            return fileSend(messageInformation, null, null);
        }

    }

    public boolean fileSend(MessageInformation messageInformation, String fileName, byte[] fileBody) {
        AtomicBoolean flagOk = new AtomicBoolean(true);
        senderList.forEach((senderType, sendServiceInt) -> {
            Specification<UserSend> sp = Specification.unrestricted();
            sp = Specifications.eq(sp, "messageInformation", messageInformation);
            sp = Specifications.eq(sp, "senderType", senderType.toString());
            sp = Specifications.eq(sp, "send", false);
            List<UserSend> userSendList = userSendRepository.findAll(sp);
            if (!userSendList.isEmpty()) {
                if (!sendServiceInt.sendMessage(new SendMessageImp(messageInformation, userSendList, fileName, fileBody))) {
                    if (fileBody != null && messageInformation.getFileForDisk() == null) {
                        saveFile(messageInformation, fileName, fileBody);
                    }
                    flagOk.set(false);
                } else {
                    userSendList.forEach(userSend -> userSend.setSend(true));

                }
                userSendRepository.saveAll(userSendList.stream().filter(UserSend::getSend).toList());
            }
        });
        return flagOk.get();
    }

    private void saveFile(MessageInformation messageInformation, String file, byte[] body) {
        fileService.saveFile(pathSave + file, body);
        messageInformation.setText("Не удалось доставить до вас файл ранее. " + messageInformation.getText());
        messageInformation.setFileForDisk(file);
        messageInformationRepository.save(messageInformation);
        ScheduleService.getINSTANCE().sendMes();
    }

    @Transactional
    public boolean sendAllNotSendMessage() {
        Specification<@NonNull UserSend> specification = Specifications.ne(null, "send", true);
        AtomicBoolean flagOk = new AtomicBoolean(true);
        Set<MessageInformation> messageInformationSet = new HashSet<>();
        userSendRepository.findAll(specification).forEach(
                userSend ->
                        messageInformationSet.add(userSend.getMessageInformation())
        );
        messageInformationSet.forEach(messageInformation -> {
            if (!sendMessage(messageInformation)) {
                flagOk.set(false);
            }
        });
        return flagOk.get();
    }
}
