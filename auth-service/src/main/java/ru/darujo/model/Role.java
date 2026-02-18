package ru.darujo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Objects;

@Entity
@Data
@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "label")
    private String label;
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToMany
    @JoinTable(name = "role_rights",
            joinColumns=@JoinColumn(name = "role_id"),
            inverseJoinColumns =@JoinColumn(name = "right_id"))
    private Collection<Right> rights;

    @ManyToMany
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name ="role_id"),
            inverseJoinColumns = @JoinColumn(name ="user_id"))
    private Collection<User> users;

    public Role(Long id, String name, String label, Project project) {
        this.id = id;
        this.name = name;
        this.label = label;
        this.project = project;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(id, role.id) && Objects.equals(name, role.name) && Objects.equals(label, role.label) && Objects.equals(users, role.users);
    }
}
