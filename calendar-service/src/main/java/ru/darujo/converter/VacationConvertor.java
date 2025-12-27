package ru.darujo.converter;

import ru.darujo.dto.calendar.VacationDto;
import ru.darujo.model.Vacation;

public class VacationConvertor {
    public static VacationDto getVacationDto(Vacation vacation) {
        return new VacationDto(vacation.getId(),vacation.getNikName(),vacation.getDateStart(),vacation.getDateEnd());
    }

    public static Vacation getVacation(VacationDto vacationDto) {
        return new Vacation(vacationDto.getId(),vacationDto.getNikName(),vacationDto.getDateStart(),vacationDto.getDateEnd());
    }

}
