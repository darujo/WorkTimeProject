package ru.darujo.dto.user;

import ru.darujo.dto.project.ProjectDto;

import java.io.Serializable;
import java.util.List;

public class UserDto implements Serializable {
    @SuppressWarnings("unused")
    public UserDto() {
    }

    private Long id;
    private String nikName;

    private String firstName;

    private String lastName;

    private String patronymic;
    private Boolean passwordChange;
    private Boolean telegramAdd;
    private Long projectId;
    private List<ProjectDto> projects;

    public UserDto(Long id, String nikName, String firstName, String lastName, String patronymic, Boolean passwordChange, Boolean telegramAdd, Long projectId, List<ProjectDto> projects) {
        this.id = id;
        this.nikName = nikName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.passwordChange = passwordChange;
        this.telegramAdd = telegramAdd;
        this.projectId = projectId;
        this.projects = projects;
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

    @SuppressWarnings("unused")
    public Long getProjectId() {
        return projectId;
    }

    @SuppressWarnings("unused")
    public List<ProjectDto> getProjects() {
        return projects;
    }
}
