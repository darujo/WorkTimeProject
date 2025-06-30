package ru.darujo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.darujo.assistant.helper.DataHelper;
import ru.darujo.dto.calendar.UserVacationsDto;
import ru.darujo.service.VacationReportService;

import java.sql.Timestamp;
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
    public UserVacationsDto getWeekWork(@RequestParam(required = false) String nikName,
                                        @RequestParam(required = false, name = "dateStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateStartStr,
                                        @RequestParam(required = false, name = "dateEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateEndStr,
                                        @RequestParam(required = false) String periodSplit) {
        Timestamp dateStart = DataHelper.DTZToDate(dateStartStr, "dateStart = ", true);
        Timestamp dateEnd = DataHelper.DTZToDate(dateEndStr, "dateEnd = ", true);

        return vacationReportService.getUserVacations(nikName, dateStart, dateEnd, periodSplit);
    }
}
