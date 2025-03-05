package ru.darujo.dto;

import java.io.Serializable;

public class UserDto implements Serializable {
    private long id;
    private String nikName;

    private String firstName;

    private String lastName;

    private String patronymic;

    public UserDto(long id, String nikName, String firstName, String lastName, String patronymic) {
        this.id = id;
        this.nikName = nikName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
    }

    public long getId() {
        return id;
    }

    public String getNikName() {
        return nikName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }
}
