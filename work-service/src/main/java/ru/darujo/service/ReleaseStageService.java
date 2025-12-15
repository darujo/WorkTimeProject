package ru.darujo.service;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.darujo.convertor.WorkConvertor;
import ru.darujo.dto.work.ReleaseStageDto;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

@Slf4j
@Service
public class ReleaseStageService {
    private WorkService workService;

    @Autowired
    public void setWorkService(WorkService workService) {
        this.workService = workService;
    }

    public PriorityQueue<@NonNull ReleaseStageDto> getReleaseStage(String name, String sort, Integer stageZiGe, Integer stageZiLe, Long codeSap, String codeZi, String task, Long releaseId) {
        Map<Long, ReleaseStageDto> releaseStageDtoMap = new HashMap<>();
        workService.findWorkLittle(null, null, name, sort, stageZiGe, stageZiLe, codeSap, codeZi, task, releaseId)
                .forEach(workLittle -> {
                    ReleaseStageDto releaseStageDto = releaseStageDtoMap.computeIfAbsent(
                            workLittle.getRelease() == null ? 0 : workLittle.getRelease().getId(),
                            k -> workLittle.getRelease() == null ?
                                    new ReleaseStageDto(0L,
                                            "Без релиза",
                                            null,
                                            null, -1F)
                                    :
                                    new ReleaseStageDto(workLittle.getRelease().getId(),
                                            workLittle.getRelease().getName(),
                                            workLittle.getRelease().getIssuingReleasePlan(),
                                            workLittle.getRelease().getIssuingReleaseFact(),
                                            workLittle.getRelease().getSort())
                    );

                    releaseStageDto.getWorks()[workLittle.getStageZI()].add(WorkConvertor.getWorkLittleDto(workLittle));

                });
        PriorityQueue<ReleaseStageDto> releaseStageDTOs = new PriorityQueue<>(Comparator.comparing(ReleaseStageDto::getSort).thenComparing(ReleaseStageDto::getName));
        releaseStageDTOs.addAll(releaseStageDtoMap.values());
        return releaseStageDTOs;
    }

    public void changeStage(String login, Long workId, Long releaseId, Integer stageZI) {
        workService.setReleaseAndStageZi(login, workId, releaseId, stageZI);


    }

}
