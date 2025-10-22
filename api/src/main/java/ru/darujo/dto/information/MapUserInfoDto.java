package ru.darujo.dto.information;

import ru.darujo.dto.user.UserTelegramDto;

import java.util.List;
import java.util.Map;

public class MapUserInfoDto {
    @SuppressWarnings("unused")
    public MapUserInfoDto() {
    }

    private Map<MessageType, List<UserTelegramDto>> messageTypeListMap;

    public Map<MessageType, List<UserTelegramDto>> getMessageTypeListMap() {
        return messageTypeListMap;
    }

    public MapUserInfoDto(Map<MessageType, List<UserTelegramDto>> messageTypeListMap) {
        this.messageTypeListMap = messageTypeListMap;
    }
}
