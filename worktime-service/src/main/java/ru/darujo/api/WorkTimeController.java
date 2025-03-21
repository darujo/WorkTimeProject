package ru.darujo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.darujo.convertor.WorkTimeConvertor;
import ru.darujo.dto.ListString;
import ru.darujo.dto.UserWorkDto;
import ru.darujo.dto.WorkTimeDto;
import ru.darujo.exceptions.ResourceNotFoundException;
import ru.darujo.model.WorkTime;
import ru.darujo.service.WorkTimeService;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
        workTimeService.findWorkTime(null, null, null, null, null, null, null, null).forEach(workTime -> workTimeService.saveWorkTime(WorkTimeConvertor.getWorkTime(WorkTimeConvertor.getWorkTimeDto(workTime)), false));
        return new WorkTimeDto();
    }

    @GetMapping("/{id}")
    public WorkTimeDto WorkTimeEdit(@PathVariable long id) {
        return WorkTimeConvertor.getWorkTimeDto(workTimeService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Отмеченая работа не найден")));
    }
    @GetMapping("/right/{right}")
    public boolean checkRight (@PathVariable String right,
                               @RequestHeader(defaultValue = "false", name = "WORK_TIME_EDIT") boolean rightEdit,
                               @RequestHeader(defaultValue = "false", name = "WORK_TIME_CREATE") boolean rightCreate){
        right = right.toLowerCase();
        if( right.equals("edit")){
            if(!rightEdit) {
                throw new ResourceNotFoundException("У вас нет права на редактирование WORK_TIME_EDIT");
            }
        }
        else if( right.equals("create")){
            if(!rightCreate) {
                throw new ResourceNotFoundException("У вас нет права на редактирование WORK_TIME_CREATE");
            }
        }
        return true;

    }
    @PostMapping("")
    public WorkTimeDto WorkTimeSave(@RequestHeader String username,
                                    @RequestBody WorkTimeDto workTimeDto,
                                    @RequestHeader(defaultValue = "false", name = "WORK_TIME_EDIT") boolean right) {
        if (!right) {
            throw new ResourceNotFoundException("У вас нет права WORK_TIME_EDIT");
        }
        if (workTimeDto.getNikName() == null || !workTimeDto.getNikName().equals("")) {
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
                                              @RequestParam(required = false) String taskDevbo,
                                              @RequestParam(required = false) String taskBts,
                                              @RequestParam(required = false) String nikName,
                                              @RequestParam(defaultValue = "false") boolean currentUser,
                                              @RequestParam(defaultValue = "1") Integer page,
                                              @RequestParam(defaultValue = "10") Integer size) {
        Date dateLt = stringToDate(dateLtStr, "dateLt = ");
        Date dateLe = stringToDate(dateLeStr, "dateLe = ", false);
        Date dateGt = stringToDate(dateGtStr, "dateGt = ", false);
        Date dateGe = stringToDate(dateGeStr, "dateGe = ");
        if (nikName == null && currentUser){
            nikName = username;
        }
        workTimeService.clearCash();
        if ((taskBts == null && taskDevbo == null) || taskId != null) {
            return ((Page<WorkTime>) workTimeService.findWorkTime(taskId,
                    nikName,
                    dateLt,
                    dateLe,
                    dateGt,
                    dateGe,
                    page,
                    size)).map(workTimeService::getWorkTimeDtoAndUpd);
        } else {
            List<WorkTime> workTimeDtos = workTimeService.findWorkTimeTask(
                    taskDevbo,
                    taskBts,
                    nikName,
                    dateLt,
                    dateLe,
                    dateGt,
                    dateGe);
            return workTimeDtos.stream().map(workTimeService::getWorkTimeDtoAndUpd).collect(Collectors.toList());

        }
    }

    @GetMapping("/rep/fact/time")
    public Float getTimeWork(@RequestParam(required = false) Long taskId,
                             @RequestParam(required = false) String nikName,
                             @RequestParam(required = false, name = "dateLe") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateLeStr,
                             @RequestParam(required = false, name = "dateGt") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateGtStr) {
        Date dateLe = stringToDate(dateLeStr, "dateLe = ", false);
        Date dateGt = stringToDate(dateGtStr, "dateGt = ", false);
        if (dateLe == null && dateGt == null) {
            return 0f;
        }
        return workTimeService.getTimeWork(taskId, nikName, dateGt, dateLe);
    }

    @GetMapping("/rep/fact/user")
    public ListString getFactUser(@RequestParam(required = false) Long taskId) {
        return workTimeService.getFactUser(taskId);
    }

    @GetMapping("/rep/fact/week")
    public List<UserWorkDto> getWeekWork(@RequestParam(required = false) String nikName,
                                         @RequestParam(defaultValue = "true") boolean weekSplit,
                                         @RequestParam(required = false, name = "dateStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateStartStr,
                                         @RequestParam(required = false, name = "dateEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateEndStr) {
        Timestamp dateStart = stringToDate(dateStartStr, "dateStart = ", true);
        Timestamp dateEnd = stringToDate(dateEndStr, "dateEnd = ", true);
        return workTimeService.getWeekWork(nikName, weekSplit, dateStart, dateEnd);
    }


    private Timestamp stringToDate(ZonedDateTime dateStr, String text) {
        return stringToDate(dateStr, text, false);
    }


    private Timestamp stringToDate(ZonedDateTime dateStr, String text, boolean checkNull) {
        if (dateStr != null) {

            Calendar c = Calendar.getInstance();
            c.setTime(Timestamp.from(dateStr.toInstant()));
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            return new Timestamp(c.getTimeInMillis());

        } else if (checkNull) {
            throw new ResourceNotFoundException("Не не передан обязательный параметр " + text + " null ");
        }
        return null;
    }
    @GetMapping("/rep/fact/availTime/{taskId}")
    public Boolean getFactUsers(@PathVariable long taskId
    ) {
        return workTimeService.getAvailTime(taskId);
    }

}