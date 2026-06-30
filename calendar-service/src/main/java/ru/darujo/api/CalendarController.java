package ru.darujo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.darujo.assistant.helper.DateHelper;
import ru.darujo.assistant.helper.EnumHelper;
import ru.darujo.converter.DayInfoConverter;
import ru.darujo.dto.calendar.DayTypeDto;
import ru.darujo.dto.calendar.WeekDto;
import ru.darujo.dto.calendar.WeekWorkDto;
import ru.darujo.dto.ratestage.AttrDto;
import ru.darujo.service.CalendarService;
import ru.darujo.service.DayInfoService;
import ru.darujo.utils.calendar.structure.DateInfo;

import java.time.ZonedDateTime;
import java.util.List;

@RestController()
@RequestMapping("/v1/calendar")
public class CalendarController {
    private CalendarService calendarService;
    private DayInfoService dayInfoService;

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
    public List<WeekWorkDto> PeriodTimeList(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateStart,
                                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateEnd,
                                            @RequestParam(required = false) String period
    ) {
        return calendarService.getPeriodTime(DateHelper.zDTToLD(dateStart), DateHelper.zDTToLD(dateEnd), period);
    }

    @GetMapping("/work/time")
    public Float WorkTime(@RequestParam
                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                              ZonedDateTime dateStart,
                          @RequestParam
                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                              ZonedDateTime dateEnd
    ) {
        return calendarService.getWorkTime(DateHelper.zDTToLD(dateStart), DateHelper.zDTToLD(dateEnd));
    }

    @GetMapping("/day/type")
    public List<AttrDto<Enum<?>>> getDayTypeList() {
        return EnumHelper.getList(DayTypeDto.getTypeDay());
    }

    @GetMapping("/day")
    public DateInfo getDayInfo(@RequestParam(required = false)
                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                   ZonedDateTime date) {

        return calendarService.getDateInfo(DateHelper.zDTToLD(date));
    }

    @PostMapping("/day")
    public void saveDayInfo(@RequestBody DateInfo dayInfoDto) {
        dayInfoService.addNew(DayInfoConverter.getDayInfo(dayInfoDto));
    }

    @Autowired
    public void setDayInfoService(DayInfoService dayInfoService) {
        this.dayInfoService = dayInfoService;
    }
}