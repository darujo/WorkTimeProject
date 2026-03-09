package ru.darujo.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.darujo.convertor.WorkConvertor;
import ru.darujo.dto.work.ReleaseStageDto;
import ru.darujo.model.Release;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReleaseStageService {
    private WorkService workService;
    private ReleaseService releaseService;

    @Autowired
    public void setWorkService(WorkService workService) {
        this.workService = workService;
    }

    @Transactional
    public List<@NonNull ReleaseStageDto> getReleaseStage(String name, String sort, Integer stageZiGe, Integer stageZiLe, Long codeSap, String codeZi, String task, List<Long> releaseIdList, Long projectId) {
        Map<Long, ReleaseStageDto> releaseStageDtoMap = new LinkedHashMap<>();
        if (releaseIdList != null && !releaseIdList.isEmpty()) {
            releaseService.findAll(releaseIdList, null).forEach(releaseFull -> releaseStageDtoMap.put(releaseFull.getRelease().getId(), getReleaseStageDto(releaseFull.getRelease())));
        }
        workService.findWorkLittle(null, null, name, sort, stageZiGe, stageZiLe, codeSap, codeZi, task, releaseIdList, projectId)
                .forEach(workLittleFull -> {
                    ReleaseStageDto releaseStageDto = releaseStageDtoMap.computeIfAbsent(
                            workLittleFull.getWork().getRelease() == null ? 0 : workLittleFull.getWork().getRelease().getId(),
                            k -> workLittleFull.getWork().getRelease() == null ?
                                    new ReleaseStageDto(0L,
                                            "Без релиза",
                                            -1F)
                                    : getReleaseStageDto(workLittleFull.getWork().getRelease())
                    );

                    releaseStageDto.getWorks()[workLittleFull.getWorkProject().getStageZi()].add(WorkConvertor.getWorkLittleDto(workLittleFull));

                });
        return releaseStageDtoMap.values().stream().sorted().collect(Collectors.toList());
    }

    public void changeStage(String login, Long workId, Long projectId, Long releaseId, Integer stageZI) {
        workService.setReleaseAndStageZi(login, workId, projectId, releaseId, stageZI);


    }

    private ReleaseStageDto getReleaseStageDto(Release release) {
        return new ReleaseStageDto(release.getId(),
                release.getName(),
                release.getSort());
    }

    @Autowired
    public void setReleaseService(ReleaseService releaseService) {
        this.releaseService = releaseService;
    }
}
