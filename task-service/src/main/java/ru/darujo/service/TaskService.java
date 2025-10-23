package ru.darujo.service;

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

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.*;

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
    public Task saveWorkTime(Task task) {
        if(task.getCodeDEVBO() != null && task.getCodeDEVBO().equalsIgnoreCase("DevBO-000")){
            task.setCodeDEVBO(null);
        }
        if (task.getCodeDEVBO() == null || task.getCodeDEVBO().equals("") || task.getCodeDEVBO().equalsIgnoreCase("DeVbo-000")) {
            if (task.getCodeBTS() == null || task.getCodeBTS().equals("")) {
                throw new ResourceNotFoundRunTime("Не задан номер DEVBO и BTS");
            }
        }
        if (CodeService.getTaskTypeIsZi(task.getType())) {
            if (task.getWorkId() == null) {
                throw new ResourceNotFoundRunTime("Не выбрано ЗИ");
            }
            workServiceIntegration.getWorEditDto(task.getWorkId());
        }
        if (task.getType() == 1 && (task.getCodeBTS() != null && !task.getCodeBTS().equals(""))) {
            task.setType(5);
        }
        task.setRefresh(new Timestamp(System.currentTimeMillis()));
        if (task.getId() != null) {
            Task taskSave = findById(task.getId()).orElseThrow(() -> new ResourceNotFoundRunTime("Отмеченая работа не найден"));
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
                                   String codeDEVBO,
                                   String description,
                                   Long workId,
                                   Integer type,
                                   Integer page,
                                   Integer size) {
        return findTask(nikName,
                codeBTS,
                codeDEVBO,
                description,
                workId,
                type,
                null,
                page,
                size);
    }

    public Iterable<Task> findTask(String nikName,
                                   String codeBTS,
                                   String codeDEVBO,
                                   String description,
                                   Long workId,
                                   Integer type,
                                   List<Long> listTaskId,
                                   Integer page,
                                   Integer size) {
        Specification<Task> specification = Specification.where(Specifications.queryDistinctTrue());
        specification = Specifications.inLong(specification, "id", listTaskId);
        specification = Specifications.like(specification,"nikName", nikName );
        specification = Specifications.like(specification,"codeBTS", codeBTS);
        specification = Specifications.like(specification,"codeDEVBO", codeDEVBO);
        specification = Specifications.like(specification,"description", description);
        specification = Specifications.eq(specification, "type", type);
        specification = Specifications.eq(specification, "workId", workId);
        if (page != null) {
            return taskRepository.findAll(specification, PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "refresh")));
        } else {
            return taskRepository.findAll(specification);
        }

    }


    @Transactional
    public boolean refreshTime(long id, Date date) {

        Task task = findById(id).orElseThrow(() -> new ResourceNotFoundRunTime("Отмеченая работа не найден"));
        task.setRefresh(new Timestamp(System.currentTimeMillis()));
        taskRepository.save(task);
        boolean ok = workServiceIntegration.setWorkDate(task.getWorkId(), date);
        System.out.println("Обновили даты в ЗИ? " + ok);
        return true;
    }

    public String taskCheck(TaskDto taskDto) {
        String text = null;
        if (taskDto.getWorkId() != null && taskDto.getType() == 1 && (taskDto.getCodeBTS() != null && !taskDto.getCodeBTS().equals(""))) {
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

    public String taskCheckAvail(Long id, Long workId, String codeDEVBO, String codeBTS) {

        String text = checkAvail(id, workId, "codeBTS", codeBTS);
        if (text != null) {
            return text;
        }
        text = checkAvail(id, workId, "codeDEVBO", codeDEVBO);
        return text;
    }

    private String checkAvail(Long id, Long workId, String dbField, String value) {

        Specification<Task> specification = Specification.where(null);

        if (value != null && !value.equals("")) {
            specification = Specifications.like(specification, dbField, value);
            specification = Specifications.notEqual(specification,"id", id);
            Page<Task> page;
            if (workId != null) {
                Specification<Task> specificationNotWork = Specifications.notEqual(specification,"workId", workId);
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
}
