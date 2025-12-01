package ru.darujo.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.darujo.dto.calendar.DayDto;
import ru.darujo.dto.calendar.DayTypeDto;
import ru.darujo.dto.calendar.WeekDto;
import ru.darujo.dto.calendar.WeekWorkDto;
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.utils.calendar.ProductionCalendar;
import ru.darujo.utils.calendar.structure.DateInfo;
import ru.darujo.utils.calendar.structure.DayType;

import java.sql.Timestamp;
import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
@Primary
public class CalendarService {
    ProductionCalendar productionCalendar = new ProductionCalendar();

    public List<WeekDto> getWeekList(Integer month, Integer year) {
        if (month != null && (month < 1 || month > 12)) {
            throw new ResourceNotFoundRunTime("Месяц должен быть от 1 до 12");
        }
        if (year == null) {
            throw new ResourceNotFoundRunTime("Не задан год (year)");
        }

        List<WeekDto> weekDTOs = new ArrayList<>();
        LocalDate dayStart;
        int day = 31;
        LocalDate dayEnd = LocalDate.now();

        if (month != null) {
            dayStart = LocalDate.of(year, month, 1);
            boolean ok = false;
            while (!ok) {
                try {
                    dayEnd = LocalDate.of(year, month, day);
                    ok = true;
                } catch (DateTimeException ex) {
                    day--;
                }
            }
        } else {
            dayStart = LocalDate.of(year, 1, 1);
            dayEnd = LocalDate.of(year, 12, 31);
        }
        DayOfWeek dayOfWeek = dayStart.getDayOfWeek();
        LocalDate date = dayStart;
        DayDto[] days = new DayDto[7];

        for (int i = dayOfWeek.getValue() - 1; i < 7; i++) {
            DayDto dayDto;
            DateInfo dateInfo;

            dateInfo = productionCalendar.getDateInfo(date);
            dayDto = new DayDto(
                    date.getDayOfMonth(),
                    dateInfo.getType() == DayType.HOLIDAY || dateInfo.getType() == DayType.WEEK_END,
                    dateInfo.getType() == DayType.SHORTDAY,
                    dateInfo.getTitle());
            days[i] = dayDto;
            date = date.plusDays(1);
        }
        WeekDto weekDto = new WeekDto(
                days[0],
                days[1],
                days[2],
                days[3],
                days[4],
                days[5],
                days[6], month);
        weekDTOs.add(weekDto);
        while (!date.isAfter(dayEnd)) {
            for (int i = 0; i < 7; i++) {
                DayDto dayDto;
                DateInfo dateInfo;

                dateInfo = productionCalendar.getDateInfo(date);
                if (date.isAfter(dayEnd)) {
                    dayDto = null;
                } else {
                    dayDto = new DayDto(
                            date.getDayOfMonth(),
                            dateInfo.getType() == DayType.HOLIDAY  || dateInfo.getType() == DayType.WEEK_END,
                            dateInfo.getType() == DayType.SHORTDAY,
                            dateInfo.getTitle());
                }
                days[i] = dayDto;
                date = date.plusDays(1);
            }
            weekDto = new WeekDto(
                    days[0],
                    days[1],
                    days[2],
                    days[3],
                    days[4],
                    days[5],
                    days[6], month);
            weekDTOs.add(weekDto);
        }

        return weekDTOs;
    }

    private LocalDate weekStart(LocalDate date) {
        return date.minusDays(date.getDayOfWeek().getValue() - 1);
    }

    private LocalDate weekEnd(LocalDate date) {
        return date.plusDays(7 - date.getDayOfWeek().getValue());
    }

    private LocalDate monthStart(LocalDate date) {
        return date.minusDays(date.getDayOfMonth() - 1);
    }

    private LocalDate monthEnd(LocalDate date) {
        return monthStart(date).plusMonths(1).minusDays(1);
    }

