package ru.darujo.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.darujo.integration.TelegramServiceIntegration;
import ru.darujo.model.MessageInformation;
import ru.darujo.model.SendMessageImp;
import ru.darujo.model.UserSend;
import ru.darujo.repository.MessageInformationRepository;
import ru.darujo.repository.UserSendRepository;
import ru.darujo.specifications.Specifications;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@Slf4j
public class SendService {
    private UserSendRepository userSendRepository;
    private FileService fileService;
    private TelegramServiceIntegration telegramServiceIntegration;
    private MessageInformationRepository messageInformationRepository;

    @Autowired
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    @Autowired
    public void setUserSendRepository(UserSendRepository userSendRepository) {
        this.userSendRepository = userSendRepository;
    }

    @Autowired
    public void setTelegramServiceIntegration(TelegramServiceIntegration telegramServiceIntegration) {
        this.telegramServiceIntegration = telegramServiceIntegration;
    }

    @Autowired
    public void setMessageInformationRepository(MessageInformationRepository messageInformationRepository) {
        this.messageInformationRepository = messageInformationRepository;
    }

    @Value("${file.save-into}")
    private String pathSave;

    public boolean sendMessage(MessageInformation messageInformation) {
        if (messageInformation.getFileForDisk() != null) {
            String body = fileService.getFileBody(pathSave + messageInformation.getFileForDisk());
            if (body != null && !body.isBlank()) {
                return fileSend(messageInformation, messageInformation.getFileForDisk(), body);
            } else {
                return fileSend(messageInformation, null, null);
            }
        } else {
            return fileSend(messageInformation, null, null);
        }

    }

    public boolean fileSend(MessageInformation messageInformation, String fileName, String fileBody) {
        List<UserSend> userSendList = userSendRepository.findAll(
                Specifications.eq(null, "messageInformation", messageInformation)
        );
        if (!telegramServiceIntegration.sendMessage(new SendMessageImp(messageInformation, userSendList, fileName, fileBody))) {
            if (fileBody != null) {
                saveFile(messageInformation, fileName, fileBody);
            }
            return false;
        }
        return true;
    }

    private void saveFile(MessageInformation messageInformation, String file, String body) {
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
            if (sendMessage(messageInformation)) {
                flagOk.set(false);
            }
        });
        return flagOk.get();
    }
}
