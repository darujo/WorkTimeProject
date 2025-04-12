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
import java.util.concurrent.atomic.AtomicReference;

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
        LinkedHashMap<String,UserWorkDto> userWorkDtoHashMap = new LinkedHashMap<>();
        taskService.findTask(null,null,null,null,workId,null,null,null)
                .forEach(task -> workTimeServiceIntegration.getWorkUserOrZi(task.getId(), nikName, addTotal, false, null, null)
                        .forEach(userWorkDto -> {
                            UserWorkDto userWorkDtoSave = userWorkDtoHashMap.get(userWorkDto.getNikName());
                            if (userWorkDtoSave == null){
                                userWorkDtoHashMap.put(userWorkDto.getNikName(),userWorkDto);
                            } else {
                                userWorkDto.getWorkTime().forEach(userWorkDtoSave::addTime);
                            }
                        }));
        List<UserWorkDto> userWorkDTOs = new ArrayList<>();
        AtomicReference<Boolean> first = new AtomicReference<>(true);
        userWorkDtoHashMap.forEach((s, userWorkDto) -> {
            if(first.get()) {
                first.set(false);
                userWorkDto.setUserCol(userWorkDtoHashMap.size());

            } else {
                userWorkDto.setUserCol(null);
            }
            userWorkDTOs.add(userWorkDto.addTimeAll());

        });
        return userWorkDTOs;
    }
}
