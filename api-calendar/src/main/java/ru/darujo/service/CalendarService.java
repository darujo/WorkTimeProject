package ru.darujo.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.darujo.dto.calendar.DayDto;
import ru.darujo.dto.calendar.WeekDto;
import ru.darujo.dto.calendar.WeekWorkDto;
import ru.darujo.utils.calendar.ProductionCalendar;
import ru.darujo.utils.calendar.days.RU_Days;
import ru.darujo.utils.calendar.structure.DateInfo;
import ru.darujo.utils.calendar.structure.DayType;

import java.sql.Timestamp;
import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
@Primary
public class CalendarService {
    ProductionCalendar productionCalendar = new ProductionCalendar(new RU_Days());

    public List<WeekDto> getWeekList(Integer month, Integer year) {
        List<WeekDto> weekDtos = new ArrayList<>();
        LocalDate dayStart;
        int day = 31;
        LocalDate dayEnd = LocalDate.now();

        if (month != null){
            dayStart = LocalDate.of(year,month,1);
            boolean ok = false;
            while (!ok) {
                try {
                    dayEnd = LocalDate.of(year, month, day);
                    ok = true;
                } catch (DateTimeException ex) {
                   day--;
                }
            }
        }
        else {
            dayStart = LocalDate.of(year,1,1);
            dayEnd   = LocalDate.of(year,12,31);
        }
        DayOfWeek dayOfWeek = dayStart.getDayOfWeek();
        LocalDate date = dayStart;
        DayDto [] days = new DayDto[7];
        DateInfo dateInfo = productionCalendar.getDateInfo(date);
        DayDto dayDto = new DayDto(
                date.getDayOfMonth(),
                dateInfo.getType() == DayType.HOLIDAY,
                dateInfo.getType() == DayType.SHORTDAY,
                dateInfo.getTitle());
        days[dayOfWeek.getValue() - 1] = dayDto;
        for (int i = dayOfWeek.getValue(); i < 7;i++){
            date =date.plusDays(1);
            dateInfo = productionCalendar.getDateInfo(date);
            dayDto = new DayDto(
                    date.getDayOfMonth(),
                    dateInfo.getType() == DayType.HOLIDAY,
                    dateInfo.getType() == DayType.SHORTDAY,
                    dateInfo.getTitle());
            days[i] =dayDto;
        }
        WeekDto weekDto = new WeekDto(
                days[0],
                days[1],
                days[2],
                days[3],
                days[4],
                days[5],
                days[6],month);
        weekDtos.add(weekDto);
        while (date.compareTo(dayEnd) <= 0 ){
            for (int i = 0;i < 7 ;i++){
                date =date.plusDays(1);

                dateInfo = productionCalendar.getDateInfo(date);
                if (date.compareTo(dayEnd) > 0)
                {
                    dayDto = null;
                }
                else {
                    dayDto = new DayDto(
                            date.getDayOfMonth(),
                            dateInfo.getType() == DayType.HOLIDAY,
                            dateInfo.getType() == DayType.SHORTDAY,
                            dateInfo.getTitle());
                }
                days[i] =dayDto;
            }
            weekDto = new WeekDto(
                    days[0],
                    days[1],
                    days[2],
                    days[3],
                    days[4],
                    days[5],
                    days[6],month);
            weekDtos.add(weekDto);
        }

        return weekDtos;
    }
    private LocalDate weekStart(LocalDate date){
        return date.minusDays(date.getDayOfWeek().getValue() - 1);
    }
    private LocalDate weekEnd(LocalDate date){
        return date.plusDays(7 -date.getDayOfWeek().getValue() );
    }

    public List<WeekWorkDto> getWeekTime(Date dateStart, Date dateEnd) {
        List<WeekWorkDto> weekWorkDtos = new ArrayList<>();
        LocalDate dayStart = weekStart(dateStart.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        LocalDate dayEnd = weekEnd(dateEnd.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());


        LocalDate date = dayStart.minusDays(1);

        while (date.compareTo(dayEnd) < 0 ){
            Timestamp weekStart =null;
            Timestamp weekEnd= null;
            float time = 0f;
            for (int i = 0;i < 7 ;i++) {

                date = date.plusDays(1);
                if (i ==0) {
                    weekStart = Timestamp.valueOf(date.atStartOfDay());
                }else if (i ==6) {
                    weekEnd = Timestamp.valueOf(date.atStartOfDay());
                }
                DateInfo dateInfo = productionCalendar.getDateInfo(date);
                if(dateInfo.getType() == DayType.SHORTDAY){
                    time = time + 7;
                } else if(dateInfo.getType() == DayType.WORKDAY){
                    time = time + 8;
                }

            }
            WeekWorkDto weekWorkDto= new WeekWorkDto(
                    weekStart,weekEnd,time);
            weekWorkDtos.add(weekWorkDto);
        }

        return weekWorkDtos;
    }
}
