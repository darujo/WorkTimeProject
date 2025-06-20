package ru.darujo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.darujo.convertor.WorkCriteriaConvertor;
import ru.darujo.dto.ratestage.WorkCriteriaDto;
import ru.darujo.exceptions.ResourceNotFoundException;
import ru.darujo.service.WorkCriteriaService;

import java.util.List;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("/v1/criteria")
public class WorkCriteriaController {
    private WorkCriteriaService workCriteriaService;
    @Autowired
    public void setWorkCriteria(WorkCriteriaService workCriteriaService) {
        this.workCriteriaService = workCriteriaService;
    }


    @GetMapping("/{id}")
    public WorkCriteriaDto WorkCriteriaEdit(@PathVariable long id) {
        return WorkCriteriaConvertor.getWorkCriteriaDto(workCriteriaService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Не найдена разбивка")));
    }

    @PostMapping("")
    public WorkCriteriaDto WorkStageSave(@RequestBody WorkCriteriaDto workCriteriaDto) {
        return WorkCriteriaConvertor.getWorkCriteriaDto(workCriteriaService.saveWorkCriteria(WorkCriteriaConvertor.getWorkCriteria(workCriteriaDto)));
    }

    @DeleteMapping("/{id}")
    public void deleteWorkStage(@PathVariable long id) {
        workCriteriaService.deleteWorkCriteria(id);
    }

    @GetMapping("")
    public List<WorkCriteriaDto> WorkStageList(@RequestParam Long workId) {
        return workCriteriaService.findWorkCriteria(workId).stream().map(WorkCriteriaConvertor::getWorkCriteriaDto).collect(Collectors.toList());
    }

}