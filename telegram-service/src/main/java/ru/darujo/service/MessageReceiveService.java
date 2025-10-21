package ru.darujo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.darujo.model.MessageReceive;
import ru.darujo.repository.MessageReceiveRepository;

@Service
public class MessageReceiveService {

    private MessageReceiveRepository messageReceiveRepository;

    @Autowired
    public void setMessageReceiveRepository(MessageReceiveRepository messageReceiveRepository) {
        this.messageReceiveRepository = messageReceiveRepository;
    }

    public MessageReceive saveMessageReceive (MessageReceive messageReceive){
        return messageReceiveRepository.save(messageReceive);
    }

}
