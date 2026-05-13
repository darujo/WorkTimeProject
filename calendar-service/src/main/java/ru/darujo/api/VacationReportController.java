package ru.darujo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.darujo.assistant.helper.DateHelper;
import ru.darujo.dto.calendar.UserVacationsDto;
import ru.darujo.service.VacationReportService;

import java.time.LocalDate;
import java.time.ZonedDateTime;


@RestController()
@RequestMapping("/v1/vacation/report")
public class VacationReportController {
    VacationReportService vacationReportService;

    @Autowired
    public void setVacationReportService(VacationReportService vacationReportService) {
        this.vacationReportService = vacationReportService;
    }

    @GetMapping("/user")
    public UserVacationsDto getWeekWork(@RequestParam(required = false)
                                        String nikName,
                                        @RequestParam(required = false)
                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                        ZonedDateTime dateStart,
                                        @RequestParam(required = false)
                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                            ZonedDateTime dateEnd,
                                        @RequestParam(required = false)
                                        String periodSplit) {
        LocalDate lDateStart = DateHelper.zDTToLD(dateStart, "dateStart = ");
        LocalDate lDateEnd = DateHelper.zDTToLD(dateEnd, "dateEnd = ");

        return vacationReportService.getUserVacations(nikName, lDateStart, lDateEnd, periodSplit);
    }

    @GetMapping("/user/work/day/last")
    public LocalDate getLastWorkDay(@RequestParam(required = false)
                                    String username,
                                    @RequestParam(required = false)
                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                    ZonedDateTime dateStart,
                                    @RequestParam(required = false)
                                    Integer dayMinus,
                                    @RequestParam(required = false)
                                    Boolean lastWeek) {
        LocalDate lDateStart;
        if (dateStart == null) {
            lDateStart = LocalDate.now();
        } else {
            lDateStart = DateHelper.zDTToLD(dateStart);
        }
        return vacationReportService.getLastWorkDay(username, lDateStart, dayMinus, lastWeek);
    }

    @GetMapping("/user/work/day")
    public Boolean isWorkDayUser(@RequestParam(required = false)
                                 String username,
                                 @RequestParam(required = false)
                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                 ZonedDateTime date
    ) {
        LocalDate lDate;
        if (date == null) {
            lDate = LocalDate.now();
        } else {
            lDate = DateHelper.zDTToLD(date);
        }
        return vacationReportService.isWorkDayUser(lDate, username);
    }

    @GetMapping("/work/day/after/week")
    public Boolean isDayAfterWeek(@RequestParam(required = false)
                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                      ZonedDateTime date,
                                  @RequestParam(required = false)
                                  Integer dayMinus) {
        LocalDate lDate;
        if (date == null) {
            lDate = LocalDate.now();
        } else {
            lDate = DateHelper.zDTToLD(date);
        }
        return vacationReportService.isDayAfterWeek(lDate, dayMinus);
    }

}

