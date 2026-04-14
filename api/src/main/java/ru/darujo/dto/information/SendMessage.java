package ru.darujo.dto.information;

import java.util.Collection;

public interface SendMessage {

    String getAuthor();
    String getTitle();
    String getText();
    boolean isAttachFile();
    String getFileName();
    String getFileBody();
    Collection<? extends UserSendMessage> getUserSendMessages();

}
