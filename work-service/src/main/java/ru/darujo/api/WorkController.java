package ru.darujo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.darujo.convertor.WorkConvertor;
import ru.darujo.dto.*;
import ru.darujo.exceptions.ResourceNotFoundException;
import ru.darujo.service.WorkService;

import java.util.List;

@RestController()
@RequestMapping("/v1/works")
public class WorkController {
    private WorkService workService;

    @Autowired
    public void setWorkService(WorkService workService) {
        this.workService = workService;
    }

    @GetMapping("/conv")
    public WorkDto workConv() {
        workService.findWorks(1, 10000, null, null, null).map(work -> workService.saveWork(WorkConvertor.getWork(WorkConvertor.getWorkEditDto(work))));
        return new WorkDto();
    }

    @GetMapping("/{id}")
    public WorkEditDto WorkEdit(@PathVariable long id) {
        return WorkConvertor.getWorkEditDto(workService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Задача не найден")));
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
                                  @RequestParam(defaultValue = "6") Integer stageZiLt,
                                  @RequestParam(required = false) String sort) {

        return workService.findWorks(page, size, name, sort, stageZiLt).map(WorkConvertor::getWorkDto);
    }

    @GetMapping("/rep")
    public List<WorkRepDto> getTimeWork(@RequestParam(required = false) String ziName) {
        return workService.getWorkRep(ziName);
    }

    @GetMapping("/rep/factwork")
    public List<WorkFactDto> getFactWork(@RequestParam(required = false) String userName) {
        return workService.getWorkFactRep(userName);
    }

    @GetMapping("/obj/little")
    public Page<WorkLittleDto> WorkLittlePage(@RequestParam(defaultValue = "1") int page,
                                              @RequestParam(defaultValue = "10") int size,
                                              @RequestParam(required = false) String name,
                                              @RequestParam(defaultValue = "6") Integer stageZiLt,
                                              @RequestParam(required = false) String sort) {
        return workService.findWorkLittle(page, size, name, sort, stageZiLt).map(WorkConvertor::getWorkLittleDto);
    }

    @GetMapping("/obj/little/{id}")
    public WorkLittleDto WorkLittleDto(@PathVariable long id) {
        return WorkConvertor.getWorkLittleDto(workService.findLittleById(id).orElseThrow(() -> new ResourceNotFoundException("Задача не найден")));
    }
}
