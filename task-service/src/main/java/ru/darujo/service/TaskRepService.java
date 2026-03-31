package ru.darujo.service;

import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.darujo.dto.ListString;
import ru.darujo.dto.workperiod.UserWorkDto;
import ru.darujo.integration.WorkTimeServiceIntegration;
import ru.darujo.model.Task;
import ru.darujo.repository.TaskRepository;
import ru.darujo.specifications.Specifications;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
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
            List<Long> workIdList,
            Long projectId,
            Date dateLe,
            Date dateGt,
            String type) {
        List<Long> taskIdList = taskService.findTask(null, codeBTS, codeDEVBO, description, workIdList, null, projectId, null, null).getContent()
                .stream()
                .map(Task::getId).toList();
        return workTimeServiceIntegration.getTimeTask(taskIdList, nikName, dateLe, dateGt, type);

    }

    public ListString getFactUsers(List<Long> workIdList, Long projectId, Date dateLe) {
        ListString users = new ListString();
        taskService.findTask(null, null, null, null, workIdList, null, projectId, null, null)
                .stream().map(task ->
                        workTimeServiceIntegration
                                .getUsers(task.getId(), dateLe))
                .forEach(user ->
                        users.getList().addAll(user.getList()));
        return users;
    }

    public Boolean getAvailTime(List<Long> workIdList, Long projectId) {
        Specification<@NonNull Task> specification = Specification.unrestricted();
        specification = Specifications.in(specification, "workId", workIdList);
        specification = Specifications.eq(specification, "projectId", projectId);
        List<Task> tasks = taskRepository.findAll(specification);
        if (tasks.isEmpty()) {
            return false;
        }
        return tasks.stream().anyMatch(task -> workTimeServiceIntegration.availTime(task.getId()));

    }

    public List<UserWorkDto> getWeekWork(List<Long> workIdList, Long projectId, String nikName, Boolean addTotal) {
        List<Task> tasks = taskService.findTask(null, null, null, null, workIdList, null, projectId, null, null).getContent();
        if (tasks.isEmpty()) {
            return null;
        }
        return workTimeServiceIntegration.getWorkUserOrZi(tasks.stream().map(Task::getId).collect(Collectors.toList()), nikName, addTotal, false, null, null);

    }

    public Timestamp getLastTime(List<Long> workId, Timestamp dateLe, Timestamp dateGe) {
        List<Task> tasks = taskService.findTask(null, null, null, null, workId, null, null, null, null).getContent();

        return workTimeServiceIntegration.getLastTime(tasks.stream().map(Task::getId).collect(Collectors.toList()), dateLe, dateGe);
    }
}
