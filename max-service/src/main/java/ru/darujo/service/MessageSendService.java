package ru.darujo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.darujo.model.MessageSend;
import ru.darujo.repository.MessageSendRepository;

@Service
public class MessageSendService {

    private MessageSendRepository messageSendRepository;

    @Autowired
    public void setMessageSendRepository(MessageSendRepository messageSendRepository) {
        this.messageSendRepository = messageSendRepository;
    }


    public void saveMessageSend(MessageSend messageSend) {
        messageSendRepository.save(messageSend);
    }


}
