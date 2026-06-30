package ru.darujo.dto.information;

import java.util.Collection;

public interface SendMessage {

    String getAuthor();
    String getTitle();
    String getText();
    boolean isAttachFile();
    String getFileName();

    byte[] getFileBody();
    Collection<? extends UserSendMessage> getUserSendMessages();

}
