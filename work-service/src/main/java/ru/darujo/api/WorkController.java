package ru.darujo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.darujo.convertor.WorkConvertor;
import ru.darujo.dto.*;
import ru.darujo.exceptions.ResourceNotFoundException;
import ru.darujo.model.Work;
import ru.darujo.service.WorkService;

import java.util.List;
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

    @GetMapping("/conv")
    public WorkDto workConv() {
        workService.findWorks(1, 10000, null, null, null, null, null, null, null, null).map(work -> workService.saveWork(WorkConvertor.getWork(WorkConvertor.getWorkEditDto(work))));
        return new WorkDto();
    }

    @GetMapping("/find")
    public Iterable<Work> workList(@RequestParam(required = false) String name,
                                   @RequestParam(required = false) String task
    ) {
        long cur_tiem = System.nanoTime();

        Iterable<Work> works = workService.findWorks(1, 100000000, name, null, null, null, null, null, task,null);
        float time_last = (System.nanoTime() - cur_tiem) * 0.000000001f;
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
                    null,
                    null,
                    null,
                    null,
                    8,
                    null,
                    null,
                    null);
            workService.saveWork(work);
            System.out.println("создана задача " + i);
        }

        return new WorkDto();
    }

    @GetMapping("/{id}")
    public WorkEditDto WorkEdit(@PathVariable long id) {
        return WorkConvertor.getWorkEditDto(workService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Задача не найден")));
    }

    @GetMapping("/right/{right}")
    public boolean checkRight(@PathVariable String right,
                              @RequestHeader(defaultValue = "false", name = "ZI_EDIT") boolean rightEdit,
                              @RequestHeader(defaultValue = "false", name = "ZI_CREATE") boolean rightCreate) {
        right = right.toLowerCase();
        if (right.equals("edit")) {
            if (!rightEdit) {
                throw new ResourceNotFoundException("У вас нет права на редактирование ZI_EDIT");
            }
        } else if (right.equals("create")) {
            if (!rightCreate) {
                throw new ResourceNotFoundException("У вас нет права на редактирование ZI_CREATE");
            }
        }
        return true;

    }

    @PostMapping("")
    public WorkDto WorkSave(@RequestBody WorkEditDto workDto,
                            @RequestHeader(defaultValue = "false", name = "ZI_EDIT") boolean right) {
        if (!right) {
            throw new ResourceNotFoundException("У вас нет права ZI_EDIT");
        }
        return WorkConvertor.getWorkDto(workService.saveWork(WorkConvertor.getWork(workDto)));
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
                                  @RequestParam(required = false) String release,
                                  @RequestParam(defaultValue = "release") String sort) {
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
        long cur_tiem = System.nanoTime();
        Page<WorkDto> workDtos = workService.findWorks(page, size, name, sort, stageZiGe, stageZiLe, codeSap, codeZi, task,release).map(WorkConvertor::getWorkDto);
        float time_last = (cur_tiem - System.nanoTime()) * 0.000000001f;
        System.out.println("Время выполнения " + time_last);
        return workDtos;
    }

    @GetMapping("/rep")
    public List<WorkRepDto> getTimeWork(@RequestParam(required = false) String ziName,
                                        @RequestParam(required = false) Boolean availWork,
                                        @RequestParam(defaultValue = "15") Integer stageZi,
                                        @RequestParam(required = false) String release,
                                        @RequestParam(required = false)  String[] sort) {
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
        return workService.getWorkRep(ziName, availWork, stageZiGe, stageZiLe, release, sort);
    }

    @GetMapping("/rep/factwork")
    public List<WorkFactDto> getFactWork(@RequestParam(required = false) String userName) {
        return workService.getWorkFactRep(userName);
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
        return workService.findWorkLittle(page, size, name, sort, stageZiGe, stageZiLe).map(WorkConvertor::getWorkLittleDto);
    }

    @GetMapping("/obj/little/{id}")
    public WorkLittleDto WorkLittleDto(@PathVariable long id) {
        return WorkConvertor.getWorkLittleDto(workService.findLittleById(id).orElseThrow(() -> new ResourceNotFoundException("Задача не найден")));
    }
}