    private LocalDate yearStart(LocalDate date) {
        return date.minusDays(date.getDayOfYear()-1);
    }

    private LocalDate yearEnd(LocalDate date) {
        return yearStart(date).plusYears(1).minusDays(1);
    }

    public List<WeekWorkDto> getPeriodTime(Date dateStart, Date dateEnd, String periodSplit) {
        String period = null;
        String split = null;

        if(periodSplit != null) {
            switch (periodSplit) {
                case "1" -> periodSplit = "day";
                case "2" -> periodSplit = "week_day";
                case "3" -> periodSplit = "week";
                case "4" -> periodSplit = "month";
                case "5" -> periodSplit = "month_day";
                case "6" -> periodSplit = "month_week";
                case "7" -> periodSplit = "month3";
                case "8" -> periodSplit = "month3_day";
                case "9" -> periodSplit = "month3_week";
                case "10" -> periodSplit = "year";
                case "11" -> periodSplit = "year_day";
                case "12" -> periodSplit = "year_week";
            }
            switch (periodSplit) {
                case "week_day" -> {
                    period = "week";
                    split = "day";
                }
                case "year_day" -> {
                    period = "year";
                    split = "day";
                }
                case "year_week" -> {
                    period = "year";
                    split = "week";
                }
                case "month_day" -> {
                    period = "month";
                    split = "day";
                }
                case "month_week" -> {
                    period = "month";
                    split = "week";
                }
                case "month3_day" -> {
                    period = "month3";
                    split = "day";
                }
                case "month3_week" -> {
                    period = "month3";
                    split = "week";
                }
                default -> period = periodSplit;
            }
        }
        return getPeriodTime(dateStart, dateEnd, period, split);
    }

