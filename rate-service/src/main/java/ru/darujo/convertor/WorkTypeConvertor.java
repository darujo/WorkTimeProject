package ru.darujo.convertor;

import ru.darujo.dto.ratestage.WorkTypeDto;
import ru.darujo.model.WorkType;

public class WorkTypeConvertor {
    public static WorkTypeDto getWorkTypeDto(WorkType workType) {
        return WorkTypeBuilder
                .createWorkType()
                .setId(workType.getId())
                .setType(workType.getType())
                .setTime(workType.getTime())
                .setWorkId(workType.getWorkId())
                .setNumber(workType.getNumber())
                .getWorkTypeDto();
    }

    public static WorkType getWorkType(WorkTypeDto workTypeDto) {
        return WorkTypeBuilder.createWorkType()
                .setId(workTypeDto.getId())
                .setType(workTypeDto.getType())
                .setTime(workTypeDto.getTime())
                .setWorkId(workTypeDto.getWorkId())
                .setNumber(workTypeDto.getNumber())
                .getWorkType();
    }
}
