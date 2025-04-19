package ru.darujo.dto.user;

import java.io.Serializable;

public class UserEditDto implements Serializable {
    public UserEditDto() {
    }

    private Long id;
    private String nikName;

    private String firstName;

    private String lastName;

    private String patronymic;
    private String userPassword;

    public UserEditDto(Long id, String nikName, String firstName, String lastName, String patronymic,String userPassword) {
        this.id = id;
        this.nikName = nikName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.userPassword = userPassword;
    }

    public UserEditDto(String nikName) {
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

    public String getUserPassword() {
        return userPassword;
    }
}
