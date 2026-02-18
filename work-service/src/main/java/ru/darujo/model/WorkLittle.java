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
    @Column(name = "task")
    private String task;
    @Column(name = "project_list")
    private List<Long> projectList;

}
