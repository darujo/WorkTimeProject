package ru.darujo.converter;

import ru.darujo.assistant.helper.DateHelper;
import ru.darujo.dto.calendar.VacationDto;
import ru.darujo.model.Vacation;
import ru.darujo.type.VacationType;

public class VacationConvertor {
    public static VacationDto getVacationDto(Vacation vacation) {
        return new VacationDto(
                vacation.getId(),
                vacation.getNikName(),
                DateHelper.getZDT(vacation.getDateStart()),
                DateHelper.getZDT(vacation.getDateEnd()),
                vacation.getType() == null ? VacationType.VACATION : VacationType.valueOf(vacation.getType()),
                vacation.getDynamic());
    }

    public static Vacation getVacation(VacationDto vacationDto) {
        return new Vacation(
                vacationDto.getId(),
                vacationDto.getNikName(),
                DateHelper.zDTToLD(vacationDto.getDateStart()),
                DateHelper.zDTToLD(vacationDto.getDateEnd()),
                vacationDto.getType() == null ? VacationType.VACATION.toString() : vacationDto.getType().toString(),
                vacationDto.getDynamic() != null ? vacationDto.getDynamic() : false);
    }

}
