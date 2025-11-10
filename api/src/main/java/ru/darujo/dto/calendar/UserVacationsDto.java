package ru.darujo.dto.calendar;

import java.io.Serializable;
import java.util.List;

public class UserVacationsDto implements Serializable {
    List<WeekWorkDto> weekWorkDtos;
    List<UserVacation> userVacations;

    public UserVacationsDto(List<WeekWorkDto> weekWorkDtos, List<UserVacation> userVacations) {
        this.weekWorkDtos = weekWorkDtos;
        this.userVacations = userVacations;
    }

    @SuppressWarnings("unused")
    public List<WeekWorkDto> getWeekWorkDtos() {
        return weekWorkDtos;
    }

    @SuppressWarnings("unused")
    public List<UserVacation> getUserVacations() {
        return userVacations;
    }
}
