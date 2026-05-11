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
                                        LocalDate dateStart,
                                        @RequestParam(required = false)
                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                        LocalDate dateEnd,
                                        @RequestParam(required = false)
                                        String periodSplit) {
        DateHelper.checkNull(dateStart, "dateStart = ");
        DateHelper.checkNull(dateEnd, "dateEnd = ");

        return vacationReportService.getUserVacations(nikName, dateStart, dateEnd, periodSplit);
    }

    @GetMapping("/user/work/day/last")
    public LocalDate getLastWorkDay(@RequestParam(required = false)
                                    String username,
                                    @RequestParam(required = false)
                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                    LocalDate dateStart,
                                    @RequestParam(required = false)
                                    Integer dayMinus,
                                    @RequestParam(required = false)
                                    Boolean lastWeek) {

        if (dateStart == null) {
            dateStart = LocalDate.now();
        }
        return vacationReportService.getLastWorkDay(username, dateStart, dayMinus, lastWeek);
    }

    @GetMapping("/user/work/day")
    public Boolean isWorkDayUser(@RequestParam(required = false)
                                 String username,
                                 @RequestParam(required = false)
                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                 LocalDate date
    ) {
        if (date == null) {
            date = LocalDate.now();
        }
        return vacationReportService.isWorkDayUser(date, username);
    }

    @GetMapping("/work/day/after/week")
    public Boolean isDayAfterWeek(@RequestParam(required = false)
                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                  LocalDate date,
                                  @RequestParam(required = false)
                                  Integer dayMinus) {
        if (date == null) {
            date = LocalDate.now();
        }
        return vacationReportService.isDayAfterWeek(date, dayMinus);
    }

}

