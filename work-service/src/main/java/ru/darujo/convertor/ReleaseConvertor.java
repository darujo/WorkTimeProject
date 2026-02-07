package ru.darujo.convertor;

import ru.darujo.dto.work.ReleaseEditDto;
import ru.darujo.model.Release;

public class ReleaseConvertor {
    public static Release getRelease(Long projectId, ReleaseEditDto releaseEditDto) {
        Float sort = releaseEditDto.getSort();
        if (sort == null) {
            try {
                sort = Float.parseFloat(releaseEditDto.getName());
            } catch (NumberFormatException ignored) {
                sort = 999f;
            }

        }
        return new Release(releaseEditDto.getId(), releaseEditDto.getName(), releaseEditDto.getIssuingReleasePlan(), releaseEditDto.getIssuingReleaseFact(), sort, projectId);
    }

    public static ReleaseEditDto getReleaseDto(Release release) {
        return new ReleaseEditDto(release.getId(), release.getName(), release.getProjectId(), release.getIssuingReleasePlan(), release.getIssuingReleaseFact(), release.getSort());
    }
}
