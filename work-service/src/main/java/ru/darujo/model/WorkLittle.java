package ru.darujo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "work")
public class WorkLittle {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Код SAP
    @Column(name = "code_sap")
    private Long codeSap;
    // Код Зи
    @Column(name = "codeZI")
    private String codeZI;
    // Наименование Зи
    @Column(name = "name")
    private String name;
    // Разработка прототипа Факт
    // ВЕНДЕРКА Факт
    @Column(name = "stageZI")
    private Integer stageZI;
    @Column(name = "task")
    private String task;
    @ManyToOne
    @JoinColumn(name="release_id")
    private Release release;

}
