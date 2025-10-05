package ru.darujo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.darujo.assistant.helper.DataHelper;
import ru.darujo.dto.ListString;
import ru.darujo.dto.workperiod.UserWorkDto;
import ru.darujo.service.TaskRepService;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.*;

@RestController()
@RequestMapping("/v1/task/rep/fact")
public class TaskRepController {
    private TaskRepService taskRepService;

    @Autowired
    public void setTaskRepService(TaskRepService taskRepService) {
        this.taskRepService = taskRepService;
    }

    @GetMapping("/time")
    public Float getTaskTime(@RequestParam(required = false) String nikName,
                             @RequestParam(required = false) String codeBTS,
                             @RequestParam(required = false) String codeDEVBO,
                             @RequestParam(required = false) String description,
                             @RequestParam(required = false) Long workId,
                             @RequestParam(required = false, name = "dateLe") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateLeStr,
                             @RequestParam(required = false, name = "dateGt") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateGtStr,
                             @RequestParam(required = false) String type
    ) {
        Date dateLe = DataHelper.DTZToDate(dateLeStr, "dateLe = ");
        Date dateGt = DataHelper.DTZToDate(dateGtStr, "dateGt = ");
        return taskRepService.getTaskTime(
                nikName,
                codeBTS,
                codeDEVBO,
                description,
                workId,
                dateLe,
                dateGt,
                type);
    }

    @GetMapping("/user")
    public ListString getFactUsers(@RequestParam(required = false) Long workId,
                                   @RequestParam(required = false, name = "dateLe") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateLeStr
                                   ) {
        Date dateLe = DataHelper.DTZToDate(dateLeStr, "dateLe = ");

        return taskRepService.getFactUsers(
                workId,dateLe);
    }
    @GetMapping("/avail/{workId}")
    public Boolean getFactUsers(@PathVariable long workId
    ) {
        return taskRepService.getAvailTime(workId);
    }

    @GetMapping("/week")
    public List<UserWorkDto> getWeekWork(@RequestParam(required = false) Long workId,
                                         @RequestParam(required = false) String nikName,
                                         @RequestParam(required = false) Boolean addTotal
    ){
        return taskRepService.getWeekWork(workId,nikName, addTotal);
    }

    @GetMapping("/lastTime/{workId}")
    public Timestamp getLastTime(@PathVariable long workId
    ) {
        return taskRepService.getLastTime(workId);
    }
}