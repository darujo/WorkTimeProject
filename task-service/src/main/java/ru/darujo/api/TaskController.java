package ru.darujo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.darujo.convertor.TaskConvertor;
import ru.darujo.dto.ListString;
import ru.darujo.dto.TaskDto;
import ru.darujo.dto.UserDto;
import ru.darujo.dto.work.WorkLittleDto;
import ru.darujo.dto.parsing.DateParser;
import ru.darujo.exceptions.ResourceNotFoundException;
import ru.darujo.integration.UserServiceIntegration;
import ru.darujo.integration.WorkServiceIntegration;
import ru.darujo.model.Task;
import ru.darujo.service.TaskService;

import java.time.ZonedDateTime;
import java.util.*;

@RestController()
@RequestMapping("/v1/task")
public class TaskController extends DateParser {
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

    UserServiceIntegration userServiceIntegration;

    @Autowired
    public void setUserServiceIntegration(UserServiceIntegration userServiceIntegration) {
        this.userServiceIntegration = userServiceIntegration;
    }

    @GetMapping("/{id}")
    public TaskDto TaskEdit(@PathVariable long id) {
        return TaskConvertor.getTaskDto(taskService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Отмеченая работа не найден")));
    }

    @GetMapping("/refresh/{id}")
    public boolean TaskRefresh(@PathVariable long id) {
        return taskService.refreshTime(id);

    }

    @GetMapping("/right/{right}")
    public boolean checkRight (@PathVariable String right,
                               @RequestHeader(defaultValue = "false", name = "TASK_EDIT") boolean rightEdit,
                               @RequestHeader(defaultValue = "false", name = "TASK_CREATE") boolean rightCreate){
        right = right.toLowerCase();
        if( right.equals("edit")){
            if(!rightEdit) {
                throw new ResourceNotFoundException("У вас нет права на редактирование TASK_EDIT");
            }
        }
        else if( right.equals("create")){
            if(!rightCreate) {
                throw new ResourceNotFoundException("У вас нет права на редактирование TASK_CREATE");
            }
        }
        return true;

    }
    @PostMapping("")
    public TaskDto TaskSave(@RequestHeader(required = false) String username,
                            @RequestBody TaskDto taskDto,
                            @RequestHeader(defaultValue = "false", name = "TASK_EDIT") boolean right) {
        if (!right) {
            throw new ResourceNotFoundException("У вас нет права TASK_EDIT");
        }
        if (taskDto.getNikName() == null || !taskDto.getNikName().equals("")) {
            taskDto.setNikName(username);
        }
        return TaskConvertor.getTaskDto(taskService.saveWorkTime(TaskConvertor.getTask(taskDto)));
    }
    @PostMapping("/checkAvail")
    public String TaskCheckAvail(@RequestBody TaskDto taskDto) {
        return taskService.workTimeCheckAvail(taskDto.getId(),taskDto.getWorkId(), taskDto.getCodeDEVBO(), taskDto.getCodeBTS());
    }
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable long id) {
        taskService.deleteWorkTime(id);
    }

    @GetMapping("")
    public Iterable<TaskDto> findTasks(@RequestParam(required = false) String nikName,
                                       @RequestParam(required = false) String codeBTS,
                                       @RequestParam(required = false) String codeDEVBO,
                                       @RequestParam(required = false) String description,
                                       @RequestParam(required = false) Long workId,
                                       @RequestParam(required = false) String ziName,
                                       @RequestParam(required = false) Integer type,
                                       @RequestParam(defaultValue = "1") Integer page,
                                       @RequestParam(defaultValue = "10") Integer size) {
        if (workId == null && ziName != null) {
            return findTasks(nikName, codeBTS, codeDEVBO, description, ziName,workId, type);
        }

        return findTasks(nikName, codeBTS, codeDEVBO, description, workId, type, page, size);
    }

