package ru.darujo.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nikName", unique = true)
    private String nikName;

    @Column(name = "userpasword")
    private String userpasword;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
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
}

