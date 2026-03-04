package ru.darujo.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.darujo.model.WorkTimeTypeDto;
import ru.darujo.service.WorkTimeTypeService;

import java.util.List;

@RestController()
@RequestMapping("/v1/code")
public class WorkTimeCodeController {
    @GetMapping("")
    public List<WorkTimeTypeDto> getList(@RequestParam("system_project") Long projectId){
        return WorkTimeTypeService.getTypeDtoList(projectId, null);
    }
}
