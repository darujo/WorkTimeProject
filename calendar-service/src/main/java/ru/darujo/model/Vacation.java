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
    @Column(name = "nikName")
    private String nikName;
    @Column(name = "dateStart")
    private LocalDate dateStart;
    @Column(name = "dateEnd")
    private LocalDate dateEnd;

}
