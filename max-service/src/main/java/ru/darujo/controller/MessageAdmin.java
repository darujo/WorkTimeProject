package ru.darujo.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.darujo.dto.information.SendAdminMessage;

@NoArgsConstructor
@Getter
public class MessageAdmin implements SendAdminMessage {
    @SuppressWarnings("unused")
    private String title;
    @SuppressWarnings("unused")
    private String text;
    @SuppressWarnings("unused")
    private boolean attachFile;
    @SuppressWarnings("unused")
    private String fileName;
    @SuppressWarnings("unused")
    private byte[] fileBody;
}
