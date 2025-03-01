package ru.darujo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.darujo.convertor.WorkTimeConvertor;
import ru.darujo.dto.ListString;
import ru.darujo.dto.WorkTimeDto;
import ru.darujo.exceptions.ResourceNotFoundException;
import ru.darujo.model.WorkTime;
import ru.darujo.service.WorkTimeService;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController()
@RequestMapping("/v1/worktime")
public class WorkTimeController {
    private WorkTimeService workTimeService;
    @Autowired
    public void setWorkTimeService(WorkTimeService workTimeService) {
        this.workTimeService = workTimeService;
    }


    @GetMapping("/{id}")
    public WorkTimeDto WorkTimeEdit(@PathVariable long id) {
        return WorkTimeConvertor.getWorkTimeDto(workTimeService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Отмеченая работа не найден")));
    }

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    @PostMapping("")
    public WorkTimeDto WorkTimeSave( @RequestHeader String username,
                                     @RequestBody WorkTimeDto workTimeDto) {
        if (workTimeDto.getUserName() == null || !workTimeDto.getUserName().equals("") )
        {
            workTimeDto.setUserName(username);
        }
        return WorkTimeConvertor.getWorkTimeDto(workTimeService.saveWorkTime(WorkTimeConvertor.getWorkTime(workTimeDto)));
    }

    @DeleteMapping("/{id}")
    public void deleteWorkTime(@PathVariable long id) {
        workTimeService.deleteWorkTime(id);
    }

    @GetMapping("")
    public Page<WorkTimeDto> findWorkTime(@RequestParam(required = false) String dateLeStr,
                                          @RequestParam(required = false) String dateGtStr,
                                          @RequestParam(required = false) String dateGeStr,
                                          @RequestParam(required = false) Long taskId,
                                          @RequestParam(required = false) String userName,
                                          @RequestParam(defaultValue = "1")Integer page,
                                          @RequestParam(defaultValue = "10") Integer size) {
        Date dateLe = stringToDate(dateLeStr,"dateLe = ");
        Date dateGt = stringToDate(dateGtStr,"dateGt = ");
        Date dateGe = stringToDate(dateGeStr,"dateGe = ");
        return ((Page<WorkTime>) workTimeService.findWorkTime(taskId,
                                            userName,
                                            dateLe,
                                            dateGt,
                                            dateGe,
                                            page,
                                            size)).map(WorkTimeConvertor::getWorkTimeDto);
    }
    @GetMapping("/rep/fact/time")
    public Float getTimeWork(@RequestParam(required = false) Long taskId,
                             @RequestParam(required = false) String userName ,
                             @RequestParam(required = false, name = "dateLe") String dateLeStr ,
                             @RequestParam(required = false, name = "dateGt") String dateGtStr) {
       Date dateLe = stringToDate(dateLeStr,"dateLe = ");
       Date dateGt = stringToDate(dateGtStr,"dateGt = ");
       return workTimeService.getTimeWork(taskId,userName, dateLe, dateGt);
    }

    @GetMapping("/rep/fact/user")
    public ListString getFactUser(@RequestParam(required = false) Long taskId) {
        return workTimeService.getFactUser(taskId);
    }

    private Date stringToDate(String dateStr,String text){
        if (dateStr != null) {
            try {
                return new Timestamp(simpleDateFormat.parse(dateStr).getTime());
            } catch (ParseException e) {
                throw new ResourceNotFoundException("Не удалось распарсить дату " + text + " " + dateStr);
            }
        }
        return null;
    }

}