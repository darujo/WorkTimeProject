package ru.darujo.convertor;

import ru.darujo.assistant.helper.DateHelper;
import ru.darujo.dto.work.ReleaseEditDto;
import ru.darujo.model.Release;
import ru.darujo.model.ReleaseFull;
import ru.darujo.model.ReleaseProject;

public class ReleaseConvertor {
    public static ReleaseProject getRelease(Long projectId, ReleaseEditDto releaseEditDto) {
        Float sort = releaseEditDto.getSort();
        if (sort == null) {
            try {
                sort = Float.parseFloat(releaseEditDto.getName());
            } catch (NumberFormatException ignored) {
                sort = 999f;
            }

        }
        Release release = new Release(
                releaseEditDto.getId(),
                releaseEditDto.getName(),
                DateHelper.zDTToLD(releaseEditDto.getIssuingReleasePlan()),
                sort);
        return new ReleaseProject(null, DateHelper.zDTToLD(releaseEditDto.getIssuingReleaseFact()), release, projectId);
    }

    public static ReleaseEditDto getReleaseDto(ReleaseFull releaseFull) {
        Release release = releaseFull.getRelease();
        ReleaseProject releaseProject = releaseFull.getReleaseProject();
        return new ReleaseEditDto(
                release.getId(),
                release.getName(),
                releaseProject == null ? null : releaseProject.getProjectId(),
                DateHelper.getZDT(release.getIssuingReleasePlan()),
                releaseProject == null ? null : DateHelper.getZDT(releaseProject.getIssuingReleaseFact()),
                release.getSort());
    }
}
