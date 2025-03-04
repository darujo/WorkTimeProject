package ru.darujo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.darujo.dto.ListString;
import ru.darujo.dto.WorkDto;
import ru.darujo.exceptions.ResourceNotFoundException;
import ru.darujo.integration.TaskServiceIntegration;
import ru.darujo.model.WorkTime;
import ru.darujo.repository.WorkTimeRepository;
import ru.darujo.repository.specifications.WorkTimeSpecifications;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Primary
public class WorkTimeService {
    private TaskServiceIntegration taskServiceIntegration;
    @Autowired
    public void setWorkServiceIntegration(TaskServiceIntegration taskServiceIntegration) {
        this.taskServiceIntegration = taskServiceIntegration;
    }

    private WorkTimeRepository workTimeRepository;
    @Autowired
    public void setWorkTimeRepository(WorkTimeRepository workTimeRepository) {
        this.workTimeRepository = workTimeRepository;
    }

    public Optional<WorkTime> findById(long id) {
        return workTimeRepository.findById(id);
    }

    public WorkTime saveWorkTime(WorkTime workTime) {
        validWorkTime(workTime);
        WorkDto workDto = taskServiceIntegration.getWork(workTime.getTaskId());
        return workTimeRepository.save(workTime);
    }

    public void deleteWorkTime(Long id) {
        workTimeRepository.deleteById(id);
    }

    public Iterable<WorkTime> findWorkTime(Long taskId, String userName, Date dateLE, Date dateGT, Date dateGE, Integer page, Integer size) {
        Specification<WorkTime> specification = Specification.where(null);
        if (dateLE != null) {
            specification = specification.and(WorkTimeSpecifications.dateLE(dateLE));
        }
        if (dateGE != null) {
            specification = specification.and(WorkTimeSpecifications.dateGE(dateGE));
        }
        if (dateGT != null) {
            specification = specification.and(WorkTimeSpecifications.dateGT(dateGT));
        }
        if (userName != null) {
            specification = specification.and(WorkTimeSpecifications.userNameEQ(userName));
        }
        if (taskId != null) {
            specification = specification.and(WorkTimeSpecifications.taskIdEQ(taskId));
        }
        if (page == null){
            return workTimeRepository.findAll(specification);

        }
        else {

            return workTimeRepository.findAll(specification, PageRequest.of(page - 1, size,Sort.by("workDate").and(Sort.by("userName"))));
        }
    }
    public float getTimeWork(Long taskId, String username,Date dateLE, Date dateGT){
        AtomicReference<Float> time = new AtomicReference<>((float) 0);
        findWorkTime(taskId, username, dateLE, dateGT,null,null, null).forEach(workTime -> time.set(time.get() + workTime.getWorkTime()));
        return time.get();
    }

    public ListString getFactUser(Long taskId) {
        ListString users = new ListString();
        findWorkTime(taskId, null, null, null,null,null, null).forEach(workTime ->  users.getList().add(workTime.getUserName()));
        return  users;
    }
    private void validWorkTime(WorkTime workTime){
        if (workTime.getTaskId() == null){
            throw new ResourceNotFoundException("Не выбрана задача");
        }
        if (workTime.getWorkDate() == null){
            throw new ResourceNotFoundException("Не задана дата");
        }
        if (workTime.getWorkTime() == null){
            throw new ResourceNotFoundException("Не задано время");
        }
        if (workTime.getWorkTime() <= 0){
            throw new ResourceNotFoundException("Время должно быть больше нуля");
        }
        if (workTime.getUserName() == null){
            throw new ResourceNotFoundException("Не удалось вас опознать пожалуста авторизуйтесь");
        }
        if (workTime.getComment() == null || workTime.getComment().equals("")){
            throw new ResourceNotFoundException("Не удалось вас опознать пожалуста авторизуйтесь");
        }
    }
}
