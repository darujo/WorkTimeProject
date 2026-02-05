package ru.darujo.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.darujo.dto.TaskDto;
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.integration.WorkServiceIntegration;
import ru.darujo.model.Task;
import ru.darujo.repository.TaskRepository;
import ru.darujo.specifications.Specifications;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Primary
public class TaskService {

    private TaskRepository taskRepository;

    @Autowired
    public void setTaskRepository(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }


    private WorkServiceIntegration workServiceIntegration;

    @Autowired
    public void setWorkServiceIntegration(WorkServiceIntegration workServiceIntegration) {
        this.workServiceIntegration = workServiceIntegration;
    }

    public Optional<Task> findById(long id) {
        return taskRepository.findById(id);
    }

    @Transactional
    public Task saveWorkTime(String projectName, Task task) {
        if (task.getCode() != null && task.getCode().equalsIgnoreCase(projectName + "-000")) {
            task.setCode(null);
        }
        if (task.getCode() == null || task.getCode().isEmpty() || task.getCode().equalsIgnoreCase("DeVbo-000")) {
            if (task.getCodeBTS() == null || task.getCodeBTS().isEmpty()) {
                throw new ResourceNotFoundRunTime("Не задан номер " + projectName + " и BTS");
            }
        }
        if (CodeService.getTaskTypeIsZi(task.getType())) {
            if (task.getWorkId() == null) {
                throw new ResourceNotFoundRunTime("Не выбрано ЗИ");
            }
            workServiceIntegration.getWorEditDto(task.getWorkId());
        }
        if (task.getType() == 1 && (task.getCodeBTS() != null && !task.getCodeBTS().isEmpty())) {
            task.setType(5);
        }
        task.setRefresh(new Timestamp(System.currentTimeMillis()));
        if (task.getId() != null) {
            Task taskSave = findById(task.getId()).orElseThrow(() -> new ResourceNotFoundRunTime("Отмеченная работа не найден"));
            if (task.getTimeCreate() == null && taskSave.getTimeCreate() != null) {
                task.setTimeCreate(taskSave.getTimeCreate());
            }
        } else {
            task.setTimeCreate(task.getRefresh());
        }
        return taskRepository.save(task);
    }

    public void deleteWorkTime(Long id) {
        taskRepository.deleteById(id);
    }

    public Iterable<Task> findTask(String nikName,
                                   String codeBTS,
                                   String code,
                                   String description,
                                   Long workId,
                                   Integer type,
                                   Long projectId,
                                   Integer page,
                                   Integer size) {
        return findTask(nikName,
                codeBTS,
                code,
                description,
                workId,
                type,
                null,
                projectId,
                page,
                size);
    }

    public Iterable<Task> findTask(String nikName,
                                   String codeBTS,
                                   String code,
                                   String description,
                                   Long workId,
                                   Integer type,
                                   List<Long> listTaskId,
                                   Long projectId,
                                   Integer page,
                                   Integer size) {
        Specification<@NonNull Task> specification = Specification.where(Specifications.queryDistinctTrue());
        specification = Specifications.in(specification, "id", listTaskId);
        specification = Specifications.like(specification, "nikName", nikName);
        specification = Specifications.like(specification, "codeBTS", codeBTS);
        specification = Specifications.like(specification, "code", code);
        specification = Specifications.like(specification, "description", description);
        specification = Specifications.eq(specification, "type", type);
        specification = Specifications.eq(specification, "workId", workId);
        specification = Specifications.eq(specification, "projectId", projectId);
        if (page != null) {
            return taskRepository.findAll(specification, PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "refresh")));
        } else {
            return taskRepository.findAll(specification);
        }

    }


    @Transactional
    public boolean refreshTime(long id, Date date) {

        Task task = findById(id).orElseThrow(() -> new ResourceNotFoundRunTime("Отмеченная работа не найден"));
        task.setRefresh(new Timestamp(System.currentTimeMillis()));
        taskRepository.save(task);
        boolean ok = workServiceIntegration.setWorkDate(task.getWorkId(), date);
        log.info("Обновили даты в ЗИ? {}", ok);
        return true;
    }

    public String taskCheck(TaskDto taskDto) {
        String text = null;
        if (taskDto.getWorkId() != null && taskDto.getType() == 1 && (taskDto.getCodeBTS() != null && !taskDto.getCodeBTS().isEmpty())) {
            text = "Тип задачи будет изменен на \"Запросы по ЗИ\" так как тип задачи \"ЗИ\" и по ней введен номер запроса";
        }
        String testAvail = taskCheckAvail(taskDto.getId(), taskDto.getWorkId(), taskDto.getCodeDEVBO(), taskDto.getCodeBTS());
        if (text == null) {
            return testAvail;
        }
        if (testAvail == null) {
            return text;
        }
        return text + " " + testAvail;
    }

    public String taskCheckAvail(Long id, Long workId, String code, String codeBTS) {

        String text = checkAvail(id, workId, "codeBTS", codeBTS);
        if (text != null) {
            return text;
        }
        text = checkAvail(id, workId, "code", code);
        return text;
    }

    private String checkAvail(Long id, Long workId, String dbField, String value) {

        Specification<@NonNull Task> specification = Specification.unrestricted();

        if (value != null && !value.isEmpty()) {
            specification = Specifications.like(specification, dbField, value);
            specification = Specifications.notEqual(specification, "id", id);
            Page<@NonNull Task> page;
            if (workId != null) {
                Specification<@NonNull Task> specificationNotWork = Specifications.notEqual(specification, "workId", workId);
                page = taskRepository.findAll(specificationNotWork, PageRequest.of(0, 5));
                if (page.getTotalElements() > 0) {
                    return "Уже есть с " + dbField + ": " + value + "  по другим ЗИ " + page.getTotalElements() + " записей";
                }
            }
            page = taskRepository.findAll(specification, PageRequest.of(0, 5));
            if (page.getTotalElements() > 0) {
                return "Уже есть с " + dbField + ": " + value + " количество записей: " + page.getTotalElements();
            }
        }
        return null;
    }

    public boolean checkRight(String right, List<String> userRights) {
        right = right.toLowerCase();
        if (right.equals("edit") || right.equals("delete")) {
            if (userRights == null || !userRights.contains("TASK_EDIT")) {
                throw new ResourceNotFoundRunTime("У вас нет права на редактирование TASK_EDIT");
            }
        } else if (right.equals("create")) {
            if (userRights == null || !userRights.contains("TASK_CREATE")) {
                throw new ResourceNotFoundRunTime("У вас нет права на редактирование TASK_CREATE");
            }
        }
        return true;
    }
}
