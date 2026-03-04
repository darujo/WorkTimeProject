package ru.darujo.api;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.darujo.assistant.helper.DateHelper;
import ru.darujo.dto.work.ReleaseStageDto;
import ru.darujo.service.ReleaseStageService;

import java.util.List;

@Slf4j
@RestController()
@RequestMapping("/v1/release/stage")
public class ReleaseStageController {
    private ReleaseStageService releaseStageService;

    @Autowired
    public void setReleaseStageService(ReleaseStageService releaseStageService) {
        this.releaseStageService = releaseStageService;
    }

    @GetMapping("")
    public List<@NonNull ReleaseStageDto> ReleaseList(@RequestParam(required = false) String name,
                                                      @RequestParam(defaultValue = "15") Integer stageZi,
                                                      @RequestParam(required = false) Long codeSap,
                                                      @RequestParam(required = false) String codeZi,
                                                      @RequestParam(required = false) String task,
                                                      @RequestParam(name = "system_project", required = false) Long projectId,
                                                      @RequestParam(required = false) List<String> releaseId,
                                                      @RequestParam(defaultValue = "release.name") String sort) {
        Integer stageZiGe = null;
        Integer stageZiLe = null;
        if (stageZi != null) {
            if (stageZi < 10) {
                stageZiGe = stageZi;
                stageZiLe = stageZi;
            } else {
                stageZiLe = stageZi - 10;
            }
        }
        long curTime = System.nanoTime();
        List<@NonNull ReleaseStageDto> workDTOs = releaseStageService.getReleaseStage(name, sort, stageZiGe, stageZiLe, codeSap, codeZi, task, DateHelper.convertListToLong(releaseId), projectId);
        float time_last = (curTime - System.nanoTime()) * 0.000000001f;
        log.info("Время выполнения WorkPage {}", time_last);
        return workDTOs;
    }

    @GetMapping("/save")
    public void changeWork(@RequestHeader String username,
                           @RequestParam Long workId,
                           @RequestParam(required = false) Long releaseId,
                           @RequestParam(name = "system_project", required = false) Long projectId,
                           @RequestParam Integer stageZi
    ) {
        releaseStageService.changeStage(username, workId, projectId, releaseId, stageZi);
    }
}
