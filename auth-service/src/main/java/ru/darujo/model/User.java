package ru.darujo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

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

    @ManyToMany
    @JoinTable(name = "user_roles",
    joinColumns = @JoinColumn(name ="user_id"),
    inverseJoinColumns = @JoinColumn(name ="role_id"))
    private Collection<Role> roles;

    @ManyToMany
    @JoinTable(name = "user_rights",
            joinColumns = @JoinColumn(name ="user_id"),
            inverseJoinColumns = @JoinColumn(name ="right_id"))
    private Collection<Right> rights;

    public User(Long id, String nikName, String password, String firstName, String lastName, String patronymic, Boolean passwordChange) {
        this.id = id;
        this.nikName = nikName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.passwordChange = passwordChange;
    }


}

