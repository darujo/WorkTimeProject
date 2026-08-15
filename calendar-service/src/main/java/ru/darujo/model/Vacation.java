package ru.darujo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "vacation")
public class  Vacation {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nikName", nullable = false)
    private String nikName;
    @Column(name = "dateStart", nullable = false)
    private LocalDate dateStart;
    @Column(name = "dateEnd")
    private LocalDate dateEnd;
    @Column(name = "type", nullable = false)
    private String type;
    @Column(name = "dynamic", nullable = false)
    private Boolean dynamic;


}
