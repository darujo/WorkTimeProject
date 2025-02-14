package ru.darujo.convertor;

import ru.darujo.dto.WorkDto;
import ru.darujo.model.Work;

public class WorkConvertor {

    public static WorkDto getWorkDto(Work work){
        return WorkBuilder
                .createWork()
                .setId(work.getId())
                .setDateStartDebug(work.getDateStartDebug())
                .setDateStartDevelop(work.getDateStartDevelop())
                .setDateStartOPE(work.getDateStartOPE())
                .setDateStartRelease(work.getDateStartRelease())
                .setDateStartWender(work.getDateStartWender())
                .setName(work.getName())
                .getWorkDto();
    }
    public static Work getWork(WorkDto workDto){
        return WorkBuilder
                .createWork()
                .setId(workDto.getId())
                .setDateStartWender(workDto.getDateStartWender())
                .setDateStartRelease(workDto.getDateStartRelease())
                .setDateStartOPE(workDto.getDateStartOPE())
                .setDateStartDevelop(workDto.getDateStartDevelop())
                .setDateStartDebug(workDto.getDateStartDebug())
                .setName(workDto.getName())
                .getWork();
    }
}
