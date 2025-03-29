package ru.darujo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.darujo.dto.PageDto;
import ru.darujo.dto.workrep.WorkFactDto;
import ru.darujo.dto.workrep.WorkRepDto;
import ru.darujo.service.WorkRepService;

import java.util.List;

@RestController()
@RequestMapping("/v1/works/rep")
public class WorkRepController {
    private WorkRepService workRepService;

    @Autowired
    public void setWorkRepService(WorkRepService workRepService) {
        this.workRepService = workRepService;
    }


    @GetMapping("")
    public List<WorkRepDto> getTimeWork(@RequestParam(required = false) String ziName,
                                        @RequestParam(required = false) Boolean availWork,
                                        @RequestParam(defaultValue = "15") Integer stageZi,
                                        @RequestParam(required = false) String release,
                                        @RequestParam(required = false) String[] sort) {
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
        return workRepService.getWorkRep(ziName, availWork, stageZiGe, stageZiLe, release, sort);
    }

    @GetMapping("/factwork")
    public PageDto<WorkFactDto> getFactWork(@RequestParam(defaultValue = "1") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestParam(required = false) String userName,
                                            @RequestParam(required = false) String name,
                                            @RequestParam(defaultValue = "15") Integer stageZi,
                                            @RequestParam(required = false) Long codeSap,
                                            @RequestParam(required = false) String codeZi,
                                            @RequestParam(required = false) String task,
                                            @RequestParam(required = false) String release,
                                            @RequestParam(defaultValue = "release") String sort,
                                            @RequestParam(defaultValue = "true") boolean hideNotTime) {
        if (userName != null && userName.equals("")) {
            userName = null;
        }
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
        return workRepService.getWorkFactRep(page, size, userName, name, stageZiGe, stageZiLe, codeSap, codeZi, task, release, sort, hideNotTime);
    }

}
