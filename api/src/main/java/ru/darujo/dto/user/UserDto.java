package ru.darujo.dto.user;

import java.io.Serializable;

public class UserDto implements Serializable {
    public UserDto() {
    }

    private Long id;
    private String nikName;

    private String firstName;

    private String lastName;

    private String patronymic;
    private Boolean passwordChange;
    private Boolean telegramAdd;

    public UserDto(Long id, String nikName, String firstName, String lastName, String patronymic,Boolean passwordChange, Boolean telegramAdd) {
        this.id = id;
        this.nikName = nikName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.passwordChange = passwordChange;
        this.telegramAdd = telegramAdd;
    }
    public UserDto(Long id, String nikName, String firstName, String lastName, String patronymic) {
        this.id = id;
        this.nikName = nikName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
    }
    public UserDto(String nikName) {
        this.nikName = nikName;
    }

    public Long getId() {
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


    @SuppressWarnings("unused")
    public Boolean getPasswordChange() {
        return passwordChange;
    }

    @SuppressWarnings("unused")
    public Boolean getTelegramAdd() {
        return telegramAdd;
    }
}
