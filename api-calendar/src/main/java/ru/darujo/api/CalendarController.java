package ru.darujo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.darujo.dto.calendar.WeekDto;
import ru.darujo.dto.calendar.WeekWorkDto;
import ru.darujo.exceptions.ResourceNotFoundException;
import ru.darujo.service.CalendarService;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.*;

@RestController()
@RequestMapping("/v1/calendar")
public class CalendarController {
    private CalendarService calendarService;
    @Autowired
    public void setCalendarService(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping("")
    public List<WeekDto> WeekList(@RequestParam(required = false) Integer month,
                                  @RequestParam(required = false) Integer year
                                      ) {
        if( month!= null && (month < 1 || month > 12) ){
            throw new ResourceNotFoundException("Месяц должен быть от 1 до 12");
        }
        if (year == null){
            throw new ResourceNotFoundException("Не задан год (year)");
        }

        return calendarService.getWeekList(month,year);
    }

    @GetMapping("/weektime")
    public List<WeekWorkDto> WeekTimeList(@RequestParam(name = "dateStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateStartStr,
                                          @RequestParam(name = "dateEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateEndStr
    ) {
        Date dateStart = stringToDate(dateStartStr, "dateStart = ");
        Date dateEnd = stringToDate(dateEndStr, "dateEnd = ");


        return calendarService.getWeekTime(dateStart,dateEnd);
    }
    @GetMapping("/worktime")
    public Float WorkTime(@RequestParam(name = "dateStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateStartStr,
                          @RequestParam(name = "dateEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateEndStr
    ) {
        Date dateStart = stringToDate(dateStartStr, "dateStart = ");
        Date dateEnd = stringToDate(dateEndStr, "dateEnd = ");


        return calendarService.getWorkTime(dateStart,dateEnd);
    }

    private Timestamp stringToDate(ZonedDateTime dateStr, String text) {
        return stringToDate(dateStr,text,false);
    }
    private Timestamp stringToDate(ZonedDateTime dateStr, String text, boolean checkNull) {
        if (dateStr != null) {

            Calendar c = Calendar.getInstance();
            c.setTime(Timestamp.from(dateStr.toInstant()));
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            return new Timestamp(c.getTimeInMillis());

        }
        else if(checkNull){
            throw new ResourceNotFoundException("Не не передан обязательный параметр " + text + " null ");
        }
        return null;
    }
}