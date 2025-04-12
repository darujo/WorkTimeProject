package ru.darujo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.darujo.dto.MapStringFloat;
import ru.darujo.dto.PageDto;
import ru.darujo.dto.parsing.DateParser;
import ru.darujo.dto.workperiod.UserWorkFormDto;
import ru.darujo.dto.workperiod.WorkUserTime;
import ru.darujo.dto.workrep.WorkFactDto;
import ru.darujo.dto.workrep.WorkRepDto;
import ru.darujo.service.WorkRepService;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.List;

@RestController()
@RequestMapping("/v1/works/rep")
public class WorkRepController extends DateParser {
    private WorkRepService workRepService;

    @Autowired
    public void setWorkRepService(WorkRepService workRepService) {
        this.workRepService = workRepService;
    }


    @GetMapping("")
    public List<WorkRepDto> getTimeWork(@RequestParam(required = false) String ziName,
                                        @RequestParam(required = false) Boolean availWork,
                                        @RequestParam(defaultValue = "15") Integer stageZi,
                                        @RequestParam(required = false) String release,
                                        @RequestParam(required = false) String[] sort) {
        Integer stageZiLe = null;
        Integer stageZiGe = null;
        if (stageZi != null) {
            if (stageZi < 10) {
                stageZiGe = stageZi;
                stageZiLe = stageZi;
            } else {
                stageZiLe = stageZi - 10;
            }
        }
        return workRepService.getWorkRep(ziName, availWork, stageZiGe, stageZiLe, release, sort);
    }

    @GetMapping("/work/fact")
    public PageDto<WorkFactDto> getFactWork(@RequestParam(defaultValue = "1") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestParam(required = false) String nikName,
                                            @RequestParam(required = false) String name,
                                            @RequestParam(defaultValue = "15") Integer stageZi,
                                            @RequestParam(required = false) Long codeSap,
                                            @RequestParam(required = false) String codeZi,
                                            @RequestParam(required = false) String task,
                                            @RequestParam(required = false) String release,
                                            @RequestParam(defaultValue = "release") String sort,
                                            @RequestParam(defaultValue = "true") boolean hideNotTime) {
        if (nikName != null && nikName.equals("")) {
            nikName = null;
        }
        Integer stageZiLe = null;
        Integer stageZiGe = null;
        if (stageZi != null) {
            if (stageZi < 10) {
                stageZiGe = stageZi;
                stageZiLe = stageZi;
            } else {
                stageZiLe = stageZi - 10;
            }
        }
        return workRepService.getWorkFactRep(page, size, nikName, name, stageZiGe, stageZiLe, codeSap, codeZi, task, release, sort, hideNotTime);
    }

    @GetMapping("/time/fact")
    public Float getFactWork(@RequestParam Long workId,
                             @RequestParam Integer stage,
                             @RequestParam(required = false) String nikName
    ) {
        return workRepService.getFactWork(workId, stage, nikName);
    }

    @GetMapping("/time/fact/stage0")
    public MapStringFloat getFactWork(@RequestParam Long workId,
                                      @RequestParam(required = false) String nikName) {
        return workRepService.getFactWorkStage0(workId, nikName);
    }

    @GetMapping("/fact/week")
    public List<WorkUserTime> getWeekWork(@RequestParam(defaultValue = "false") boolean ziSplit,
                                          @RequestParam(required = false) String nikName,
                                          @RequestParam(required = false) Boolean addTotal,
                                          @RequestParam(required = false) Boolean weekSplit,
                                          @RequestParam(required = false, name = "dateStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateStartStr,
                                          @RequestParam(required = false, name = "dateEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateEndStr) {
        Timestamp dateStart = stringToDate(dateStartStr, "dateStart = ", true);
        Timestamp dateEnd = stringToDate(dateEndStr, "dateEnd = ", true);
        return workRepService.getWeekWork(ziSplit, addTotal, nikName, weekSplit, dateStart, dateEnd);
    }

}
