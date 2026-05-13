package ru.darujo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.darujo.assistant.helper.DateHelper;
import ru.darujo.dto.ListString;
import ru.darujo.dto.workperiod.UserWorkDto;
import ru.darujo.service.TaskRepService;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

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
                             @RequestParam(required = false) List<Long> workId,
                             @RequestParam(required = false) Long projectId,
                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateLe,
                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateGt,
                             @RequestParam(required = false) String type
    ) {
        return taskRepService.getTaskTime(
                nikName,
                codeBTS,
                codeDEVBO,
                description,
                workId,
                projectId,
                DateHelper.zDTToLD(dateLe),
                DateHelper.zDTToLD(dateGt),
                type);
    }

    @GetMapping("/user")
    public ListString getFactUsers(@RequestParam(required = false) List<Long> workId,
                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateLe,
                                   @RequestParam(required = false) Long projectId
    ) {
        return taskRepService.getFactUsers(
                workId, projectId, DateHelper.zDTToLD(dateLe));
    }

    @GetMapping("/avail")
    public Boolean getFactUsers(@RequestParam List<Long> workId,
                                @RequestParam(required = false) Long projectId
    ) {
        if (workId == null || workId.isEmpty()) {
            return false;
        }
        return taskRepService.getAvailTime(workId, projectId);
    }

    @GetMapping("/week")
    public List<UserWorkDto> getWeekWork(@RequestParam(required = false) List<Long> workId,
                                         @RequestParam(required = false) Long projectId,
                                         @RequestParam(required = false) String nikName,
                                         @RequestParam(required = false) Boolean addTotal
    ) {
        return taskRepService.getWeekWork(workId, projectId, nikName, addTotal);
    }

    @GetMapping("/lastTime")
    public LocalDate getLastTime(@RequestParam long workId,
                                 @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateLe,
                                 @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateGe

    ) {
        List<Long> workIDList = new ArrayList<>();
        workIDList.add(workId);
        return taskRepService.getLastTime(workIDList, DateHelper.zDTToLD(dateLe), DateHelper.zDTToLD(dateGe));
    }
}