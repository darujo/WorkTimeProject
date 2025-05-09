package ru.darujo.dto.calendar;

import ru.darujo.dto.printer.DataPrinter;
import ru.darujo.dto.user.UserFio;

import java.sql.Timestamp;

public class VacationDto extends DataPrinter implements UserFio {
    public VacationDto() {
    }

    public VacationDto(Long id, String nikName, Timestamp dateStart, Timestamp dateEnd) {
        this.id = id;
        this.nikName = nikName;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
    }

    private Long id;
    private String nikName;
    private String firstName;

    private String lastName;

    private String patronymic;
    private Timestamp dateStart;
    private Timestamp dateEnd;
    private Integer days;
    public Long getId() {
        return id;
    }

    public String getNikName() {
        return nikName;
    }

    public Timestamp getDateStart() {
        return dateStart;
    }

    public Timestamp getDateEnd() {
        return dateEnd;
    }

    public String getDateStartStr() {
        return dateToText(dateStart);
    }

    public String getDateEndStr() {
        return dateToText(dateEnd);
    }

    public Integer getDays() {
        return days;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setNikName(String nikName) {
        this.nikName = nikName;
    }

    @Override
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public void setDateEnd(Timestamp dateEnd) {
        this.dateEnd = dateEnd;
    }
}
