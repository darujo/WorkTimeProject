package ru.darujo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.darujo.convertor.WorkTimeConvertor;
import ru.darujo.dto.WorkTimeDto;
import ru.darujo.exceptions.ResourceNotFoundException;
import ru.darujo.service.WorkTimeService;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

    @PutMapping("")
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

    @GetMapping("/qq ")
    public Page<WorkTimeDto> findWorkTime(@RequestParam(required = false) Long workId,
                                          @RequestParam(required = false) Date dateLe,
                                          @RequestParam(required = false) Date dateGe,
                                          @RequestParam(defaultValue = "1") int page,
                                          @RequestParam(defaultValue = "10") int size) {
         return workTimeService.findWorkTime(workId,null,dateLe, null,dateGe, page, size).map(WorkTimeConvertor::getWorkTimeDto);
    }
    @GetMapping("")
    public Page<WorkTimeDto> findWorkTime(@RequestParam(required = false) String dateLeStr,
                                          @RequestParam(required = false) String dateGtStr,
                                          @RequestParam(required = false) String dateGeStr,
                                          @RequestParam(required = false) Long workId,
                                          @RequestParam(required = false) String userName,
                                          @RequestParam(defaultValue = "1")Integer page,
                                          @RequestParam(defaultValue = "10") Integer size) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date dateLe = null;
        Date dateGt = null;
        Date dateGe = null;
        if (dateLeStr != null) {
            try {
                dateLe = simpleDateFormat.parse(dateLeStr);
            } catch (ParseException e) {
                throw new ResourceNotFoundException("Не удалось распарсить дату dateLe = " + dateLeStr);
            }
        }
        if (dateGtStr != null) {
            try {
                dateGt = simpleDateFormat.parse(dateGtStr);
            } catch (ParseException e) {
                throw new ResourceNotFoundException("Не удалось распарсить дату dateGt = " + dateGtStr);
            }
        }
        if (dateGeStr != null) {
            try {
                dateGe = simpleDateFormat.parse(dateLeStr);
            } catch (ParseException e) {
                throw new ResourceNotFoundException("Не удалось распарсить дату dateGe = " + dateGeStr);
            }
        }
        return workTimeService.findWorkTime(workId,
                                            userName,
                                            dateLe,
                                            dateGt,
                                            dateGe,
                                            page,
                                            size).map(WorkTimeConvertor::getWorkTimeDto);
    }
    @GetMapping("/rep/time")
    public float getTimeWork(@RequestParam(required = false) Long workId,
                             @RequestParam(required = false) String userName ,
                             @RequestParam(required = false, name = "dateLe") String dateLeStr ,
                             @RequestParam(required = false, name = "dateGt") String dateGtStr) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date dateLe = null;
        Date dateGt = null;
        if (dateLeStr != null) {
            try {
                dateLe = simpleDateFormat.parse(dateLeStr);
            } catch (ParseException e) {
                throw new ResourceNotFoundException("Не удалось распарсить дату dateLe = " + dateLeStr);
            }
        }
        if (dateGtStr != null) {
            try {
                dateGt = simpleDateFormat.parse(dateGtStr);
            } catch (ParseException e) {
                throw new ResourceNotFoundException("Не удалось распарсить дату dateGt = " + dateGtStr);
            }
        }

        return workTimeService.getTimeWork(workId,userName, dateLe, dateGt);
    }

}