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
                .setUserName(workTime.getUserName())
                .setWorkTime(workTime.getWorkTime())
                .setComment(workTime.getComment())
                .getWorkTimeDto();
    }
    public static WorkTime getWorkTime(WorkTimeDto workTimeDto){
        return WorkTimeBuilder
                .createWorkTime()
                .setId(workTimeDto.getId())
                .setTaskId(workTimeDto.getTaskId())
                .setWorkDate(workTimeDto.getWorkDate())
                .setUserName(workTimeDto.getUserName())
                .setWorkTime(workTimeDto.getWorkTime())
                .setComment(workTimeDto.getComment())
                .getWorkTime();
    }
}
