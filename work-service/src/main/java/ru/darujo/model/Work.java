package ru.darujo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.darujo.service.WorkService;

import java.util.List;

@NoArgsConstructor
@Data
@Entity
@Table(name = "work")
public class Work implements WorkLittleInterface {
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
    // Краткое описание внутренней задачи
    @Column(name = "description")
    private String description;
    // Порядковый номер релиза
    @Column(name = "project_list")
    private List<Long> projectList;

    public Work(Long id,
                Long codeSap,
                String codeZi,
                String name,
                String description,
                List<Long> projectList) {
        this.id = id;
        this.codeSap = codeSap;
        this.codeZi = codeZi;
        this.name = name;
        this.description = description;
        this.projectList = projectList;
    }

    public List<Long> getProjectList() {
        return projectList;
    }
}
