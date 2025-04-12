package ru.darujo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.darujo.exceptions.ResourceNotFoundException;
import ru.darujo.integration.WorkServiceIntegration;
import ru.darujo.model.Task;
import ru.darujo.repository.TaskRepository;
import ru.darujo.repository.specifications.TaskSpecifications;

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
        if (task.getCodeDEVBO() == null || task.getCodeDEVBO().equals("") || task.getCodeDEVBO().equalsIgnoreCase("DeVbo-000")) {
            if (task.getCodeBTS() == null || task.getCodeBTS().equals("")) {
                throw new ResourceNotFoundException("Не задан номер DEVBO и BTS");
            }
        }
        if (task.getType() == 1) {
            workServiceIntegration.getWorEditDto(task.getWorkId());
        }
        task.setRefresh(new Timestamp(System.currentTimeMillis()));
        if (task.getId() != null) {
            Task taskSave = findById(task.getId()).orElseThrow(() -> new ResourceNotFoundException("Отмеченая работа не найден"));
            if(task.getTimeCreate() == null && taskSave.getTimeCreate() != null){
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
        Specification<Task> specification = Specification.where(TaskSpecifications.queryDistinctTrue());
        specification = getTaskSpecificationLike("nikName", nikName, specification);
        specification = getTaskSpecificationLike("codeBTS", codeBTS, specification);
        specification = getTaskSpecificationLike("codeDEVBO", codeDEVBO, specification);
        specification = getTaskSpecificationLike("description", description, specification);
        if (type != null) {
            specification = specification.and(TaskSpecifications.typeEq(type));
        }
        if (workId != null) {
            specification = specification.and(TaskSpecifications.workIdEQ(workId));
        }
        if (page != null) {
            return taskRepository.findAll(specification, PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "refresh")));
        } else {
            return taskRepository.findAll(specification);
        }

    }

    private Specification<Task> getTaskSpecificationLike(String field, String value, Specification<Task> specification) {
        if (value != null && !value.equals("")) {
            specification = specification.and(TaskSpecifications.like(field, value));
        }
        return specification;
    }

    @Transactional
    public boolean refreshTime(long id) {
        Task task = findById(id).orElseThrow(() -> new ResourceNotFoundException("Отмеченая работа не найден"));
        task.setRefresh(new Timestamp(System.currentTimeMillis()));
        taskRepository.save(task);
        return true;
    }

    public String workTimeCheckAvail(Long id, Long workId, String codeDEVBO, String codeBTS) {

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
            specification = getTaskSpecificationLike(dbField, value, specification);
            if (id != null) {
                specification = specification.and(TaskSpecifications.notEqual("id", id));
            }
            Page<Task> page;
            if (workId != null) {
                Specification<Task> specificationNotWork = specification.and(TaskSpecifications.notEqual("workId", workId));
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
