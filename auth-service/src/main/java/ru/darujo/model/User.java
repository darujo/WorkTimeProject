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
    private long id;

    @Column(name = "username")
    private String username;

    @Column(name = "userpasword")
    private String userpasword;

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

