package ru.darujo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.darujo.dto.ListString;
import ru.darujo.dto.UserDto;
import ru.darujo.dto.WorkTimeDto;
import ru.darujo.dto.calendar.WeekDto;
import ru.darujo.exceptions.ResourceNotFoundException;
import ru.darujo.service.CalendarService;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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


}