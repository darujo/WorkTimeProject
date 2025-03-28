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
        return saveWorkTime(workTime, true);
    }

    public WorkTime saveWorkTime(WorkTime workTime, boolean check) {
        if (check) {
            validWorkTime(workTime);
        }

        Boolean ok = taskServiceIntegration.setTaskRefreshTime(workTime.getTaskId());
        System.out.println("обновили время у задачи " + ok );

        return workTimeRepository.save(workTime);
    }

    public void deleteWorkTime(Long id) {
        workTimeRepository.deleteById(id);
    }

    public Iterable<WorkTime> findWorkTime(Long taskId, String nikName, Date dateLt, Date dateLe, Date dateGT, Date dateGE, Integer type,String comment, Integer page, Integer size) {
        Specification<WorkTime> specification = Specification.where(null);
        Sort sort = null;
        if (taskId != null) {
            specification = specification.and(WorkTimeSpecifications.taskIdEQ(taskId));
//            if (sort == null)
            sort = Sort.by("taskId");
//            else{
//              sort.and(Sort.by("taskId"));
//            }
        }
        if (nikName != null) {
            specification = specification.and(WorkTimeSpecifications.userNikNameEQ(nikName));
            if (sort == null)
                sort = Sort.by("nikName");
            else {
                sort.and(Sort.by("nikName"));
            }
        }
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
        if (type != null) {
            specification = specification.and(WorkTimeSpecifications.typeEq(type));
        }
        if(comment != null && !comment.equals("")){
            specification = specification.and(WorkTimeSpecifications.like("comment",comment));
        }
        if (page == null) {
            return workTimeRepository.findAll(specification);

        } else {
            if (sort == null)
                sort = Sort.by(Sort.Direction.DESC, "workDate");
            else {
                sort = sort.and(Sort.by(Sort.Direction.DESC, "workDate"));
            }

            return workTimeRepository.findAll(specification, PageRequest.of(page - 1, size, sort));
        }
    }

    public List<WorkTime> findWorkTimeTask(String taskDEVBO, String taskBts, String nikName, Date dateLt, Date dateLe, Date dateGT, Date dateGE) {
        List<WorkTime> workTimes = new ArrayList<>();
        List<Long> taskIdList = taskServiceIntegration.getTaskList(taskDEVBO, taskBts);
        taskIdList.forEach(taskId ->
                findWorkTime(taskId, nikName, dateLt, dateLe, dateGT, dateGE, null, null,null, null)
                        .forEach(workTimes::add)
        );
        return workTimes;
    }

    public float getTimeWork(Long taskId, String nikName, Date dateGt, Date dateLe, String typeStr) {
        ArrayList<Integer> types = new ArrayList<>();

        if (typeStr!= null && typeStr.equals("analise")) {
            types.add(2);
            types.add(3);

        } else if (typeStr!= null && typeStr.equals("develop")) {
            types.add(1);
            types.add(4);
        } else {
            types.add(null);
        }

        AtomicReference<Float> time = new AtomicReference<>((float) 0);
        for (Integer type : types) {

            findWorkTime(taskId, nikName, null, dateLe, dateGt, null, type, null, null,null).

                    forEach(workTime ->
                            time.set(time.get() + workTime.getWorkTime())
                    );
        }
        return time.get();
    }

    public ListString getFactUser(Long taskId) {
        ListString users = new ListString();
        findWorkTime(taskId, null, null, null, null, null, null, null,null,null).forEach(workTime -> users.getList().add(workTime.getNikName()));
        return users;
    }

    private void validWorkTime(WorkTime workTime) {
        if (workTime.getTaskId() == null) {
            throw new ResourceNotFoundException("Не выбрана задача");
        }
        if (workTime.getWorkDate() == null) {
            throw new ResourceNotFoundException("Не задана дата");
        }
        if (workTime.getWorkTime() == null) {
            throw new ResourceNotFoundException("Время должно быть от 0 до 10 часов");
        }
        if (workTime.getWorkTime() <= 0) {
            throw new ResourceNotFoundException("Время должно быть больше нуля");
        }
        if (workTime.getNikName() == null) {
            throw new ResourceNotFoundException("Не удалось вас опознать пожалуста авторизуйтесь");
        }
        if (workTime.getComment() == null || workTime.getComment().equals("")) {
            throw new ResourceNotFoundException("Не задан комментарий");
        }
    }

    public List<UserWorkDto> getWeekWork(String nikName, boolean weekSplit, Timestamp dateStart, Timestamp dateEnd) {
        List<UserWorkDto> userWorkDTOs = new ArrayList<>();
        List<WeekWorkDto> weekWorkDTOs;
        if (weekSplit) {
            weekWorkDTOs = calendarServiceIntegration.getWeekTime(dateStart, dateEnd);
        } else {
            weekWorkDTOs = new ArrayList<>();
            weekWorkDTOs.add(new WeekWorkDto(dateStart, dateEnd, calendarServiceIntegration.getWorkTime(dateStart, dateEnd)));
        }
        Map<Long, Integer> tasks = new HashMap<>();
        weekWorkDTOs
                .forEach(weekWorkDto -> {
                    Map<String, UserWorkDto> userWorkDtoMap = new HashMap<>();
                    findWorkTime(null, nikName, null, weekWorkDto.getDayEnd(), null, weekWorkDto.getDayStart(), null, null,null,null)
                            .forEach(workTime -> {
                                Integer type = tasks.get(workTime.getTaskId());
                                if (type == null) {
                                    TaskDto taskDto = taskServiceIntegration.getTask(workTime.getTaskId());
                                    tasks.put(taskDto.getId(), taskDto.getType());
                                    type = taskDto.getType();
                                }
                                UserWorkDto userWorkDto = userWorkDtoMap.get(workTime.getNikName());
                                if (userWorkDto == null) {
                                    userWorkDto = new UserWorkDto(
                                            workTime.getNikName(),
                                            null,
                                            null,
                                            null,
                                            weekWorkDto.getDayStart(),
                                            weekWorkDto.getDayEnd(),
                                            weekWorkDto.getTime());
                                    userWorkDtoMap.put(workTime.getNikName(), userWorkDto);


                                }
                                userWorkDto.addTime(type, workTime.getWorkTime());

                            });
                    UserWorkDto userWorkDto = userWorkDtoMap.values().stream().findFirst().orElse(
                            new UserWorkDto(
                                    null,
                                    null,
                                    null,
                                    null,
                                    weekWorkDto.getDayStart(),
                                    weekWorkDto.getDayEnd(),
                                    weekWorkDto.getTime()));

                    if (userWorkDto.getNikName() != null) {

                        userWorkDto.setUserCol(userWorkDtoMap.size());
                    }
//                        UserWorkDto userWorkDto1 = userWorkDtoMap.entrySet().stream().findFirst().map(stringUserWorkDtoEntry -> stringUserWorkDtoEntry.getValue());
//                    (s, userWorkDto1) -> {userWorkDto1.setUserCol(userWorkDtoMap.size();));
//                        userWorkDto[0].setUserCol(userWorkDtoMap.size());
                    else {
                        userWorkDto.setUserCol(1);
                        userWorkDtoMap.put("", userWorkDto);
                    }
                    userWorkDtoMap.forEach((nik, userWork) -> {
                        updFio(userWork);
                        userWorkDTOs.add(
                                userWork.addTimeAll());
                    });
                });
        return userWorkDTOs;

    }

    public WorkTimeDto getWorkTimeDtoAndUpd(WorkTime workTime) {
        WorkTimeDto workTimeDto = WorkTimeConvertor.getWorkTimeDto(workTime);
        updFio(workTimeDto);
        updTask(workTimeDto);

        return workTimeDto;
    }

    private final Map<Long, TaskDto> taskDtoMap = new HashMap<>();

    public void clearCash() {
        taskDtoMap.clear();
    }

    private void updTask(WorkTimeDto workTimeDto) {
        try {
            if (workTimeDto.getTaskId() != null) {
                TaskDto taskDto = taskDtoMap.get(workTimeDto.getTaskId());
                if (taskDto == null) {
                    taskDto = taskServiceIntegration.getTask(workTimeDto.getTaskId());
                    taskDtoMap.put(taskDto.getId(), taskDto);
                }
                workTimeDto.setTaskDescription(taskDto.getDescription());
                workTimeDto.setTaskCodeBTS(taskDto.getCodeBTS());
                workTimeDto.setTaskCodeDEVBO(taskDto.getCodeDEVBO());
                workTimeDto.setTaskType(taskDto.getType());
            }
        } catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
            workTimeDto.setAuthorFirstName("Не найдена задача с id " + workTimeDto.getTaskId());
        }
    }

    private final Map<String, UserDto> userDtoMap = new HashMap<>();

    private void updFio(UserFio userFio) {
        try {
            if (userFio.getNikName() != null) {
                UserDto userDto = userDtoMap.get(userFio.getNikName());
                if (userDto == null) {
                    userDto = userServiceIntegration.getUserDto(null, userFio.getNikName());
                    userDtoMap.put(userFio.getNikName(), userDto);
                }
                userFio.setAuthorFirstName(userDto.getFirstName());
                userFio.setAuthorLastName(userDto.getLastName());
                userFio.setAuthorPatronymic(userDto.getPatronymic());
            }
        } catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
            userFio.setAuthorFirstName("Не найден пользователь с ником " + userFio.getNikName());
        }
    }

    public Boolean getAvailTime(long taskId) {
        Specification<WorkTime> specification = Specification.where(null);
        specification = specification.and(WorkTimeSpecifications.taskIdEQ(taskId));
        return workTimeRepository.findAll(specification).size() > 0;
    }
}
