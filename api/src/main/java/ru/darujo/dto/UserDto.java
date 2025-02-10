package ru.darujo.dto;

public class UserDto {
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
