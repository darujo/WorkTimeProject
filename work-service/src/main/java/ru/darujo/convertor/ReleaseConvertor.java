package ru.darujo.convertor;

import ru.darujo.dto.work.ReleaseDto;
import ru.darujo.model.Release;

public class ReleaseConvertor {
    public static Release getRelease(Long projectId, ReleaseDto releaseDto) {
        Float sort = releaseDto.getSort();
        if (sort == null) {
            try {
                sort = Float.parseFloat(releaseDto.getName());
            } catch (NumberFormatException ignored) {
                sort = 999f;
            }

        }
        return new Release(releaseDto.getId(), releaseDto.getName(), releaseDto.getIssuingReleasePlan(), releaseDto.getIssuingReleaseFact(), sort, projectId);
    }
    public static ReleaseDto getReleaseDto(Release release){
        return new ReleaseDto(release.getId(), release.getName(), release.getIssuingReleasePlan(), release.getIssuingReleaseFact(), release.getSort());
    }
}
