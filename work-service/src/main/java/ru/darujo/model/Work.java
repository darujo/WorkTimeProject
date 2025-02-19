package ru.darujo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
//@JsonIgnoreProperties(value = { "chequeLines" })
@Data
@Entity
@Table(name = "work")
public class Work {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Наименование
    @Column(name = "name")
    private String name;
    // Разработка прототипа
    @Column(name = "startDevelop")
    private Date dateStartDevelop;
    // Стабилизация прототипа
    @Column(name = "startDebug")
    private Date dateStartDebug;
    // Стабилизация релиза
    @Column(name = "startRelease")
    private Date dateStartRelease;
    // ОПЭ релиза
    @Column(name = "startOPE")
    private Date dateStartOPE;
    // ВЕНДЕРКА
    @Column(name = "startWender")
    private Date dateStartWender;
}