    @GetMapping("/rep/fact/time")
    public Float getTaskTime(@RequestParam(required = false) String nikName,
                             @RequestParam(required = false) String codeBTS,
                             @RequestParam(required = false) String codeDEVBO,
                             @RequestParam(required = false) String description,
                             @RequestParam(required = false) Long workId,
                             @RequestParam(required = false, name = "dateLe") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateLeStr,
                             @RequestParam(required = false, name = "dateGt") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateGtStr,
                             @RequestParam(required = false) String type
    ) {
        Date dateLe = stringToDate(dateLeStr, "dateLe = ");
        Date dateGt = stringToDate(dateGtStr, "dateGt = ");
        return taskService.getTaskTime(
                nikName,
                codeBTS,
                codeDEVBO,
                description,
                workId,
                dateLe,
                dateGt,
                type);
    }

    @GetMapping("/rep/fact/user")
    public ListString getFactUsers(@RequestParam(required = false) Long workId,
                                   @RequestParam(required = false, name = "dateLe") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateLeStr
                                   ) {
        Date dateLe = stringToDate(dateLeStr, "dateLe = ");

        return taskService.getFactUsers(
                workId,dateLe);
    }
    @GetMapping("/rep/fact/avail/{workId}")
    public Boolean getFactUsers(@PathVariable long workId
    ) {
        return taskService.getAvailTime(workId);
    }
    public Page<TaskDto> findTasks(String userName,
                                   String codeBTS,
                                   String codeDEVBO,
                                   String description,
                                   Long workId,
                                   Integer type,
                                   Integer page,
                                   Integer size) {
        clearCash();
        return ((Page<Task>) taskService.findTask(userName,
                codeBTS,
                codeDEVBO,
                description,
                workId,
                type,
                page,
                size)).map(this::taskAddValue);
    }

    @GetMapping("/list/id")
    public Iterable<Long> findTasks(@RequestParam(required = false) String nikName,
                                       @RequestParam(required = false) String codeBTS,
                                       @RequestParam(required = false) String codeDEVBO,
                                       @RequestParam(required = false) String description,
                                       @RequestParam(required = false) Long workId){
        Set<Long> listId = new HashSet<>();
        taskService.findTask(nikName,
                codeBTS,
                codeDEVBO,
                description,
                workId,
                null,
                null,
                null).forEach(task ->  listId.add(task.getId()));
        return listId;
    }

    private final Map<String, UserDto> userDtoMap = new HashMap<>();
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
        } catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
            if (task.getType() == 1) {
                taskDto.setCodeZi("Нет ЗИ с ID = " + taskDto.getWorkId());
                taskDto.setNameZi("Нет ЗИ с ID = " + taskDto.getWorkId());
            }
        }
        try {
            UserDto userDto = userDtoMap.get(task.getNikName());
            if (userDto == null) {
                userDto = userServiceIntegration.getUserDto(null, task.getNikName());
                userDtoMap.put(task.getNikName(), userDto);
            }
            taskDto.setFirstName(userDto.getFirstName());
            taskDto.setLastName(userDto.getLastName());
            taskDto.setPatronymic(userDto.getPatronymic());
        } catch (ResourceNotFoundException e) {
            taskDto.setFirstName("Нет пользователя с ником " + task.getNikName());

            System.out.println(e.getMessage());
        }

        return taskDto;
    }

    public List<TaskDto> findTasks(String userName,
                                   String codeBTS,
                                   String codeDEVBO,
                                   String description,
                                   String ziName,
                                   Long workId,
                                   Integer type) {
        clearCash();
        List<TaskDto> taskDtoList = new ArrayList<>();
        taskService.findTask(userName,
                codeBTS,
                codeDEVBO,
                description,
                workId,
                type,
                null,
                null).forEach(task ->
        {
            TaskDto taskDto = taskAddValue(task);
            if (ziName== null || ziName.equals("") || (taskDto.getNameZi() != null && taskDto.getNameZi().matches(".*" + ziName + "*"))) {
                taskDtoList.add(taskDto);
            }

        });
        return taskDtoList;
    }


}