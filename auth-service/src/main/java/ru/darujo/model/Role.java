package ru.darujo.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @ManyToMany
    @JoinTable(name = "role_rights",
            joinColumns=@JoinColumn(name = "role_id"),
            inverseJoinColumns =@JoinColumn(name = "right_id"))
    private Collection<Right> rights;
}
