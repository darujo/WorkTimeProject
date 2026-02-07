package ru.darujo.api;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.darujo.assistant.helper.DateHelper;
import ru.darujo.convertor.WorkConvertor;
import ru.darujo.dto.work.WorkDto;
import ru.darujo.dto.work.WorkEditDto;
import ru.darujo.dto.work.WorkLittleDto;
import ru.darujo.model.StageZiFind;
import ru.darujo.model.WorkFull;
import ru.darujo.model.WorkLittle;
import ru.darujo.service.WorkService;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
@RestController()
@RequestMapping("/v1/works")
public class WorkController {
    private WorkService workService;

    @Autowired
    public void setWorkService(WorkService workService) {
        this.workService = workService;
    }


    @GetMapping("/find")
    public Iterable<WorkFull> workList(@RequestParam(required = false) String name,
                                   @RequestParam(required = false) String task
    ) {
        long curTime = System.nanoTime();

        Iterable<WorkFull> works = workService.findWorks(1, 100000000, name, null, null, null, null, null, task, null, null);
        float time_last = (System.nanoTime() - curTime) * 0.000000001f;
        log.info("Время выполнения workList {}", time_last);
        return works;

    }


    @GetMapping("/{id}")
    public WorkEditDto workEdit(@PathVariable long id,
                                @RequestParam("system_project") Long projectId) {
        WorkFull work = workService.findById(id, projectId);
        WorkEditDto workEditDto = WorkConvertor.getWorkEditDto(work);
        workService.updWorkPlanTime(workEditDto);
        return workEditDto;
    }

    @GetMapping("/right/{right}")
    public boolean checkRight(@PathVariable String right,
                              @RequestParam(name = "system_right", required = false) List<String> rights) {
        return workService.checkRight(right, rights);
    }

    @PostMapping("")
    public WorkDto workSave(@RequestHeader String userName,
                            @RequestBody WorkEditDto workDto,
                            @RequestParam("system_right") List<String> rights,
                            @RequestParam("system_project") Long projectId) {
        workService.checkRight("edit", rights);
        WorkFull work = workService.saveWork(userName, WorkConvertor.getWork(workDto, projectId));

        return WorkConvertor.getWorkDto(work);
    }

    @DeleteMapping("/{id}")
    public void deleteWork(@PathVariable long id,
                           @RequestParam("system_right") List<String> rights
    ) {
        workService.checkRight("edit", rights);
        workService.deleteWork(id);
    }

    @GetMapping("")
    public Page<@NonNull WorkDto> workPage(@RequestParam(defaultValue = "1") int page,
                                           @RequestParam(defaultValue = "10") int size,
                                           @RequestParam(required = false) String name,
                                           @RequestParam(defaultValue = "15") Integer stageZi,
                                           @RequestParam(required = false) Long codeSap,
                                           @RequestParam(required = false) String codeZi,
                                           @RequestParam(required = false) String task,
                                           @RequestParam(required = false) Long releaseId,
                                           @RequestParam(defaultValue = "release.name") String sort,
                                           @RequestParam("system_project") Long projectId
    ) {
        StageZiFind stageZiFind = new StageZiFind(stageZi);
        long curTime = System.nanoTime();
        Page<@NonNull WorkDto> workDTOs = workService.findWorks(page, size, name, sort, stageZiFind.getStageZiGe(), stageZiFind.getStageZiLe(), codeSap, codeZi, task, releaseId, projectId)
                .map(WorkConvertor::getWorkDto);
        workDTOs.forEach(workService::updWorkPlanTime);
        float time_last = (curTime - System.nanoTime()) * 0.000000001f;
        log.info("Время выполнения WorkPage {}", time_last);
        return workDTOs;
    }


    @GetMapping("/obj/little")
    public Page<@NonNull WorkLittleDto> workLittlePage(@RequestParam(defaultValue = "1") int page,
                                                       @RequestParam(defaultValue = "10") int size,
                                                       @RequestParam(required = false) String name,
                                                       @RequestParam(defaultValue = "15") Integer stageZi,
                                                       @RequestParam(required = false) String sort,
                                                       @RequestParam("system_project") Long projectId) {
        StageZiFind stageZiFind = new StageZiFind(stageZi);

        return (workService.findWorkLittle(page, size, name, sort, stageZiFind.getStageZiGe(), stageZiFind.getStageZiLe(), null, null, null, null, projectId)).map(WorkConvertor::getWorkLittleDto);
    }

    @GetMapping("/obj/little/{id}")
    public WorkLittleDto workLittleDto(@PathVariable long id,
                                       @RequestParam(name = "system_project", required = false) Long projectId) {
        return WorkConvertor.getWorkLittleDto(workService.findLittleById(id, projectId));
    }

    @GetMapping("/refresh/{id}")
    public boolean taskRefresh(@PathVariable long id,
                               @RequestParam(required = false, name = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateStr,
                               @RequestParam Long projectId
    ) {
        Timestamp date = DateHelper.DTZToDate(dateStr, "date", false);
        return workService.setWorkDate(id, projectId, date);

    }

    @GetMapping("/change/{id}/rated")
    public WorkLittle ChangeRated(@RequestHeader String username,
                                  @PathVariable long id,
                                  @RequestParam(required = false, name = "rated") Boolean rated,
                                  @RequestParam("system_project") Long projectId
    ) {
        return workService.setRated(username, id, projectId, rated);

    }
}
