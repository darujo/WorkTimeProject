package ru.darujo.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.darujo.dto.calendar.DayDto;
import ru.darujo.dto.calendar.WeekDto;
import ru.darujo.dto.calendar.WeekWorkDto;
import ru.darujo.exceptions.ResourceNotFoundException;
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
        List<WeekDto> weekDTOs = new ArrayList<>();
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

        for (int i = dayOfWeek.getValue() - 1; i < 7;i++){
            DayDto dayDto;
            DateInfo dateInfo;

            dateInfo = productionCalendar.getDateInfo(date);
            dayDto = new DayDto(
                    date.getDayOfMonth(),
                    dateInfo.getType() == DayType.HOLIDAY,
                    dateInfo.getType() == DayType.SHORTDAY,
                    dateInfo.getTitle());
            days[i] =dayDto;
            date =date.plusDays(1);
        }
        WeekDto weekDto = new WeekDto(
                days[0],
                days[1],
                days[2],
                days[3],
                days[4],
                days[5],
                days[6],month);
        weekDTOs.add(weekDto);
        while (date.compareTo(dayEnd) <= 0 ){
            for (int i = 0;i < 7 ;i++){
                DayDto dayDto;
                DateInfo dateInfo;

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
                date =date.plusDays(1);
            }
            weekDto = new WeekDto(
                    days[0],
                    days[1],
                    days[2],
                    days[3],
                    days[4],
                    days[5],
                    days[6],month);
            weekDTOs.add(weekDto);
        }

        return weekDTOs;
    }
    private LocalDate weekStart(LocalDate date){
        return date.minusDays(date.getDayOfWeek().getValue() - 1);
    }
    private LocalDate weekEnd(LocalDate date){
        return date.plusDays(7 -date.getDayOfWeek().getValue() );
    }

    public List<WeekWorkDto> getPeriodTime(Date dateStart, Date dateEnd,String period) {
        List<WeekWorkDto> weekWorkDTOs = new ArrayList<>();
        LocalDate dayStart;
        LocalDate dayEnd;
        int periodDay = 7;
        if (period == null || period.equals("week") || period.equals("week_day")) {
            dayStart = weekStart(dateStart.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            dayEnd= weekEnd(dateEnd.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        } else if(period.equals("day")){
            dayStart = dateStart.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            dayEnd   = dateEnd.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        } else {
            throw new ResourceNotFoundException("Не верный период " + period);
        }
        if (period!= null && (period.equals("day") || period.equals("week_day"))){
            periodDay = 1;
        }


        LocalDate date = dayStart.minusDays(1);

        while (date.compareTo(dayEnd) < 0 ){
            Timestamp periodStart =null;
            Timestamp periodEnd= null;
            float time = 0f;
            for (int i = 0;i < periodDay ;i++) {

                date = date.plusDays(1);
                if (i ==0) {
                    periodStart = Timestamp.valueOf(date.atStartOfDay());
                }
                if (i == periodDay - 1 ) {
                    periodEnd = Timestamp.valueOf(date.atStartOfDay());
                }
                DateInfo dateInfo = productionCalendar.getDateInfo(date);
                if(dateInfo.getType() == DayType.SHORTDAY){
                    time = time + getDayTime(date) - 1;
                } else if(dateInfo.getType() == DayType.WORKDAY){
                    time = time + getDayTime(date);
                }

            }
            WeekWorkDto weekWorkDto= new WeekWorkDto(
                    periodStart,periodEnd,time);
            weekWorkDTOs.add(weekWorkDto);
        }

        return weekWorkDTOs;
    }

    public Float getWorkTime(Date dateStart, Date dateEnd) {
        LocalDate dayStart = dateStart.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate dayEnd = dateEnd.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        float time= 0f;
        for (LocalDate date =dayStart;date.compareTo(dayEnd) <= 0;date = date.plusDays(1)){
            DateInfo dateInfo = productionCalendar.getDateInfo(date);
            if(dateInfo.getType() == DayType.SHORTDAY){
                time = time + getDayTime(date) - 1;
            } else if(dateInfo.getType() == DayType.WORKDAY){
                time = time + getDayTime(date);
            }
        }
        return time;
    }
    public Float getDayTime(LocalDate date){
        if(date.getDayOfWeek().equals(DayOfWeek.FRIDAY)){
            return 7f;
        }
        return 8.25f;
    }
}
