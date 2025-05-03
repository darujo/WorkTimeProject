package ru.darujo.convertor;

import ru.darujo.dto.work.ReleaseDto;
import ru.darujo.model.Release;

public class ReleaseConvertor {
    public static Release getRelease(ReleaseDto releaseDto){
        return new Release(releaseDto.getId(),releaseDto.getName(),releaseDto.getIssuingReleasePlan(),releaseDto.getIssuingReleaseFact());
    }
    public static ReleaseDto getReleaseDto(Release release){
        return new ReleaseDto(release.getId(),release.getName(),release.getIssuingReleasePlan(),release.getIssuingReleaseFact());
    }
}
