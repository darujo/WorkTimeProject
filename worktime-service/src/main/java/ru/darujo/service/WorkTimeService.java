package ru.darujo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.darujo.convertor.WorkTimeConvertor;
import ru.darujo.dto.*;
import ru.darujo.dto.UserFio;
import ru.darujo.dto.calendar.WeekWorkDto;
import ru.darujo.exceptions.ResourceNotFoundException;
import ru.darujo.integration.CalendarServiceIntegration;
import ru.darujo.integration.TaskServiceIntegration;
import ru.darujo.integration.UserServiceIntegration;
import ru.darujo.model.WorkTime;
import ru.darujo.repository.WorkTimeRepository;
import ru.darujo.repository.specifications.WorkTimeSpecifications;

import java.sql.Timestamp;
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
    UserServiceIntegration userServiceIntegration;
    @Autowired
    public void setUserServiceIntegration(UserServiceIntegration userServiceIntegration) {
        this.userServiceIntegration = userServiceIntegration;
    }
    CalendarServiceIntegration calendarServiceIntegration;
    @Autowired
    public void setCalendarServiceIntegration(CalendarServiceIntegration calendarServiceIntegration) {
        this.calendarServiceIntegration = calendarServiceIntegration;
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
        TaskDto taskDto = taskServiceIntegration.getTask(workTime.getTaskId());
        return workTimeRepository.save(workTime);
    }

    public void deleteWorkTime(Long id) {
        workTimeRepository.deleteById(id);
    }

    public Iterable<WorkTime> findWorkTime(Long taskId, String nikName, Date dateLt,Date dateLe, Date dateGT, Date dateGE, Integer page, Integer size) {
        Specification<WorkTime> specification = Specification.where(null);
        if (dateLt != null) {
            specification = specification.and(WorkTimeSpecifications.dateLt(dateLt));
        }
        if (dateLe != null) {
            specification = specification.and(WorkTimeSpecifications.dateLe(dateLe));
        }
        if (dateGE != null) {
            specification = specification.and(WorkTimeSpecifications.dateGE(dateGE));
        }
        if (dateGT != null) {
            specification = specification.and(WorkTimeSpecifications.dateGT(dateGT));
        }
        if (nikName != null) {
            specification = specification.and(WorkTimeSpecifications.userNikNameEQ(nikName));
        }
        if (taskId != null) {
            specification = specification.and(WorkTimeSpecifications.taskIdEQ(taskId));
        }
        if (page == null){
            return workTimeRepository.findAll(specification);

        }
        else {

            return workTimeRepository.findAll(specification, PageRequest.of(page - 1, size,Sort.by("workDate").and(Sort.by("nikName"))));
        }
    }
    public float getTimeWork(Long taskId, String nikName,Date dateGt,Date dateLe){
        AtomicReference<Float> time = new AtomicReference<>((float) 0);
        findWorkTime(taskId, nikName, null,dateLe,dateGt,null, null,null).forEach(workTime -> time.set(time.get() + workTime.getWorkTime()));
        return time.get();
    }

    public ListString getFactUser(Long taskId) {
        ListString users = new ListString();
        findWorkTime(taskId, null, null, null,null,null, null,null).forEach(workTime ->  users.getList().add(workTime.getNikName()));
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
        if (workTime.getNikName() == null){
            throw new ResourceNotFoundException("Не удалось вас опознать пожалуста авторизуйтесь");
        }
        if (workTime.getComment() == null || workTime.getComment().equals("")){
            throw new ResourceNotFoundException("Не задан комментарий");
        }
    }

    public List<UserWorkDto> getWeekWork(String nikName, Timestamp dateStart,Timestamp dateEnd) {
        List<UserWorkDto> userWorkDtos =new ArrayList<>();
        List<WeekWorkDto> weekWorkDtos =calendarServiceIntegration.getWeekTime(dateStart,dateEnd);

        Map<Long,Integer> tasks = new HashMap<>();
        weekWorkDtos
                .forEach(weekWorkDto -> {
                    Map<String,UserWorkDto> userWorkDtoMap = new HashMap<>();
                    final UserWorkDto[] userWorkDto = {null};
                    findWorkTime(null, nikName,null,weekWorkDto.getDayEnd(),null,weekWorkDto.getDayStart(),null,null)
                            .forEach(workTime -> {
                                Integer type = tasks.get(workTime.getTaskId());
                                if(type==null){
                                    TaskDto taskDto = taskServiceIntegration.getTask(workTime.getTaskId());
                                    tasks.put(taskDto.getId(),taskDto.getType());
                                }
                                userWorkDto[0] = userWorkDtoMap.get(workTime.getNikName());
                                if (userWorkDto[0] == null){
                                    userWorkDto[0] =new UserWorkDto(
                                            workTime.getNikName(),
                                            null,
                                            null,
                                            null,
                                            weekWorkDto.getDayStart(),
                                            weekWorkDto.getDayEnd(),
                                            weekWorkDto.getTime());
                                    userWorkDtoMap.put(workTime.getNikName(), userWorkDto[0]);

                                }
                                userWorkDto[0].addTime(type,workTime.getWorkTime());

                            });
                    if(userWorkDto[0] != null) {
                        userWorkDto[0].setUserCol(userWorkDtoMap.size());
                    }else {
                        userWorkDto[0] = new UserWorkDto(
                                null,
                                null,
                                null,
                                null,
                                weekWorkDto.getDayStart(),
                                weekWorkDto.getDayEnd(),
                                0f);
                        userWorkDto[0].setUserCol(1);
                        userWorkDtoMap.put("", userWorkDto[0]);
                    }
                    userWorkDtoMap.forEach((nik, userWork) ->{
                        updFio(userWork);
                            userWorkDtos.add(
                            userWork.addTimeAll());});
        });
        return userWorkDtos;

    }

    public WorkTimeDto getWorkTimeDtoAndUpd(WorkTime workTime){
        WorkTimeDto workTimeDto = WorkTimeConvertor.getWorkTimeDto(workTime);
        updFio(workTimeDto);

        return workTimeDto;
    }
    private void updFio(UserFio userFio){
        try {
            // TODO Сделать кеширование
            if(userFio.getNikName()!=null) {
                UserDto userDto = userServiceIntegration.getUserDto(null, userFio.getNikName());
                userFio.setAuthorFirstName(userDto.getFirstName());
                userFio.setAuthorLastName(userDto.getLastName());
                userFio.setAuthorPatronymic(userDto.getPatronymic());
            }
        } catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
            userFio.setAuthorFirstName("Не найден пользователь с ником " + userFio.getNikName());
        }
    }
}
