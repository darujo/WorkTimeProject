package ru.darujo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.darujo.dto.calendar.DayTypeDto;
import ru.darujo.dto.calendar.UserVacation;
import ru.darujo.dto.calendar.UserVacationsDto;
import ru.darujo.dto.calendar.WeekWorkDto;
import ru.darujo.dto.user.UserDto;
import ru.darujo.integration.UserServiceIntegration;
import ru.darujo.model.Vacation;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
@Primary
public class VacationReportService {

    VacationService vacationService;

    @Autowired
    public void setVacationService(VacationService vacationService) {
        this.vacationService = vacationService;
    }

    CalendarService calendarService;

    @Autowired
    public void setCalendarService(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    UserServiceIntegration userServiceIntegration;

    @Autowired
    public void setUserServiceIntegration(UserServiceIntegration userServiceIntegration) {
        this.userServiceIntegration = userServiceIntegration;
    }

    public UserVacationsDto getUserVacations(String nikName, Timestamp dateStart, Timestamp dateEnd, String periodSplit) {
        if (periodSplit == null) {
            periodSplit = "day";
        }
        List<WeekWorkDto> weekWorkDTOs = calendarService.getPeriodTime(dateStart, dateEnd, periodSplit);
        return new UserVacationsDto(weekWorkDTOs, getUserVacations(nikName, weekWorkDTOs));
    }

    private List<UserVacation> getUserVacations(String nikName, List<WeekWorkDto> weekWorkDTOs) {
        List<UserVacation> userVacations = new ArrayList<>();
        List<UserDto> userDTOs = userServiceIntegration.getUserDTOs(nikName);
        userDTOs.forEach(userDto -> {
            List<WeekWorkDto> weekWorkUserList = new ArrayList<>();
            for (WeekWorkDto weekWorkDto : weekWorkDTOs) {
                weekWorkUserList.add((WeekWorkDto) weekWorkDto.clone());
            }
            UserVacation userVacation = new UserVacation(userDto.getNikName(), weekWorkUserList);
            userVacation.setFirstName(userDto.getFirstName());
            userVacation.setLastName(userDto.getLastName());
            userVacation.setPatronymic(userDto.getPatronymic());
            userVacations.add(userVacation);
            Vacation vacation = null;
            LocalDate dayEndVacation = null;
            for (WeekWorkDto weekWorkDto : weekWorkUserList) {
                LocalDate day = weekWorkDto.getDayStart().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().minusDays(1);
                LocalDate dayEnd = weekWorkDto.getDayEnd().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                float timeAll;
                boolean flagDay = weekWorkDto.getDayStart().equals(weekWorkDto.getDayEnd());
                timeAll = weekWorkDto.getTime();
                weekWorkDto.deleteDayType(DayTypeDto.HOLIDAY);
                while (day.compareTo(dayEnd) < 0) {
                    day = day.plusDays(1);
                    if (vacation == null || dayEndVacation == null || day.compareTo(dayEndVacation) > 0) {
                        vacation = vacationService.findOneDateInVacation(userDto.getNikName(), day);
                        if (vacation != null) {
                            dayEndVacation = vacation.getDateEnd().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        }
                    }
                    if (vacation != null) {
                        float time = calendarService.getTimeDay(day);
                        if (!calendarService.isHoliday(day)) {
                            weekWorkDto.addDayType(DayTypeDto.VACATION);
                        } else {
                            weekWorkDto.addDayType(DayTypeDto.HOLIDAY);
                        }
                        if (time > 0f) {
                            timeAll = timeAll - time + (flagDay ? -1f : 0f);
                        }
                    }
                }
                weekWorkDto.setTime(timeAll);
            }
        });
        return userVacations;
    }

    public Timestamp getLastWorkDay(String username,
                                    Timestamp dateStart,
                                    Integer dayMinus,
                                    Boolean lastWeek) {
        LocalDate localDate = dateStart.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (lastWeek) {
            localDate = localDate.minusDays(localDate.getDayOfWeek().getValue() - 1);
        }
        int day = 0;
        while (day < dayMinus) {
            localDate = localDate.minusDays(1);
            if (calendarService.isWorkDay(localDate) &&
                    vacationService.findOneDateInVacation(username, localDate) == null) {
                day++;
            }

        }
        return Timestamp.valueOf(localDate.atStartOfDay());

    }

    public Boolean isDayAfterWeek(Timestamp date, Integer dayMinus) {
        if (dayMinus < 1) {
            return false;
        }
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        while (0 < dayMinus) {
            if (!calendarService.isWorkDay(localDate)) {
                return false;
            }
            localDate = localDate.minusDays(1);
            dayMinus--;
        }
        return !calendarService.isWorkDay(localDate);
    }
}
