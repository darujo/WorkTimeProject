package ru.darujo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "work_type")
public class WorkType extends CopyWork implements Cloneable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "type")
    private String type;
    @Column(name = "time")
    private Float time;
    @Column(name = "work_id")
    private Long workId;
    @Column(name = "number")
    private Integer number;
    @Column(name = "project_id")
    private Long projectId;


    public WorkType clone() throws CloneNotSupportedException {
        return (WorkType) super.clone();
    }
}
