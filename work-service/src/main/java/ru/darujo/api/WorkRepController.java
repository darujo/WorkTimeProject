package ru.darujo.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.darujo.assistant.helper.DateHelper;
import ru.darujo.dto.MapStringFloat;
import ru.darujo.dto.PageDto;
import ru.darujo.dto.PageObjDto;
import ru.darujo.dto.workperiod.WorkUserTime;
import ru.darujo.dto.workrep.WorkFactDto;
import ru.darujo.dto.workrep.WorkGraphsDto;
import ru.darujo.dto.workrep.WorkRepDto;
import ru.darujo.model.StageZiFind;
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
        StageZiFind stageZiFind = new StageZiFind(stageZi);
        return workRepService.getWorkRep(ziName, availWork, stageZiFind.getStageZiGe(), stageZiFind.getStageZiLe(), releaseId, sort);
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
        if (nikName != null && nikName.isEmpty()) {
            nikName = null;
        }
        StageZiFind stageZiFind = new StageZiFind(stageZi);
        return workRepService.getWorkFactRep(page, size, nikName, name, stageZiFind.getStageZiGe(), stageZiFind.getStageZiLe(), codeSap, codeZi, task, releaseId, sort, hideNotTime);
    }

    @GetMapping("/time/fact")
    public Float getFactWork(@RequestParam Long workId,
                             @RequestParam Integer stage,
                             @RequestParam(required = false) String nikName
    ) {
        return workRepService.getFactWork(workId, stage, nikName);
    }

    @GetMapping("/time/fact/stage")
    public MapStringFloat getFactWork(@RequestParam Long workId,
                                      @RequestParam(required = false) String nikName,
                                      @RequestParam(defaultValue = "0") Integer stage) {
        return workRepService.getFactWorkStage(workId, nikName, stage);
    }

    @GetMapping("/fact/week")
    public List<WorkUserTime> getWeekWork(@RequestParam(defaultValue = "false") boolean ziSplit,
                                          @RequestParam(required = false) String nikName,
                                          @RequestParam(required = false) Boolean addTotal,
                                          @RequestParam(required = false) Boolean weekSplit,

                                          @RequestParam(required = false, name = "dateStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateStartStr,
                                          @RequestParam(required = false, name = "dateEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateEndStr,

                                          @RequestParam(required = false) Integer page,
                                          @RequestParam(required = false) Integer size,
                                          @RequestParam(required = false) String name,
                                          @RequestParam(defaultValue = "15") Integer stageZi,
                                          @RequestParam(required = false) Long codeSap,
                                          @RequestParam(required = false) String codeZi,
                                          @RequestParam(required = false) String task,
                                          @RequestParam(required = false) List<Long> releaseId,
                                          @RequestParam(defaultValue = "release") String sort) {
        Timestamp dateStart = DateHelper.DTZToDate(dateStartStr, "dateStart = ", true);
        Timestamp dateEnd = DateHelper.DTZToDate(dateEndStr, "dateEnd = ", true);
        StageZiFind stageZiFind = new StageZiFind(stageZi);

        return workRepService.getWeekWork(ziSplit, addTotal, nikName, weekSplit, dateStart, dateEnd,
                page, size, name, stageZiFind.getStageZiGe(), stageZiFind.getStageZiLe(), codeSap, codeZi, task, releaseId, sort);
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
        Timestamp dateEnd = DateHelper.DTZToDate(dateEndStr, "dateEnd = ", false);
        Timestamp dateStart = DateHelper.DTZToDate(dateStartStr, "dateStart = ", false);
        StageZiFind stageZiFind = new StageZiFind(stageZi);
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

        return workRepService.getWorkGraphRep(page, size, nameZi, stageZiFind.getStageZiGe(), stageZiFind.getStageZiLe(), codeSap, codeZi, task, releaseId, sort, dateStart, dateEnd, period);
    }


}
