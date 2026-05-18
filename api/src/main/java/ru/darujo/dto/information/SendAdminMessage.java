package ru.darujo.dto.information;

public interface SendAdminMessage {

    String getTitle();

    String getText();

    default boolean isAttachFile() {
        return false;
    }

    default String getFileName() {
        return null;
    }

    default byte[] getFileBody() {
        return null;
    }

}
