package ru.darujo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.darujo.convertor.WorkStageConvertor;
import ru.darujo.dto.ratestage.WorkStageDto;
import ru.darujo.exceptions.ResourceNotFoundException;
import ru.darujo.service.WorkStageService;

import java.util.ArrayList;
import java.util.List;

@RestController()
@RequestMapping("/v1/stage")
public class WorkStageController {
    private WorkStageService workStageService;

    @Autowired
    public void setWorkStage(WorkStageService workStageService) {
        this.workStageService = workStageService;
    }


    @GetMapping("/{id}")
    public WorkStageDto WorkStageEdit(@PathVariable long id) {
        return WorkStageConvertor.getWorkStageDto(workStageService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Не найдена разбивка")));
    }

    @PostMapping("")
    public WorkStageDto WorkStageSave(@RequestBody WorkStageDto workStageDto) {
        return WorkStageConvertor.getWorkStageDto(workStageService.saveWorkStage(WorkStageConvertor.getWorkStage(workStageDto)));
    }

    @DeleteMapping("/{id}")
    public void deleteWorkStage(@PathVariable long id) {
        workStageService.deleteWorkStage(id);
    }

    @GetMapping("")
    public List<WorkStageDto> WorkStageList(@RequestParam Long workId,
                                            @RequestParam(defaultValue = "false") boolean loadFact) {
        List<WorkStageDto> workStageDTOs = new ArrayList<>();
        workStageService.findWorkStage(workId).forEach(workStage -> workStageDTOs.add(WorkStageConvertor.getWorkStageDto(workStage)));
        workStageDTOs.forEach(workStageDto ->
                workStageService.updFio(workStageDto)

        );
        if (loadFact) {
            workStageService.updWorkStage(workId, workStageDTOs);
        }
        return workStageDTOs;
    }

}