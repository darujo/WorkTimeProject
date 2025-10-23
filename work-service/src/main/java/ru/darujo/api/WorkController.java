package ru.darujo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.darujo.assistant.helper.DataHelper;
import ru.darujo.convertor.WorkConvertor;
import ru.darujo.dto.work.WorkDto;
import ru.darujo.dto.work.WorkEditDto;
import ru.darujo.dto.work.WorkLittleDto;
import ru.darujo.exceptions.ResourceNotFoundException;
import ru.darujo.model.Work;
import ru.darujo.service.WorkService;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

@RestController()
@RequestMapping("/v1/works")
public class WorkController {
    private WorkService workService;

    @Autowired
    public void setWorkService(WorkService workService) {
        this.workService = workService;
    }


    @GetMapping("/find")
    public Iterable<Work> workList(@RequestParam(required = false) String name,
                                   @RequestParam(required = false) String task
    ) {
        long curTime = System.nanoTime();

        Iterable<Work> works = workService.findWorks(1, 100000000, name, null, null, null, null, null, task, null);
        float time_last = (System.nanoTime() - curTime) * 0.000000001f;
        System.out.println("Время выполнения " + time_last);
        return works;

    }


    @GetMapping("/{id}")
    public WorkEditDto WorkEdit(@PathVariable long id) {
        Work work = workService.findById(id);
        WorkEditDto workEditDto = WorkConvertor.getWorkEditDto(work);
        workService.updWorkPlanTime(workEditDto);
        return workEditDto;
    }

    @GetMapping("/right/{right}")
    public boolean checkRight(@PathVariable String right,
                              @RequestHeader(defaultValue = "false", name = "ZI_EDIT") boolean rightEdit,
                              @RequestHeader(defaultValue = "false", name = "ZI_CREATE") boolean rightCreate) {
        return workService.checkRight(right, rightEdit, rightCreate);
    }

    @PostMapping("")
    public WorkDto WorkSave( @RequestHeader String userName,
                            @RequestBody WorkEditDto workDto,
                            @RequestHeader(defaultValue = "false", name = "ZI_EDIT") boolean right) {
        if (!right) {
            throw new ResourceNotFoundException("У вас нет права ZI_EDIT");
        }
        Work work = workService.saveWork(userName, WorkConvertor.getWork(workDto));
        return WorkConvertor.getWorkDto(work);
    }

    @DeleteMapping("/{id}")
    public void deleteWork(@PathVariable long id) {
        workService.deleteWork(id);
    }

    @GetMapping("")
    public Page<WorkDto> WorkPage(@RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  @RequestParam(required = false) String name,
                                  @RequestParam(defaultValue = "15") Integer stageZi,
                                  @RequestParam(required = false) Long codeSap,
                                  @RequestParam(required = false) String codeZi,
                                  @RequestParam(required = false) String task,
                                  @RequestParam(required = false) Long releaseId,
                                  @RequestParam(defaultValue = "release.name") String sort) {
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
        long curTime = System.nanoTime();
        Page<WorkDto> workDTOs = workService.findWorks(page, size, name, sort, stageZiGe, stageZiLe, codeSap, codeZi, task, releaseId)
                .map(WorkConvertor::getWorkDto);
        workDTOs.forEach(workService::updWorkPlanTime);
        float time_last = (curTime - System.nanoTime()) * 0.000000001f;
        System.out.println("Время выполнения " + time_last);
        return workDTOs;
    }


    @GetMapping("/obj/little")
    public Page<WorkLittleDto> WorkLittlePage(@RequestParam(defaultValue = "1") int page,
                                              @RequestParam(defaultValue = "10") int size,
                                              @RequestParam(required = false) String name,
                                              @RequestParam(defaultValue = "15") Integer stageZi,
                                              @RequestParam(required = false) String sort) {
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
        return (workService.findWorkLittle(page, size, name, sort, stageZiGe, stageZiLe, null, null, null, null)).map(WorkConvertor::getWorkLittleDto);
    }

    @GetMapping("/obj/little/{id}")
    public WorkLittleDto WorkLittleDto(@PathVariable long id) {
        return WorkConvertor.getWorkLittleDto(workService.findLittleById(id).orElseThrow(() -> new ResourceNotFoundException("Задача не найден")));
    }

    @GetMapping("/refresh/{id}")
    public boolean TaskRefresh(@PathVariable long id,
                               @RequestParam(required = false, name = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateStr
    ) {
        Timestamp date = DataHelper.DTZToDate(dateStr, "date", true);
        return workService.setWorkDate(id, date);

    }
}
