package ru.darujo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.darujo.convertor.WorkTypeConvertor;
import ru.darujo.dto.ratestage.WorkTypeDto;
import ru.darujo.exceptions.ResourceNotFoundException;
import ru.darujo.service.WorkTypeService;

import java.util.List;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("/v1/type")
public class WorkTypeController {
    private WorkTypeService workTypeService;
    @Autowired
    public void setWorkType(WorkTypeService workTypeService) {
        this.workTypeService = workTypeService;
    }


    @GetMapping("/{id}")
    public WorkTypeDto WorkCriteriaEdit(@PathVariable long id) {
        return WorkTypeConvertor.getWorkTypeDto(workTypeService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Не найдена разбивка")));
    }

    @PostMapping("")
    public WorkTypeDto WorkStageSave(@RequestBody WorkTypeDto workTypeDto) {
        return WorkTypeConvertor.getWorkTypeDto(workTypeService.saveWorkType(WorkTypeConvertor.getWorkType(workTypeDto)));
    }

    @DeleteMapping("/{id}")
    public void deleteWorkType(@PathVariable long id) {
        workTypeService.deleteWorkType(id);
    }

    @GetMapping("")
    public List<WorkTypeDto> WorkStageList(@RequestParam Long workId) {
        return workTypeService.findWorkCriteria(workId).stream().map(WorkTypeConvertor::getWorkTypeDto).collect(Collectors.toList());
    }

}