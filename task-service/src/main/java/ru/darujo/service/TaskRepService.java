package ru.darujo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.darujo.dto.ListString;
import ru.darujo.dto.workperiod.UserWorkDto;
import ru.darujo.integration.WorkTimeServiceIntegration;
import ru.darujo.model.Task;
import ru.darujo.repository.TaskRepository;
import ru.darujo.repository.specifications.TaskSpecifications;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Primary
public class TaskRepService {

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

    private TaskService taskService;

    @Autowired
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
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
        return ((List<Task>) taskService.findTask(null, codeBTS, codeDEVBO, description, workId, null, null, null))
                .stream()
                .map(task -> workTimeServiceIntegration.getTimeTask(task.getId(), nikName, dateLe, dateGt, type))
                .reduce(Float::sum)
                .orElse(0f);
    }

    public ListString getFactUsers(Long workId, Date dateLe) {
        ListString users = new ListString();
        ((List<Task>) taskService.findTask(null, null, null, null, workId, null, null, null))
                .stream().map(task ->
                        workTimeServiceIntegration
                                .getUsers(task.getId(), dateLe))
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

    public List<UserWorkDto> getWeekWork(Long workId, String nikName, Boolean addTotal) {
        List<Task> tasks = (List<Task>) taskService.findTask(null, null, null, null, workId, null, null, null);

        return workTimeServiceIntegration.getWorkUserOrZi(tasks.stream().map(Task::getId).collect(Collectors.toList()), nikName, addTotal, false, null, null);

    }
}
