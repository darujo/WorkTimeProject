package ru.darujo.dto.workrep;

import ru.darujo.dto.user.UserFio;

import java.util.List;

public class UserWorkPeriodDto implements UserFio {

    private String firstName;
    private String lastName;
    private String patronymic;
    private String nikName;
    List<WorkPeriodDto> workPeriodDTOs;

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

    @Override
    public String getNikName() {
        return nikName;
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
    public List<WorkPeriodDto> getWorkPeriodDTOs() {
        return workPeriodDTOs;
    }

    public UserWorkPeriodDto(String nikName, List<WorkPeriodDto> workPeriodDTOs) {
        this.nikName = nikName;
        this.workPeriodDTOs = workPeriodDTOs;
    }

    @SuppressWarnings("unused")
    public UserWorkPeriodDto() {
    }
}
