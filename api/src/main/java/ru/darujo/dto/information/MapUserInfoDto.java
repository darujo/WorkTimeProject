package ru.darujo.dto.information;

import ru.darujo.dto.user.UserInfoDto;

import java.util.List;
import java.util.Map;

public class MapUserInfoDto {
    @SuppressWarnings("unused")
    public MapUserInfoDto() {
    }

    private Map<MessageType, List<UserInfoDto>> messageTypeListMap;

    public Map<MessageType, List<UserInfoDto>> getMessageTypeListMap() {
        return messageTypeListMap;
    }

    public MapUserInfoDto(Map<MessageType, List<UserInfoDto>> messageTypeListMap) {
        this.messageTypeListMap = messageTypeListMap;
    }
}
