package ru.darujo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

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
    private Timestamp dateStart;
    @Column(name = "dateEnd")
    private Timestamp dateEnd;

}
