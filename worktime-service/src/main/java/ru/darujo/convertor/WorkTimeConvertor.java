package ru.darujo.convertor;

import ru.darujo.dto.WorkTimeDto;
import ru.darujo.model.WorkTime;

public class WorkTimeConvertor {

    public static WorkTimeDto getWorkTimeDto(WorkTime workTime){
        return WorkTimeBuilder
                .createWorkTime()
                .setId(workTime.getId())
                .setTaskId(workTime.getTaskId ())
                .setWorkDate(workTime.getWorkDate())
                .setNikName(workTime.getNikName())
                .setWorkTime(workTime.getWorkTime())
                .setComment(workTime.getComment())
                .setType(workTime.getType())
                .setProjectId(workTime.getProjectId())
                .getWorkTimeDto();
    }
    public static WorkTime getWorkTime(WorkTimeDto workTimeDto){
        return WorkTimeBuilder
                .createWorkTime()
                .setId(workTimeDto.getId())
                .setTaskId(workTimeDto.getTaskId())
                .setWorkDate(workTimeDto.getWorkDate())
                .setNikName(workTimeDto.getNikName())
                .setWorkTime(workTimeDto.getWorkTime())
                .setComment(workTimeDto.getComment())
                .setType(workTimeDto.getType())
                .setProjectId(workTimeDto.getProjectId())
                .getWorkTime();
    }
}
