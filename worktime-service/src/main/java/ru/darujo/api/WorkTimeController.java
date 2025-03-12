package ru.darujo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.darujo.convertor.WorkTimeConvertor;
import ru.darujo.dto.ListString;
import ru.darujo.dto.UserWorkDto;
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
        if (workTimeDto.getNikName() == null || !workTimeDto.getNikName().equals("") )
        {
            workTimeDto.setNikName(username);
        }
        return WorkTimeConvertor.getWorkTimeDto(workTimeService.saveWorkTime(WorkTimeConvertor.getWorkTime(workTimeDto)));
    }

    @DeleteMapping("/{id}")
    public void deleteWorkTime(@PathVariable long id) {
        workTimeService.deleteWorkTime(id);
    }

    @GetMapping("")
    public Page<WorkTimeDto> findWorkTime(@RequestParam(required = false, name = "dateLt") String dateLtStr,
                                          @RequestParam(required = false, name = "dateLe") String dateLeStr,
                                          @RequestParam(required = false, name = "dateGt") String dateGtStr,
                                          @RequestParam(required = false, name = "dateGe") String dateGeStr,
                                          @RequestParam(required = false) Long taskId,
                                          @RequestParam(required = false) String nikName,
                                          @RequestParam(defaultValue = "1")Integer page,
                                          @RequestParam(defaultValue = "10") Integer size) {
        Date dateLt = stringToDate(dateLtStr,"dateLt = ");
        Date dateLe = stringToDate(dateLeStr,"dateLe = ");
        Date dateGt = stringToDate(dateGtStr,"dateGt = ");
        Date dateGe = stringToDate(dateGeStr,"dateGe = ");
        return ((Page<WorkTime>) workTimeService.findWorkTime(taskId,
                                            nikName,
                                            dateLt,
                                            dateLe,
                                            dateGt,
                                            dateGe,
                                            page,
                                            size)).map(workTimeService::getWorkTimeDtoAndUpd);
    }
    @GetMapping("/rep/fact/time")
    public Float getTimeWork(@RequestParam(required = false) Long taskId,
                             @RequestParam(required = false) String nikName ,
                             @RequestParam(required = false, name = "dateLe") String dateLeStr ,
                             @RequestParam(required = false, name = "dateGt") String dateGtStr) {
       Date dateLe = stringToDate(dateLeStr,"dateLe = ");
       Date dateGt = stringToDate(dateGtStr,"dateGt = ");
       if(dateLe == null && dateGt == null ){
           return 0f;
       }
       return workTimeService.getTimeWork(taskId,nikName, dateGt,dateLe );
    }

    @GetMapping("/rep/fact/user")
    public ListString getFactUser(@RequestParam(required = false) Long taskId) {
        return workTimeService.getFactUser(taskId);
    }

    @GetMapping("/rep/fact/week")
    public List<UserWorkDto> getWeekWork(@RequestParam(required = false) String nikName ,
                                         @RequestParam(required = false, name = "dateStart") String dateStartStr ,
                                         @RequestParam(required = false, name = "dateEnd") String dateEndStr) {
        Timestamp dateStart = stringToDate(dateStartStr,"dateStart = ",true);
        Timestamp dateEnd = stringToDate(dateEndStr,"dateEnd = ",true);
        return workTimeService.getWeekWork(nikName,dateStart,dateEnd);
    }

    private Date stringToDate(String dateStr,String text) {
        return stringToDate(dateStr,text,false);
    }
    private Timestamp stringToDate(String dateStr,String text,boolean checkNull) {

        if (dateStr != null) {
            try {
                return new Timestamp(simpleDateFormat.parse(dateStr).getTime());
            } catch (ParseException e) {
                throw new ResourceNotFoundException("Не удалось распарсить дату " + text + " " + dateStr);
            }
        }
        else if(checkNull){
            throw new ResourceNotFoundException("Не не передан обязательный параметр " + text + " null ");
        }
        return null;
    }


}