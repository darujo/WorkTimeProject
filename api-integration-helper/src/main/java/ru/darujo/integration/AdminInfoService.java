package ru.darujo.integration;

import ru.darujo.dto.information.SendAdminMessage;

public interface AdminInfoService {
    void sendMessageForAdmin(SendAdminMessage message);
}
