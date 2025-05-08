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

    public List<WeekWorkDto> getWeekWorkDtos() {
        return weekWorkDtos;
    }

    public List<UserVacation> getUserVacations() {
        return userVacations;
    }
}
