package ru.darujo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "work")
public class WorkLittle implements WorkLittleInterface {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Код SAP
    @Column(name = "code_sap")
    private Long codeSap;
    // Код Зи
    @Column(name = "code_zi")
    private String codeZi;
    // Наименование Зи
    @Column(name = "name")
    private String name;
    @Column(name = "project_list")
    private List<Long> projectList;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "release_id")
    private Release release;

    @OneToMany(mappedBy = "workParent", fetch = FetchType.LAZY)
    private List<WorkLittle> childWork;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private WorkLittle workParent;


}
