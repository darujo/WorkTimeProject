package ru.darujo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.darujo.dto.ListString;
import ru.darujo.integration.TaskServiceIntegration;
import ru.darujo.integration.UserServiceIntegration;
import ru.darujo.integration.WorkTimeServiceIntegration;
import ru.darujo.model.Task;
import ru.darujo.repository.TaskRepository;
import ru.darujo.repository.specifications.TaskSpecifications;

import java.util.*;

@Service
@Primary
public class TaskService {

    private TaskRepository taskRepository;
    private Iterable<Task> taskPage;

    @Autowired
    public void setWorkTimeRepository(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    private WorkTimeServiceIntegration workTimeServiceIntegration;

    @Autowired
    public void setWorkTimeServiceIntegration(WorkTimeServiceIntegration workTimeServiceIntegration) {
        this.workTimeServiceIntegration = workTimeServiceIntegration;
    }

    public Optional<Task> findById(long id) {
        return taskRepository.findById(id);
    }

    public Task saveWorkTime(Task task) {
        taskPage = null;
        return taskRepository.save(task);
    }

    public void deleteWorkTime(Long id) {
        taskRepository.deleteById(id);
        taskPage = null;
    }

    public Iterable<Task> findWorkTime(String nikName,
                                       String codeBTS,
                                       String codeDEVBO,
                                       String description,
                                       Long workId,
                                       Integer page,
                                       Integer size) {
        Specification<Task> specification = Specification.where(null);
        if (nikName != null) {
            specification = specification.and(TaskSpecifications.nikNameLike(nikName));
        }
        if (codeBTS != null) {
            specification = specification.and(TaskSpecifications.codeBTSLike(codeBTS));
        }
        if (codeDEVBO != null) {
            specification = specification.and(TaskSpecifications.codeDEVBOLike(codeDEVBO));
        }
        if (description != null) {
            specification = specification.and(TaskSpecifications.descriptionLike(description));
        }
        if (workId != null) {
            specification = specification.and(TaskSpecifications.workIdEQ(workId));
        }
        if (page != null) {
            taskPage = taskRepository.findAll(specification, PageRequest.of(page - 1, size));
        } else {
            taskPage = taskRepository.findAll(specification);
        }

        System.out.println(taskPage);
        return taskPage;
    }

    public Float getTaskTime(
            String userName,
            String codeBTS,
            String codeDEVBO,
            String description,
            Long workId,
            Date dateLe,
            Date dateGt) {
        return ((List<Task>) findWorkTime(null, codeBTS, codeDEVBO, description, workId, null, null))
                .stream().map(task -> workTimeServiceIntegration.getTimeTask(task.getId(), userName, dateLe, dateGt)).reduce((sumTime, time) ->
                        sumTime + time).orElse(0f);
    }

    public ListString getFactUsers(Long workId) {
        ListString users = new ListString();
        ((List<Task>) findWorkTime(null, null, null, null, workId, null, null))
                .stream().map(task ->
                        workTimeServiceIntegration
                                .getUsers(task.getId()))
                .forEach(user ->
                        users.getList().addAll(user.getList()));
        return users;
    }
}
