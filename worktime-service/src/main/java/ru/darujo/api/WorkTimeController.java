package ru.darujo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.darujo.convertor.WorkTimeConvertor;
import ru.darujo.dto.WorkTimeDto;
import ru.darujo.dto.parsing.DateParser;
import ru.darujo.exceptions.ResourceNotFoundException;
import ru.darujo.model.WorkTime;
import ru.darujo.service.WorkTimeService;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("/v1/worktime")
public class WorkTimeController extends DateParser {
    private WorkTimeService workTimeService;

    @Autowired
    public void setWorkTimeService(WorkTimeService workTimeService) {
        this.workTimeService = workTimeService;
    }

    @GetMapping("/conv")
    public WorkTimeDto workConv() {
        workTimeService.findWorkTime(null, null, null, null, null, null, null, null,null,null).forEach(workTime -> workTimeService.saveWorkTime(WorkTimeConvertor.getWorkTime(WorkTimeConvertor.getWorkTimeDto(workTime)), false));
        return new WorkTimeDto();
    }

    @GetMapping("/{id}")
    public WorkTimeDto WorkTimeEdit(@PathVariable long id) {
        return WorkTimeConvertor.getWorkTimeDto(workTimeService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Отмеченая работа не найден")));
    }

    @GetMapping("/right/{right}")
    public boolean checkRight(@PathVariable String right,
                              @RequestHeader(defaultValue = "false", name = "WORK_TIME_EDIT") boolean rightEdit,
                              @RequestHeader(defaultValue = "false", name = "WORK_TIME_CREATE") boolean rightCreate,
                              @RequestHeader(defaultValue = "false", name = "WORK_TIME_CHANGE_USER") boolean rightChangeUser) {
       return workTimeService.checkRight(right,rightEdit, rightCreate, rightChangeUser);


    }

    @PostMapping("")
    public WorkTimeDto WorkTimeSave(@RequestHeader String username,
                                    @RequestBody WorkTimeDto workTimeDto,
                                    @RequestHeader(defaultValue = "false", name = "WORK_TIME_EDIT") boolean right) {
        if (!right) {
            throw new ResourceNotFoundException("У вас нет права WORK_TIME_EDIT");
        }
        if (workTimeDto.getNikName() == null || workTimeDto.getNikName().equals("")) {
            workTimeDto.setNikName(username);
        }
        return WorkTimeConvertor.getWorkTimeDto(workTimeService.saveWorkTime(WorkTimeConvertor.getWorkTime(workTimeDto)));
    }

    @DeleteMapping("/{id}")
    public void deleteWorkTime(@PathVariable long id) {
        workTimeService.deleteWorkTime(id);
    }

    @GetMapping("")
    public Iterable<WorkTimeDto> findWorkTime(@RequestHeader String username,
                                              @RequestParam(required = false, name = "dateLt") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateLtStr,
                                              @RequestParam(required = false, name = "dateLe") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateLeStr,
                                              @RequestParam(required = false, name = "dateGt") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateGtStr,
                                              @RequestParam(required = false, name = "dateGe") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateGeStr,
                                              @RequestParam(required = false) Long taskId,
                                              @RequestParam(required = false) String taskDEVBO,
                                              @RequestParam(required = false) String taskBTS,
                                              @RequestParam(required = false) String nikName,
                                              @RequestParam(required = false) Integer type,
                                              @RequestParam(required = false) String comment,
                                              @RequestParam(defaultValue = "false") boolean currentUser,
                                              @RequestParam(defaultValue = "1") Integer page,
                                              @RequestParam(defaultValue = "10") Integer size) {
        Date dateLt = stringToDate(dateLtStr, "dateLt = ");
        Date dateLe = stringToDate(dateLeStr, "dateLe = ", false);
        Date dateGt = stringToDate(dateGtStr, "dateGt = ", false);
        Date dateGe = stringToDate(dateGeStr, "dateGe = ");
        if ((nikName == null || nikName.equals(""))  && currentUser) {
            nikName = username;
        }

        workTimeService.clearCash();
        if ((taskBTS == null && taskDEVBO == null) || taskId != null) {
            return ((Page<WorkTime>) workTimeService.findWorkTime(taskId,
                    nikName,
                    dateLt,
                    dateLe,
                    dateGt,
                    dateGe,
                    type,
                    comment,
                    page,
                    size)).map(workTimeService::getWorkTimeDtoAndUpd);
        } else {
            List<WorkTime> workTimeDTOs = workTimeService.findWorkTimeTask(
                    taskDEVBO,
                    taskBTS,
                    nikName,
                    dateLt,
                    dateLe,
                    dateGt,
                    dateGe,
                    type,
                    comment);
            return workTimeDTOs.stream().map(workTimeService::getWorkTimeDtoAndUpd).collect(Collectors.toList());

        }
    }

}