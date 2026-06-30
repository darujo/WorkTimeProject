package ru.darujo.dto.information;

import ru.darujo.dto.user.UserInfoDto;
import ru.darujo.type.MessageSenderType;
import ru.darujo.type.MessageType;

import java.util.List;
import java.util.Map;

public class MapUserInfoDto {
    @SuppressWarnings("unused")
    public MapUserInfoDto() {
    }

    private Map<MessageType, Map<MessageSenderType, List<UserInfoDto>>> messageTypeListMap;

    public Map<MessageType, Map<MessageSenderType, List<UserInfoDto>>> getMessageTypeListMap() {
        return messageTypeListMap;
    }

    public MapUserInfoDto(Map<MessageType, Map<MessageSenderType, List<UserInfoDto>>> messageTypeListMap) {
        this.messageTypeListMap = messageTypeListMap;
    }
}
