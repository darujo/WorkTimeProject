package ru.darujo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.darujo.model.MessageInformation;
import ru.darujo.repository.MessageInformationRepository;

@Service
@Primary
public class MessageInformationService {
    private MessageInformationRepository messageInformationRepository;
    @Autowired
    public void setMessageInformationRepository(MessageInformationRepository workTypeRepository) {
        this.messageInformationRepository = workTypeRepository;
    }

    public MessageInformation saveMessageInformation(MessageInformation messageInformation) {
        return messageInformationRepository.save(messageInformation);
    }

    public void deleteMessageInformation(Long id) {
        messageInformationRepository.deleteById(id);
    }


}
