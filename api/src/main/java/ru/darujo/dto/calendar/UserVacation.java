package ru.darujo.dto.calendar;

import ru.darujo.dto.user.UserFio;

import java.util.List;

public class UserVacation implements UserFio {
    final private String nikName;
    private String firstName;

    private String lastName;

    private String patronymic;

    @Override
    public String getNikName() {
        return nikName;
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
    List<WeekWorkDto> weekWorkDtos;

    public UserVacation(String nikName, List<WeekWorkDto> weekWorkDtos) {
        this.nikName = nikName;
        this.weekWorkDtos = weekWorkDtos;
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

    @SuppressWarnings("unused")
    public List<WeekWorkDto> getWeekWorkDtos() {
        return weekWorkDtos;
    }
}
