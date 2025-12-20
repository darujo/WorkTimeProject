package ru.darujo.service;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.darujo.convertor.WorkConvertor;
import ru.darujo.dto.work.ReleaseStageDto;
import ru.darujo.model.Release;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

@Slf4j
@Service
public class ReleaseStageService {
    private WorkService workService;
    private ReleaseService releaseService;

    @Autowired
    public void setWorkService(WorkService workService) {
        this.workService = workService;
    }

    public PriorityQueue<@NonNull ReleaseStageDto> getReleaseStage(String name, String sort, Integer stageZiGe, Integer stageZiLe, Long codeSap, String codeZi, String task, List<Long> releaseIdList) {
        Map<Long, ReleaseStageDto> releaseStageDtoMap = new HashMap<>();
        if (releaseIdList != null && !releaseIdList.isEmpty()) {
            releaseService.findAll(releaseIdList).forEach(release -> releaseStageDtoMap.put(release.getId(), getReleaseStageDto(release)));
        }
        workService.findWorkLittle(null, null, name, sort, stageZiGe, stageZiLe, codeSap, codeZi, task, releaseIdList)
                .forEach(workLittle -> {
                    ReleaseStageDto releaseStageDto = releaseStageDtoMap.computeIfAbsent(
                            workLittle.getRelease() == null ? 0 : workLittle.getRelease().getId(),
                            k -> workLittle.getRelease() == null ?
                                    new ReleaseStageDto(0L,
                                            "Без релиза",
                                            null,
                                            null, -1F)
                                    : getReleaseStageDto(workLittle.getRelease())
                    );

                    releaseStageDto.getWorks()[workLittle.getStageZI()].add(WorkConvertor.getWorkLittleDto(workLittle));

                });
        PriorityQueue<ReleaseStageDto> releaseStageDTOs = new PriorityQueue<>();
        releaseStageDTOs.addAll(releaseStageDtoMap.values());
        return releaseStageDTOs;
    }

    public void changeStage(String login, Long workId, Long releaseId, Integer stageZI) {
        workService.setReleaseAndStageZi(login, workId, releaseId, stageZI);


    }

    private ReleaseStageDto getReleaseStageDto(Release release) {
        return new ReleaseStageDto(release.getId(),
                release.getName(),
                release.getIssuingReleasePlan(),
                release.getIssuingReleaseFact(),
                release.getSort());
    }

    @Autowired
    public void setReleaseService(ReleaseService releaseService) {
        this.releaseService = releaseService;
    }
}
