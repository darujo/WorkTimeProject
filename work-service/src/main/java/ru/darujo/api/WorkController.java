package ru.darujo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.darujo.convertor.WorkConvertor;
import ru.darujo.dto.work.WorkDto;
import ru.darujo.dto.work.WorkEditDto;
import ru.darujo.dto.work.WorkLittleDto;
import ru.darujo.exceptions.ResourceNotFoundException;
import ru.darujo.model.Work;
import ru.darujo.model.WorkLittle;
import ru.darujo.service.WorkService;

import java.util.Random;

@RestController()
@RequestMapping("/v1/works")
public class WorkController {
    private WorkService workService;

    @Autowired
    public void setWorkService(WorkService workService) {
        this.workService = workService;
    }

    Random random = new Random();

//    @GetMapping("/conv")
//    public WorkDto workConv() {
//        workService.findWorks(1, 10000, null, null, null, null, null, null, null, null).map(work -> workService.saveWork(WorkConvertor.getWork(WorkConvertor.getWorkEditDto(work))));
//        return new WorkDto();
//    }

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

    public static String generateString(Random rng, String characters, int length) {
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = characters.charAt(rng.nextInt(characters.length()));
        }
        return new String(text);
    }

    @GetMapping("/convAddWork")
    public WorkDto workAddConv() {

        for (int i = 0; i < 40000; i++) {
            String str = "Зи с номером" + i + "или другим";
            if (i % 1000 == 0) {
                str = str + "тест";
            }
            str = str + "как-то так";


            Work work = new Work(null,
                    null,
                    null,
                    generateString(random, str, 15),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    generateString(random, str, 15),
                    null,
                    null,
                    null,
                    8,
                    null);
            workService.saveWork(work);
            System.out.println("создана задача " + i);
        }

        return new WorkDto();
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
    public WorkDto WorkSave(@RequestBody WorkEditDto workDto,
                            @RequestHeader(defaultValue = "false", name = "ZI_EDIT") boolean right) {
        if (!right) {
            throw new ResourceNotFoundException("У вас нет права ZI_EDIT");
        }
        Work work = workService.saveWork(WorkConvertor.getWork(workDto));
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
        return ((Page<WorkLittle>) workService.findWorkLittle(page, size, name, sort, stageZiGe, stageZiLe, null, null, null, null)).map(WorkConvertor::getWorkLittleDto);
    }

    @GetMapping("/obj/little/{id}")
    public WorkLittleDto WorkLittleDto(@PathVariable long id) {
        return WorkConvertor.getWorkLittleDto(workService.findLittleById(id).orElseThrow(() -> new ResourceNotFoundException("Задача не найден")));
    }
}
