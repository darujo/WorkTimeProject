package ru.darujo.dto.calendar;

import ru.darujo.assistant.helper.DataHelper;
import ru.darujo.dto.user.UserFio;

import java.io.Serializable;
import java.sql.Timestamp;

public class VacationDto implements UserFio, Serializable {
    @SuppressWarnings("unused")
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

    @SuppressWarnings("unused")
    public String getDateStartStr() {
        return DataHelper.dateToDDMMYYYY(dateStart);
    }

    @SuppressWarnings("unused")
    public String getDateEndStr() {
        return DataHelper.dateToDDMMYYYY(dateEnd);
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
