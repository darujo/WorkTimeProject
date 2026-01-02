package ru.darujo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.darujo.assistant.helper.DateHelper;
import ru.darujo.dto.calendar.WeekDto;
import ru.darujo.dto.calendar.WeekWorkDto;
import ru.darujo.service.CalendarService;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

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
        return calendarService.getWeekList(month, year);
    }

    @GetMapping("/period/time")
    public List<WeekWorkDto> PeriodTimeList(@RequestParam(name = "dateStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateStartStr,
                                            @RequestParam(name = "dateEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateEndStr,
                                            @RequestParam(required = false) String period
    ) {
        Date dateStart = DateHelper.DTZToDate(dateStartStr, "dateStart = ");
        Date dateEnd = DateHelper.DTZToDate(dateEndStr, "dateEnd = ");


        return calendarService.getPeriodTime(dateStart, dateEnd, period);
    }

    @GetMapping("/work/time")
    public Float WorkTime(@RequestParam(name = "dateStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateStartStr,
                          @RequestParam(name = "dateEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateEndStr
    ) {
        Date dateStart = DateHelper.DTZToDate(dateStartStr, "dateStart = ");
        Date dateEnd = DateHelper.DTZToDate(dateEndStr, "dateEnd = ");


        return calendarService.getWorkTime(dateStart, dateEnd);
    }
}