    public List<WeekWorkDto> getPeriodTime(Date dateStart, Date dateEnd, String period, String split) {
        List<WeekWorkDto> weekWorkDTOs = new ArrayList<>();
        LocalDate dayStart;
        LocalDate dayEnd;
        int periodDay;
        if (period == null){
            period = "week";
        }

        switch (period) {
            case "week" -> {
                dayStart = weekStart(dateStart.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                dayEnd = weekEnd(dateEnd.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            }
            case "day" -> {
                dayStart = dateStart.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                dayEnd = dateEnd.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            }
            case "month" -> {
                dayStart = monthStart(dateStart.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                dayEnd = monthEnd(dateEnd.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            }
            case "month3" -> {
                dayStart = monthStart(dateStart.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                dayEnd = monthEnd(dateEnd.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()).plusMonths(2);
            }
            case "year" -> {
                dayStart = yearStart(dateStart.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                dayEnd = yearEnd(dateEnd.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            }
            default -> throw new ResourceNotFoundRunTime("Не верный период " + period);
        }
        if (split == null) {
            periodDay = switch (period) {
                case "day" -> 1;
                case "month" -> dayEnd.getDayOfMonth();
                case "month3" -> 92;
                case "year" -> dayEnd.getDayOfYear();
                default -> 7;
            };
        } else if (split.equals("day")) {
            periodDay = 1;
        } else if (split.equals("week")) {
            dayStart = weekStart(dayStart);
            dayEnd = weekEnd(dayEnd);
            periodDay = 7;
        } else {
            periodDay = 7;
        }


        LocalDate date = dayStart.minusDays(1);

        while (date.isBefore(dayEnd)) {
            Timestamp periodStart = null;
            Timestamp periodEnd = null;
            float time = 0f;
            HashMap<DayTypeDto,Integer> dayTypes = new HashMap<>();
            for (int i = 0; i < periodDay; i++) {

                date = date.plusDays(1);
                if (i == 0) {
                    periodStart = Timestamp.valueOf(date.atStartOfDay());
                }
                if (i == periodDay - 1) {
                    periodEnd = Timestamp.valueOf(date.atStartOfDay());
                }
                DateInfo dateInfo = productionCalendar.getDateInfo(date);
                if(dateInfo.getType().equals(DayType.WEEK_END) ) {
                    addDayType(dayTypes, DayTypeDto.WEEK_END);
                } else if(dateInfo.getType().equals(DayType.WORKDAY) ) {
                    addDayType(dayTypes, DayTypeDto.WORKDAY);
                } else if(dateInfo.getType().equals(DayType.HOLIDAY) ) {
                    addDayType(dayTypes, DayTypeDto.HOLIDAY);
                } else if(dateInfo.getType().equals(DayType.SHORTDAY) ) {
                    addDayType(dayTypes, DayTypeDto.SHORTDAY);
                }
                time = time + getTimeDay(dateInfo);
            }
            WeekWorkDto weekWorkDto = new WeekWorkDto(
                    periodStart, periodEnd, time, dayTypes);
            weekWorkDTOs.add(weekWorkDto);
        }

        return weekWorkDTOs;
    }
    private void addDayType (HashMap<DayTypeDto,Integer> dayTypes, DayTypeDto dayType){
        Integer day = dayTypes.get(dayType);
        if(day == null){
            dayTypes.put(dayType,1);
        } else {
            dayTypes.put(dayType,++day);
        }
    }

    public float getTimeDay(LocalDate date) {
        DateInfo dateInfo = productionCalendar.getDateInfo(date);
        return getTimeDay(dateInfo);
    }
    public float getTimeDay(DateInfo dateInfo) {
        if (dateInfo.getType() == DayType.SHORTDAY) {
            return getDayTime(dateInfo.getDate()) - 1;
        } else if (dateInfo.getType() == DayType.WORKDAY) {
            return getDayTime(dateInfo.getDate());
        }
        return 0f;
    }


    public Float getWorkTime(Date dateStart, Date dateEnd) {
        LocalDate dayStart = dateStart.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate dayEnd = dateEnd.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        float time = 0f;
        for (LocalDate date = dayStart; !date.isAfter(dayEnd); date = date.plusDays(1)) {
            DateInfo dateInfo = productionCalendar.getDateInfo(date);
            if (dateInfo.getType() == DayType.SHORTDAY) {
                time = time + getDayTime(date) - 1;
            } else if (dateInfo.getType() == DayType.WORKDAY) {
                time = time + getDayTime(date);
            }
        }
        return time;
    }

    public Float getDayTime(LocalDate date) {
        if (date.getDayOfWeek().equals(DayOfWeek.FRIDAY)) {
            return 7f;
        }
        return 8.25f;
    }

    public int getDayNotHoliday(Date dateStart, Date dateEnd) {
        return getDayNotHoliday(dateStart.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                dateEnd.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    }
    public int getDayNotHoliday(LocalDate dateStart, LocalDate dateEnd) {
        return productionCalendar.iDaysNotHoliday(dateStart, dateEnd);
    }

    public Timestamp getDateEndNotHoliday(Timestamp dateStart, Integer days) {
        return Timestamp.valueOf(getDateEndNotHoliday(dateStart.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),days).atStartOfDay());
    }

    private LocalDate getDateEndNotHoliday(LocalDate dateStart, Integer days) {
        return productionCalendar.getDateEndNotHoliday(dateStart, days);
    }

    public boolean isHoliday(Timestamp date) {
        return  isHoliday(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    }

    public boolean isHoliday(LocalDate date) {
        return productionCalendar.isHoliday(date);
    }
    public boolean isWorkDay(Timestamp date) {
        return  isWorkDay(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    }

    public boolean isWorkDay(LocalDate date) {
        return productionCalendar.isWorkDay(date);
    }

    public boolean existWorkDay(Timestamp dateStart, Timestamp dateEnd) {
        return existWorkDay(dateStart.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                dateEnd.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    }

    private boolean existWorkDay(LocalDate dateStart, LocalDate dateEnd) {
        return productionCalendar.existWorkDay(dateStart, dateEnd);
    }
}
