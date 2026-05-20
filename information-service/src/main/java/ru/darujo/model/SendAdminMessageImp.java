package ru.darujo.model;

import ru.darujo.dto.information.SendAdminMessage;

public class SendAdminMessageImp implements SendAdminMessage {
    @SuppressWarnings("unused")
    private String title;
    @SuppressWarnings("unused")
    private String text;
    @SuppressWarnings("unused")
    private String fileName;
    @SuppressWarnings("unused")
    private byte[] fileBody;

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public byte[] getFileBody() {
        return fileBody;
    }

    @Override
    public boolean isAttachFile() {
        return true;
    }
}
