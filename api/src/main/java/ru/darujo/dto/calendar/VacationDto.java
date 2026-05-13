package ru.darujo.dto.calendar;

import ru.darujo.assistant.helper.DateHelper;
import ru.darujo.dto.user.UserFio;

import java.io.Serializable;
import java.time.ZonedDateTime;


public class VacationDto implements UserFio, Serializable {
    @SuppressWarnings("unused")
    public VacationDto() {
    }

    public VacationDto(Long id, String nikName, ZonedDateTime dateStart, ZonedDateTime dateEnd) {
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
    private ZonedDateTime dateStart;
    private ZonedDateTime dateEnd;
    private Integer days;
    public Long getId() {
        return id;
    }

    public String getNikName() {
        return nikName;
    }

    public ZonedDateTime getDateStart() {
        return dateStart;
    }

    public ZonedDateTime getDateEnd() {
        return dateEnd;
    }

    @SuppressWarnings("unused")
    public String getDateStartStr() {
        return DateHelper.dateToDDMMYYYY(dateStart);
    }

    @SuppressWarnings("unused")
    public String getDateEndStr() {
        return DateHelper.dateToDDMMYYYY(dateEnd);
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

    public void setDateEnd(ZonedDateTime dateEnd) {
        this.dateEnd = dateEnd;
    }
}
