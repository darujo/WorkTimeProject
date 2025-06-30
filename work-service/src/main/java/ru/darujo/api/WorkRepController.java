package ru.darujo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.darujo.assistant.helper.DataHelper;
import ru.darujo.dto.MapStringFloat;
import ru.darujo.dto.PageDto;
import ru.darujo.dto.PageObjDto;
import ru.darujo.dto.workperiod.WorkUserTime;
import ru.darujo.dto.workrep.WorkFactDto;
import ru.darujo.dto.workrep.WorkGraphsDto;
import ru.darujo.dto.workrep.WorkRepDto;
import ru.darujo.service.WorkRepService;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.List;

@RestController()
@RequestMapping("/v1/works/rep")
public class WorkRepController {
    private WorkRepService workRepService;

    @Autowired
    public void setWorkRepService(WorkRepService workRepService) {
        this.workRepService = workRepService;
    }


    @GetMapping("")
    public List<WorkRepDto> getTimeWork(@RequestParam(required = false) String ziName,
                                        @RequestParam(required = false) Boolean availWork,
                                        @RequestParam(defaultValue = "15") Integer stageZi,
                                        @RequestParam(required = false) Long releaseId,
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
        return workRepService.getWorkRep(ziName, availWork, stageZiGe, stageZiLe, releaseId, sort);
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
                                            @RequestParam(required = false) Long releaseId,
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
        return workRepService.getWorkFactRep(page, size, nikName, name, stageZiGe, stageZiLe, codeSap, codeZi, task, releaseId, sort, hideNotTime);
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
                                          @RequestParam(required = false, name = "dateEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateEndStr,

                                          @RequestParam(defaultValue = "1") int page,
                                          @RequestParam(defaultValue = "10") int size,
                                          @RequestParam(required = false) String name,
                                          @RequestParam(defaultValue = "15") Integer stageZi,
                                          @RequestParam(required = false) Long codeSap,
                                          @RequestParam(required = false) String codeZi,
                                          @RequestParam(required = false) String task,
                                          @RequestParam(required = false) Long releaseId,
                                          @RequestParam(defaultValue = "release") String sort) {
        Timestamp dateStart = DataHelper.DTZToDate(dateStartStr, "dateStart = ", true);
        Timestamp dateEnd = DataHelper.DTZToDate(dateEndStr, "dateEnd = ", true);
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
        return workRepService.getWeekWork(ziSplit, addTotal, nikName, weekSplit, dateStart, dateEnd,
                page, size, name, stageZiGe, stageZiLe, codeSap, codeZi, task, releaseId, sort);
    }

    @GetMapping("/graph")
    public PageObjDto<WorkGraphsDto> getWorkGraphRep(@RequestParam(defaultValue = "1") Integer page,
                                                     @RequestParam(defaultValue = "10") Integer size,
                                                     @RequestParam(required = false) String nameZi,
                                                     @RequestParam(defaultValue = "15") Integer stageZi,
                                                     @RequestParam(required = false) Long codeSap,
                                                     @RequestParam(required = false) String codeZi,
                                                     @RequestParam(required = false) String task,
                                                     @RequestParam(required = false) Long releaseId,
                                                     @RequestParam(required = false) String sort,
                                                     @RequestParam(required = false, name = "dateStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateStartStr,
                                                     @RequestParam(required = false, name = "dateEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateEndStr,
                                                     @RequestParam(required = false) String period) {
        Timestamp dateEnd = DataHelper.DTZToDate(dateEndStr, "dateEnd = ", false);
        Timestamp dateStart = DataHelper.DTZToDate(dateStartStr, "dateStart = ", false);
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
        if (dateStart == null) {
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE, -100);
            dateStart = new Timestamp(c.getTimeInMillis());
        }
        if (dateEnd == null) {
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE, 100);

            dateEnd = new Timestamp(c.getTimeInMillis());
        }

        return workRepService.getWorkGraphRep(page, size, nameZi, stageZiGe, stageZiLe, codeSap, codeZi, task, releaseId, sort, dateStart, dateEnd, period);
    }

}
