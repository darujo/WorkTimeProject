package ru.darujo.api;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.darujo.assistant.helper.DateHelper;
import ru.darujo.convertor.TaskConvertor;
import ru.darujo.dto.TaskDto;
import ru.darujo.dto.ratestage.AttrDto;
import ru.darujo.dto.work.WorkLittleDto;
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.integration.UserServiceIntegrationImp;
import ru.darujo.integration.WorkServiceIntegrationImp;
import ru.darujo.model.Task;
import ru.darujo.service.TaskService;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.*;

@Log4j2
@RestController()
@RequestMapping("/v1/task")
public class TaskController {
    private TaskService taskService;

    @Autowired
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    private WorkServiceIntegrationImp workServiceIntegration;

    @Autowired
    public void setWorkServiceIntegration(WorkServiceIntegrationImp workServiceIntegration) {
        this.workServiceIntegration = workServiceIntegration;
    }

    UserServiceIntegrationImp userServiceIntegration;

    @Autowired
    public void setUserServiceIntegration(UserServiceIntegrationImp userServiceIntegration) {
        this.userServiceIntegration = userServiceIntegration;
    }

    @GetMapping("/{id}")
    public TaskDto TaskEdit(@PathVariable long id) {
        return TaskConvertor.getTaskDto(taskService.findById(id).orElseThrow(() -> new ResourceNotFoundRunTime("Отмеченная работа не найден")));
    }

    @GetMapping("/refresh/{id}")
    public boolean TaskRefresh(@PathVariable long id,
                               @RequestParam(required = false, name = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateStr
    ) {
        Timestamp date = DateHelper.DTZToDate(dateStr, "date");
        return taskService.refreshTime(id, date);

    }

    @GetMapping("/right/{right}")
    public boolean checkRight(@PathVariable String right,
                              @RequestParam(name = "system_right", required = false) List<String> userRight
    ) {

        return taskService.checkRight(right, userRight);

    }

    @PostMapping("")
    public TaskDto TaskSave(@RequestHeader(required = false) String username,
                            @RequestBody TaskDto taskDto,
                            @RequestParam("system_right") List<String> right,
                            @RequestParam("system_project") Long projectId,
                            @RequestParam("system_project_code") String projectName) {
        taskService.checkRight(taskDto.getId() == null ? "create" : "edit", right);
        if (taskDto.getNikName() == null || !taskDto.getNikName().isEmpty()) {
            taskDto.setNikName(username);
        }
        return TaskConvertor.getTaskDto(taskService.saveWorkTime(projectName, TaskConvertor.getTask(projectId, taskDto)));
    }

    @PostMapping("/checkAvail")
    public AttrDto<Integer> TaskCheckAvail(@RequestBody TaskDto taskDto) {
        String test = taskService.taskCheck(taskDto);
        if (test != null) {
            return new AttrDto<>(-1, test);
        }
        return new AttrDto<>(0, "");
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable long id,
                           @RequestParam("system_right") List<String> right) {
        taskService.checkRight("delete", right);
        taskService.deleteWorkTime(id);
    }

    @GetMapping("")
    public Page<TaskDto> findTasks(@RequestParam(required = false) String nikName,
                                   @RequestParam(required = false) String codeBTS,
                                   @RequestParam(required = false) String codeDEVBO,
                                   @RequestParam(required = false) String description,
                                   @RequestParam(required = false) List<Long> workId,
                                   @RequestParam(required = false) String ziName,
                                   @RequestParam(required = false) Integer type,
                                   @RequestParam(defaultValue = "1") Integer page,
                                   @RequestParam(defaultValue = "10") Integer size,
                                   @RequestParam(required = false) Long[] arrTaskId,
                                   @RequestParam("system_project") Long projectId) {
        List<Long> listTaskId = null;
        if (arrTaskId != null && arrTaskId.length != 0) {
            listTaskId = Arrays.asList(arrTaskId);

        }
        clearCash();
        return taskService.findTask(nikName,
                codeBTS,
                codeDEVBO,
                description,
                ziName,
                workId,
                type,
                listTaskId,
                projectId,
                page,
                size).map(this::taskAddValue);

    }


    @GetMapping("/list/id")
    public Iterable<Long> findTasks(@RequestParam(required = false) String nikName,
                                    @RequestParam(required = false) String codeBTS,
                                    @RequestParam(required = false) String codeDEVBO,
                                    @RequestParam(required = false) String description,
                                    @RequestParam(required = false) List<Long> workId,
                                    @RequestParam(required = false) Long projectId) {
        Set<Long> listId = new HashSet<>();
        taskService.findTask(nikName,
                codeBTS,
                codeDEVBO,
                description,
                workId,
                null,
                projectId,
                null,
                null).forEach(task -> listId.add(task.getId()));
        return listId;
    }

    private final Map<Long, WorkLittleDto> workLittleDtoMap = new HashMap<>();

    private void clearCash() {
        workLittleDtoMap.clear();
    }

    private TaskDto taskAddValue(Task task) {
        TaskDto taskDto = TaskConvertor.getTaskDto(task);
        try {

            WorkLittleDto workLittleDto = workLittleDtoMap.get(taskDto.getWorkId());
            if (workLittleDto == null) {
                workLittleDto = workServiceIntegration.getWorEditDto(taskDto.getWorkId());
                workLittleDtoMap.put(taskDto.getId(), workLittleDto);
            }
            taskDto.setCodeZi(workLittleDto.getCodeZI());
            taskDto.setNameZi(workLittleDto.getName());
        } catch (ResourceNotFoundRunTime e) {
            log.error(e.getMessage());
            if (task.getType() == 1) {
                taskDto.setCodeZi("Нет ЗИ с ID = " + taskDto.getWorkId());
                taskDto.setNameZi("Нет ЗИ с ID = " + taskDto.getWorkId());
            }
        }
        userServiceIntegration.updFio(taskDto);
        return taskDto;
    }

}