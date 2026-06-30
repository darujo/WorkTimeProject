package ru.darujo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.darujo.utils.calendar.structure.DayType;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "day_info")
public class DayInfo {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date", unique = true, nullable = false)
    private LocalDate date;

    /**
     * Enum {@link DayType} - тип даты
     * Выходной\праздничный, сокращенный рабочий день, рабочий день
     */

    @Column(name = "type", nullable = false)
    private String type;

    /**
     * Наименование праздничного дня
     */
    @Column(name = "title")
    private String title;
}
