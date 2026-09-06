package ru.darujo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "code")
public class Code {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id")
    private Code parent;
    @Column(name = "code")
    private String code;
    @Column(name = "name")
    private String name;
    @Column(name = "sort")
    private Integer sort;
    @OneToMany
    @JoinColumn(name = "code_id")
    private List<CodeAttr> codeAttrList;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Code code1 = (Code) o;
        return Objects.equals(parent, code1.parent) && Objects.equals(code, code1.code) && Objects.equals(name, code1.name) && Objects.equals(sort, code1.sort);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parent, code, name, sort);
    }
}
