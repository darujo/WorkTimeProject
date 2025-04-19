package ru.darujo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    public Role(Long id, String name, String label) {
        this.id = id;
        this.name = name;
        this.label = label;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
