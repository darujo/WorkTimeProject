package ru.darujo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.darujo.assistant.helper.DataHelper;
import ru.darujo.dto.ListString;
import ru.darujo.dto.workperiod.UserWorkDto;
import ru.darujo.dto.workrep.UserWorkPeriodDto;
import ru.darujo.service.WorkTimeRepService;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@RestController()
@RequestMapping("/v1/worktime/rep/fact")
public class WorkTimeRepController {
    private WorkTimeRepService workTimeRepService;

    @Autowired
    public void setWorkTimeRepService(WorkTimeRepService workTimeRepService) {
        this.workTimeRepService = workTimeRepService;
    }


    @GetMapping("/time")
    public Float getTimeWork(@RequestParam(required = false) Long[] taskId,
                             @RequestParam(required = false) String nikName,
                             @RequestParam(required = false, name = "dateLe") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateLeStr,
                             @RequestParam(required = false, name = "dateGt") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateGtStr,
                             @RequestParam(required = false) String type) {
        Date dateLe = DataHelper.DTZToDate(dateLeStr, "dateLe = ", false);
        Date dateGt = DataHelper.DTZToDate(dateGtStr, "dateGt = ", false);
        if (dateLe == null && dateGt == null) {
            return 0f;
        }
        return workTimeRepService.getTimeWork(taskId, nikName, dateGt, dateLe, type);
    }

    @GetMapping("/user")
    public ListString getFactUser(@RequestParam(required = false) Long[] taskId,
                                  @RequestParam(required = false, name = "dateLe") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateLeStr
                                  ) {
        Date dateLe = DataHelper.DTZToDate(dateLeStr, "dateLe = ", false);
        return workTimeRepService.getFactUser(taskId,dateLe);
    }

    @GetMapping("/user/work")
    public List<UserWorkPeriodDto> getUserWork(@RequestParam(required = false) String nikName,
                                               @RequestParam(defaultValue = "week") String periodSplit,
                                               @RequestParam(required = false, name = "dateStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateStartStr,
                                               @RequestParam(required = false, name = "dateEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateEndStr,
                                               @RequestHeader String username) {
        if (nikName == null || nikName.equals("")) {
            nikName = username;
        }
        switch (periodSplit) {
            case "1":
                periodSplit = "day";
                break;
            case "2":
                periodSplit = "week_day";
                break;
            case "3":
                periodSplit = "week";
                break;
        }
        Timestamp dateStart = DataHelper.DTZToDate(dateStartStr, "dateStart = ", true);
        Timestamp dateEnd = DataHelper.DTZToDate(dateEndStr, "dateEnd = ", true);
        return workTimeRepService.getUserWork(nikName, periodSplit, dateStart, dateEnd);
    }

    @GetMapping("/week")
    public List<UserWorkDto> getWeekWork(@RequestParam(required = false) Long[] taskId,
                                         @RequestParam(required = false) String nikName,
                                         @RequestParam(defaultValue = "false") Boolean addTotal,
                                         @RequestParam(defaultValue = "true") Boolean weekSplit,
                                         @RequestParam(required = false, name = "dateStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateStartStr,
                                         @RequestParam(required = false, name = "dateEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateEndStr) {
        Timestamp dateStart = DataHelper.DTZToDate(dateStartStr, "dateStart = ", false);
        Timestamp dateEnd = DataHelper.DTZToDate(dateEndStr, "dateEnd = ", false);
        return workTimeRepService.getWeekWork(taskId,nikName,addTotal, weekSplit, dateStart, dateEnd);
    }


    @GetMapping("/availTime/{taskId}")
    public Boolean getFactUsers(@PathVariable long taskId
    ) {
        return workTimeRepService.getAvailTime(taskId);
    }

    @GetMapping("/lastTime")
    public Timestamp getLastTime(@RequestParam(required = false) Long[] taskId
    ) {
        return workTimeRepService.getLastTime(taskId);
    }
}