package ru.darujo.api;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.darujo.assistant.helper.DateHelper;
import ru.darujo.convertor.WorkTimeConvertor;
import ru.darujo.dto.WorkTimeDto;
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.model.WorkTime;
import ru.darujo.service.WorkTimeService;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@RestController()
@RequestMapping("/v1/worktime")
public class WorkTimeController {
    private WorkTimeService workTimeService;

    @Autowired
    public void setWorkTimeService(WorkTimeService workTimeService) {
        this.workTimeService = workTimeService;
    }

    @GetMapping("/conv")
    public WorkTimeDto workConv() {
        workTimeService.findWorkTime(null, null, null, null, null, null, null, null, null, null, null).forEach(workTime -> workTimeService.saveWorkTime(WorkTimeConvertor.getWorkTime(WorkTimeConvertor.getWorkTimeDto(workTime)), false));
        return new WorkTimeDto();
    }

    @GetMapping("/{id}")
    public WorkTimeDto workTimeEdit(@PathVariable long id) {
        return WorkTimeConvertor.getWorkTimeDto(workTimeService.findById(id).orElseThrow(() -> new ResourceNotFoundRunTime("Отмеченная работа не найден")));
    }

    @GetMapping("/right/{right}")
    public boolean checkRight(@PathVariable String right,
                              @RequestParam(name = "system_right", required = false) List<String> userRight) {
        return workTimeService.checkRight(right, userRight);


    }

    @PostMapping("")
    public WorkTimeDto workTimeSave(@RequestHeader String username,
                                    @RequestBody WorkTimeDto workTimeDto,
                                    @RequestParam("system_right") List<String> userRight,
                                    @RequestParam("system_project") Long projectId) {
        workTimeService.checkRight(workTimeDto.getId() == null ? "create" : "edit", userRight);
        if (workTimeDto.getNikName() == null || workTimeDto.getNikName().isEmpty()) {
            workTimeDto.setNikName(username);
        }
        workTimeDto.setProjectId(projectId);
        return WorkTimeConvertor.getWorkTimeDto(workTimeService.saveWorkTime(WorkTimeConvertor.getWorkTime(workTimeDto)));
    }

    @DeleteMapping("/{id}")
    public void deleteWorkTime(@PathVariable long id,
                               @RequestParam("system_right") List<String> userRight) {
        workTimeService.checkRight("delete", userRight);
        workTimeService.deleteWorkTime(id);
    }

    @GetMapping("")
    public Page<@NonNull WorkTimeDto> findWorkTime(@RequestHeader String username,
                                                   @RequestParam(required = false, name = "dateLt") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateLtStr,
                                                   @RequestParam(required = false, name = "dateLe") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateLeStr,
                                                   @RequestParam(required = false, name = "dateGt") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateGtStr,
                                                   @RequestParam(required = false, name = "dateGe") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateGeStr,
                                                   @RequestParam(required = false) Long[] taskId,
                                                   @RequestParam(required = false) String taskDEVBO,
                                                   @RequestParam(required = false) String taskBTS,
                                                   @RequestParam(required = false) String nikName,
                                                   @RequestParam(required = false) Integer type,
                                                   @RequestParam(required = false) String comment,
                                                   @RequestParam("system_project") Long projectId,
                                                   @RequestParam(defaultValue = "false") boolean currentUser,
                                                   @RequestParam(defaultValue = "1") Integer page,
                                                   @RequestParam(defaultValue = "10") Integer size) {
        Date dateLt = DateHelper.DTZToDate(dateLtStr, "dateLt = ");
        Date dateLe = DateHelper.DTZToDate(dateLeStr, "dateLe = ", false);
        Date dateGt = DateHelper.DTZToDate(dateGtStr, "dateGt = ", false);
        Date dateGe = DateHelper.DTZToDate(dateGeStr, "dateGe = ");
        if ((nikName == null || nikName.isEmpty()) && currentUser) {
            nikName = username;
        }

        workTimeService.clearCash();
        if ((taskBTS == null && taskDEVBO == null) || taskId != null) {
            return workTimeService.findWorkTime(taskId,
                    nikName,
                    dateLt,
                    dateLe,
                    dateGt,
                    dateGe,
                    type,
                    comment,
                    projectId,
                    page,
                    size).map(workTimeService::getWorkTimeDtoAndUpd);
        } else {
            Page<@NonNull WorkTime> workTimeDTOs = workTimeService.findWorkTimeTask(
                    taskDEVBO,
                    taskBTS,
                    nikName,
                    dateLt,
                    dateLe,
                    dateGt,
                    dateGe,
                    type,
                    comment,
                    projectId,
                    page,
                    size);
            return workTimeDTOs.map(workTimeService::getWorkTimeDtoAndUpd);

        }
    }

}