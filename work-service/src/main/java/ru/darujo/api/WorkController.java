package ru.darujo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.darujo.convertor.WorkConvertor;
import ru.darujo.dto.WorkDto;
import ru.darujo.exceptions.ResourceNotFoundException;
import ru.darujo.service.WorkService;

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
        return WorkConvertor.getWorkDto(workService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Продукт не найден")));
    }

    @PostMapping("")
    public WorkDto WorkSave(@RequestBody WorkDto workDto) {
        return WorkConvertor.getWorkDto(workService.saveWork(WorkConvertor.getWork(workDto)));
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable long id) {
        workService.deleteWork(id);
    }

    @GetMapping("")
    public Page<WorkDto> productsMinMax(@RequestParam(defaultValue = "1") int page,
                                        @RequestParam(defaultValue = "10") int size,
                                        @RequestParam String name) {
        return workService.findWorks(page, size,name).map(WorkConvertor::getWorkDto);
    }


}
