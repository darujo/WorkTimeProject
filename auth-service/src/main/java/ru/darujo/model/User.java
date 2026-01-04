package ru.darujo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.darujo.exceptions.ResourceNotFoundRunTime;

import java.util.Collection;
import java.util.List;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nik_name", unique = true)
    private String nikName;

    @Column(name = "password")
    private String password;

    @Column(name = "password_change")
    private Boolean passwordChange;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "patronymic")
    private String patronymic;

    @Column(name = "telegram_id")
    private Long telegramId;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project currentProject;

    @ManyToMany
    @JoinTable(name = "user_projects",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id"))
    private List<Project> projects;

    @ManyToMany
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles;

    @ManyToMany
    @JoinTable(name = "user_rights",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "right_id"))
    private Collection<Right> rights;

    public User(Long id, String nikName, String password, String firstName, String lastName, String patronymic, Boolean passwordChange, List<Project> projects) {
        this.id = id;
        this.nikName = nikName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.passwordChange = passwordChange;
        this.projects = projects;
    }

    public Project getCurrentProject() {
        if (currentProject == null) {
            if (!getProjects().isEmpty()) {
                currentProject = getProjects().get(0);
            } else {
                throw new ResourceNotFoundRunTime("У пользователя нет ни одного проекта обратитесь к администратору");
            }
        }
        return currentProject;
    }
}

