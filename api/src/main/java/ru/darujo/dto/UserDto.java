package ru.darujo.dto;

import java.io.Serializable;

public class UserDto implements Serializable {
    private long id;
    private String username;

    public UserDto(long id, String username) {
        this.id = id;
        this.username = username;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}
