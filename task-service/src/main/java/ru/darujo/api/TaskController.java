package ru.darujo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.darujo.convertor.TaskConvertor;
import ru.darujo.dto.TaskDto;
import ru.darujo.dto.WorkEditDto;
import ru.darujo.exceptions.ResourceNotFoundException;
import ru.darujo.integration.WorkServiceIntegration;
import ru.darujo.model.Task;
import ru.darujo.service.TaskService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

@RestController()
@RequestMapping("/v1/task")
public class TaskController {
    private TaskService taskService;
    @Autowired
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }
    private WorkServiceIntegration workServiceIntegration;

    @Autowired
    public void setWorkServiceIntegration(WorkServiceIntegration workServiceIntegration) {
        this.workServiceIntegration = workServiceIntegration;
    }


    @GetMapping("/{id}")
    public TaskDto WorkTimeEdit(@PathVariable long id) {
        return TaskConvertor.getTaskDto(taskService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Отмеченая работа не найден")));
    }

    @PostMapping("")
    public TaskDto WorkTimeSave(@RequestHeader(required = false) String username,
                                @RequestBody TaskDto taskDto) {
        if (taskDto.getUserName() == null || !taskDto.getUserName().equals("") )
        {
            taskDto.setUserName(username);
        }
        return TaskConvertor.getTaskDto(taskService.saveWorkTime(TaskConvertor.getTask(taskDto)));
    }

    @DeleteMapping("/{id}")
    public void deleteWorkTime(@PathVariable long id) {
        taskService.deleteWorkTime(id);
    }

    @GetMapping("")
    public Iterable<TaskDto> findTasks(@RequestParam(required = false) String userName,
                                   @RequestParam(required = false) String codeBTS,
                                   @RequestParam(required = false) String codeDEVBO,
                                   @RequestParam(required = false) String description,
                                   @RequestParam(required = false) Long workId,
                                   @RequestParam(required = false) String ziName,
                                   @RequestParam(defaultValue = "1")Integer page,
                                   @RequestParam(defaultValue = "10") Integer size) {
        if (workId == null && ziName !=null){
            return findTasks(userName,codeBTS,codeDEVBO,description,ziName);
        }

        return findTasks(userName,codeBTS,codeDEVBO,description,workId,page,size);
    }
    @GetMapping("/rep/time")
    public Float getTaskTime(@RequestParam(required = false) String userName,
                             @RequestParam(required = false) String codeBTS,
                             @RequestParam(required = false) String codeDEVBO,
                             @RequestParam(required = false) String description,
                             @RequestParam(required = false) Long workId,
                             @RequestParam(required = false, name = "dateLe") String dateLeStr ,
                             @RequestParam(required = false, name = "dateGt") String dateGtStr
                                   ) {
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
        return taskService.getTaskTime(
                userName,
                codeBTS,
                codeDEVBO,
                description,
                workId,
                dateLe,
                dateGt);
    }
    public Page<TaskDto> findTasks (String userName,
                                   String codeBTS,
                                   String codeDEVBO,
                                   String description,
                                   Long workId,
                                   Integer page,
                                   Integer size) {
        return ((Page<Task>) taskService.findWorkTime(userName,
                codeBTS,
                codeDEVBO,
                description,
                workId,
                page,
                size)).map(task->
        {

            return taskAddValue(task);

        });
    }

    private TaskDto taskAddValue(Task task) {
        TaskDto taskDto = TaskConvertor.getTaskDto(task);
        try {


            WorkEditDto worEditDto = workServiceIntegration.getWorEditDto(taskDto.getWorkId());
            taskDto.setCodeZi(worEditDto.getCodeZI());
            taskDto.setNameZi(worEditDto.getName());
        }
        catch (ResourceNotFoundException e){
            System.out.println(e.getMessage());
        }
        return taskDto;
    }

    public List<TaskDto> findTasks (String userName,
                                    String codeBTS,
                                    String codeDEVBO,
                                    String description,
                                    String ziName) {
        List<TaskDto> taskDtoList = new ArrayList<>();
        taskService.findWorkTime(userName,
                codeBTS,
                codeDEVBO,
                description,
                null,
                null,
                null).forEach(task->
        {
            TaskDto taskDto = taskAddValue(task);
            System.out.println(taskDto.getNameZi().matches( ".*" + ziName + "*" ));
            if(taskDto.getNameZi().matches( ".*" + ziName + "*" )) {
                taskDtoList.add(taskDto);
            }

        });
        return taskDtoList;
    }
}