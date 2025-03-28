package ru.darujo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.darujo.dto.ListString;
import ru.darujo.exceptions.ResourceNotFoundException;
import ru.darujo.integration.WorkServiceIntegration;
import ru.darujo.integration.WorkTimeServiceIntegration;
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

    private WorkTimeServiceIntegration workTimeServiceIntegration;

    @Autowired
    public void setWorkTimeServiceIntegration(WorkTimeServiceIntegration workTimeServiceIntegration) {
        this.workTimeServiceIntegration = workTimeServiceIntegration;
    }

    private WorkServiceIntegration workServiceIntegration;

    @Autowired
    public void setWorkServiceIntegration(WorkServiceIntegration workServiceIntegration) {
        this.workServiceIntegration = workServiceIntegration;
    }

    public Optional<Task> findById(long id) {
        return taskRepository.findById(id);
    }

    public Task saveWorkTime(Task task) {
        if (task.getType() == 1) {
            workServiceIntegration.getWorEditDto(task.getWorkId());
        }
        task.setRefresh(new Timestamp(System.currentTimeMillis()));
        return taskRepository.save(task);
    }

    public void deleteWorkTime(Long id) {
        taskRepository.deleteById(id);
    }

    public Iterable<Task> findWorkTime(String nikName,
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
            return taskRepository.findAll(specification, PageRequest.of(page - 1, size, Sort.by("refresh")));
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

    public Float getTaskTime(
            String nikName,
            String codeBTS,
            String codeDEVBO,
            String description,
            Long workId,
            Date dateLe,
            Date dateGt,
            String type) {
        return ((List<Task>) findWorkTime(null, codeBTS, codeDEVBO, description, workId, null, null, null))
                .stream()
                .map(task -> workTimeServiceIntegration.getTimeTask(task.getId(), nikName, dateLe, dateGt, type))
                .reduce((sumTime, time) -> sumTime + time)
                .orElse(0f);
    }

    public ListString getFactUsers(Long workId) {
        ListString users = new ListString();
        ((List<Task>) findWorkTime(null, null, null, null, workId, null, null, null))
                .stream().map(task ->
                        workTimeServiceIntegration
                                .getUsers(task.getId()))
                .forEach(user ->
                        users.getList().addAll(user.getList()));
        return users;
    }

    public Boolean getAvailTime(long workId) {
        Specification<Task> specification = Specification.where(null);
        specification = specification.and(TaskSpecifications.workIdEQ(workId));
        List<Task> tasks = taskRepository.findAll(specification);
        if (tasks.size() == 0) {
            return false;
        }
        return tasks.stream().anyMatch(task -> workTimeServiceIntegration.availTime(task.getId()));

    }
    @Transactional
    public boolean refreshTime(long id) {
        Task task = findById(id).orElseThrow(() -> new ResourceNotFoundException("Отмеченая работа не найден"));
        saveWorkTime(task);
        return true;
    }
}
