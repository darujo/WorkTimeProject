package ru.darujo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.darujo.dto.ListString;
import ru.darujo.dto.workperiod.UserWorkDto;
import ru.darujo.dto.workperiod.WorkUserFactPlan;
import ru.darujo.dto.workrep.UserWorkPeriodDto;
import ru.darujo.service.WorkTimeRepService;

import java.time.LocalDate;
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
                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate dateLe,
                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate dateGt,
                             @RequestParam(required = false) String type) {
        if (dateLe == null && dateGt == null) {
            return 0f;
        }
        return workTimeRepService.getTimeWork(taskId, nikName, dateGt, dateLe, type);
    }

    @GetMapping("/user")
    public ListString getFactUser(@RequestParam(required = false) Long[] taskId,
                                  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate dateLe
    ) {
        return workTimeRepService.getFactUser(taskId, dateLe);
    }

    @GetMapping("/user/work")
    public List<UserWorkPeriodDto> getUserWork(@RequestParam(required = false) String nikName,
                                               @RequestParam(defaultValue = "week") String periodSplit,
                                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate dateStart,
                                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate dateEnd,
                                               @RequestHeader String username) {
        if (nikName == null || nikName.isEmpty()) {
            nikName = username;
        }
        periodSplit = periodConvert(periodSplit);
        return workTimeRepService.getUserWork(nikName, periodSplit, dateStart, dateEnd);
    }

    @GetMapping("/user/work/only")
    public WorkUserFactPlan getUserWorkOnly(@RequestParam(required = false) String nikName,
                                            @RequestParam(defaultValue = "week") String periodSplit,
                                            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate dateStart,
                                            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate dateEnd) {
        periodSplit = periodConvert(periodSplit);
        return workTimeRepService.getUserWorkOnly(nikName, periodSplit, dateStart, dateEnd);
    }

    private String periodConvert(String periodSplit) {
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
        return periodSplit;
    }

    @GetMapping("/week")
    public List<UserWorkDto> getWeekWork(@RequestParam(required = false) Long[] taskId,
                                         @RequestParam(required = false) String nikName,
                                         @RequestParam(defaultValue = "false") Boolean addTotal,
                                         @RequestParam(defaultValue = "true") Boolean weekSplit,
                                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate dateStart,
                                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate dateEnd) {
        return workTimeRepService.getWeekWork(taskId, nikName, addTotal, weekSplit, dateStart, dateEnd);
    }


    @GetMapping("/availTime/{taskId}")
    public Boolean getFactUsers(@PathVariable long taskId
    ) {
        return workTimeRepService.getAvailTime(taskId);
    }

    @GetMapping("/lastTime")
    public LocalDate getLastTime(@RequestParam(required = false) Long[] taskId,
                                 @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate dateLe,
                                 @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate dateGe) {
            return workTimeRepService.getLastTime(taskId, dateGe, dateLe);
        }
    }