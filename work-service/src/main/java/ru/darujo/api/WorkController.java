package ru.darujo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.darujo.convertor.WorkConvertor;
import ru.darujo.dto.WorkDto;
import ru.darujo.dto.WorkRepDto;
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


    @GetMapping("/{id}")
    public WorkDto WorkEdit(@PathVariable long id) {
        return WorkConvertor.getWorkDto(workService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Задача не найден")));
    }

    @PostMapping("")
    public WorkDto WorkSave(@RequestBody WorkDto workDto) {
        return WorkConvertor.getWorkDto(workService.saveWork(WorkConvertor.getWork(workDto)));
    }

    @DeleteMapping("/{id}")
    public void deleteWork(@PathVariable long id) {
        workService.deleteWork(id);
    }

    @GetMapping("")
    public Page<WorkDto> WorkPage(@RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  @RequestParam(required = false) String name){
        return workService.findWorks(page, size,name).map(WorkConvertor::getWorkDto);
    }
    @GetMapping("/rep")
    public List<WorkRepDto> getTimeWork(@RequestParam(required = false) String userName) {
        return workService.getWorkRep(userName);
    }

}
