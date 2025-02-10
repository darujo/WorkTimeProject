package ru.darujo.convertor;

import ru.darujo.dto.WorkTimeDto;
import ru.darujo.model.WorkTime;

public class WorkTimeConvertor {

    public static WorkTimeDto getWorkTimeDto(WorkTime workTime){
        return WorkTimeBuilder
                .createWorkTime()
                .setId(workTime.getId())
                .setWorkId(workTime.getWork ().getId())
                .setWorkDate(workTime.getWorkDate())
                .setUserName(workTime.getUserName())
                .setWorkTime(workTime.getWorkTime())
                .getWorkTimeDto();
    }
    public static WorkTime getWorkTime(WorkTimeDto workTimeDto){
        return WorkTimeBuilder
                .createWorkTime()
                .setId(workTimeDto.getId())
                .setWorkId(workTimeDto.getWorkId())
                .setWorkDate(workTimeDto.getWorkDate())
                .setUserName(workTimeDto.getUserName())
                .setWorkTime(workTimeDto.getWorkTime())
                .getWorkTime();
    }
}
