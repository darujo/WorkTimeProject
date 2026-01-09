package ru.darujo.dto.user;

import java.io.Serializable;
import java.util.List;

public class UserEditDto implements Serializable {
    @SuppressWarnings("unused")
    public UserEditDto() {
    }

    private Long id;
    private String nikName;

    private String firstName;

    private String lastName;

    private String patronymic;
    private String userPassword;
    private Boolean passwordChange;
    @SuppressWarnings("unused")
    private String textPassword;
    private List<Long> projects;
    private Boolean block;
    private Boolean admin;

    public UserEditDto(Long id, String nikName, String firstName, String lastName, String patronymic, String userPassword, Boolean passwordChange, List<Long> projects, Boolean block, Boolean admin) {
        this.id = id;
        this.nikName = nikName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.userPassword = userPassword;
        this.passwordChange = passwordChange;
        this.projects = projects;
        this.block = block;
        this.admin = admin;
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

    public String getTextPassword() {
        return textPassword;
    }

    public Boolean getPasswordChange() {
        return passwordChange;
    }

    public List<Long> getProjects() {
        return projects;
    }

    public Boolean isBlock() {
        return block;
    }

    public Boolean isAdmin() {
        return admin;
    }
}
