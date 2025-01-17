package ru.darujo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.darujo.convertor.WorkTimeConvertor;
import ru.darujo.dto.*;
import ru.darujo.dto.user.UserFio;
import ru.darujo.dto.user.UserDto;
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.integration.CalendarServiceIntegration;
import ru.darujo.integration.TaskServiceIntegration;
import ru.darujo.integration.UserServiceIntegration;
import ru.darujo.model.WorkTime;
import ru.darujo.repository.WorkTimeRepository;
import ru.darujo.specifications.Specifications;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

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

    @Transactional
    public WorkTime saveWorkTime(WorkTime workTime, boolean check) {
        if (check) {
            validWorkTime(workTime);
        }

        Boolean ok;
        if (workTime.getType() == 1 || workTime.getType() == 4) {
            ok = taskServiceIntegration.setTaskRefreshTime(workTime.getTaskId(), workTime.getWorkDate());
        } else {
            ok = taskServiceIntegration.setTaskRefreshTime(workTime.getTaskId());

        }
        System.out.println("обновили время у задачи " + ok);

        return workTimeRepository.save(workTime);
    }

    public void deleteWorkTime(Long id) {
        workTimeRepository.deleteById(id);
    }

    public Page<WorkTime> findWorkTime(Long[] taskId, String nikName, Date dateLt, Date dateLe, Date dateGT, Date dateGE, Integer type, String comment, Integer page, Integer size) {
        Specification<WorkTime> specification = Specification.where(null);
        Sort sort = null;
        if (taskId != null) {
            specification = Specifications.in(specification, "taskId", taskId);
            if (taskId.length == 1) {
                sort = Sort.by("taskId");
            }
        }
        if (nikName != null) {
            try {
                List<String> users = Objects.requireNonNull(getUsers(nikName)).stream().map(UserDto::getNikName).collect(Collectors.toList());
                specification = Specifications.in(specification, "nikName", users);
                if (users.size() == 1) {
                    if (sort == null)
                        sort = Sort.by("nikName");
                    else {
                        sort.and(Sort.by("nikName"));
                    }
                }

            } catch (NullPointerException ex) {
                throw new ResourceNotFoundRunTime("Нет пользователей с Ролью или Логином " + nikName);
            }
        }
        specification = Specifications.lt(specification, "workDate", dateLt);
        specification = Specifications.le(specification, "workDate", dateLe);
        specification = Specifications.ge(specification, "workDate", dateGE);
        specification = Specifications.gt(specification, "workDate", dateGT);
        specification = Specifications.eq(specification, "type", type);
        specification = Specifications.like(specification, "comment", comment);
        if (sort == null)
            sort = Sort.by(Sort.Direction.DESC, "workDate");
        else {
            sort = sort.and(Sort.by(Sort.Direction.DESC, "workDate"));
        }
        if (page == null) {
            return new PageImpl<>(workTimeRepository.findAll(specification, sort));

        } else {
            return workTimeRepository.findAll(specification, PageRequest.of(page - 1, size, sort));
        }
    }

    public Page<WorkTime> findWorkTimeTask(String taskDEVBO, String taskBts, String nikName, Date dateLt, Date dateLe, Date dateGT, Date dateGE, Integer type, String comment, Integer page, Integer size) {
        Page<WorkTime> workTimes;
        List<Long> taskIdList = taskServiceIntegration.getTaskList(taskDEVBO, taskBts);
        if (taskIdList == null || taskIdList.size() == 0) {
            return new PageImpl<>(new ArrayList<>());
        }

        workTimes = findWorkTime(taskIdList.toArray(new Long[0]), nikName, dateLt, dateLe, dateGT, dateGE, type, comment, page, size)

        ;
        return workTimes;
    }

    private void validWorkTime(WorkTime workTime) {
        if (workTime.getTaskId() == null) {
            throw new ResourceNotFoundRunTime("Не выбрана задача");
        }
        if (workTime.getWorkDate() == null) {
            throw new ResourceNotFoundRunTime("Не задана дата");
        }
        if (workTime.getWorkTime() == null) {
            throw new ResourceNotFoundRunTime("Время должно быть от 0 до 10 часов");
        }
        if (workTime.getWorkTime() <= 0) {
            throw new ResourceNotFoundRunTime("Время должно быть больше нуля");
        }
        if (workTime.getNikName() == null) {
            throw new ResourceNotFoundRunTime("Не удалось вас опознать пожалуста авторизуйтесь");
        }
        if (workTime.getComment() == null || workTime.getComment().isEmpty()) {
            throw new ResourceNotFoundRunTime("Не задан комментарий");
        }
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
        } catch (ResourceNotFoundRunTime e) {
            System.out.println(e.getMessage());
            workTimeDto.setFirstName("Не найдена задача с id " + workTimeDto.getTaskId());
        }
    }

    private final Map<String, UserDto> userDtoMap = new HashMap<>();

    public void updFio(UserFio userFio) {
        try {
            if (userFio.getNikName() != null) {
                UserDto userDto = userDtoMap.get(userFio.getNikName());
                if (userDto == null) {
                    userDto = userServiceIntegration.getUserDto(null, userFio.getNikName());
                    userDtoMap.put(userFio.getNikName(), userDto);
                }
                userFio.setFirstName(userDto.getFirstName());
                userFio.setLastName(userDto.getLastName());
                userFio.setPatronymic(userDto.getPatronymic());
            }
        } catch (ResourceNotFoundRunTime e) {
            System.out.println(e.getMessage());
            userFio.setFirstName("Не найден пользователь с ником " + userFio.getNikName());
        }
    }

    public List<UserDto> getUsers(String role) {
        try {
            return userServiceIntegration.getUserDTOs(role);
        } catch (ResourceNotFoundRunTime e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Boolean getAvailTime(long taskId) {
        Specification<WorkTime> specification = Specification.where(Specifications.eq(null,"taskId",taskId));
        return workTimeRepository.findAll(specification).size() > 0;
    }

    public boolean checkRight(String right, boolean rightEdit, boolean rightCreate, boolean rightChangeUser) {
        right = right.toLowerCase();
        switch (right) {
            case "edit":
                if (!rightEdit) {
                    throw new ResourceNotFoundRunTime("У вас нет права на редактирование WORK_TIME_EDIT");
                }
                break;
            case "create":
                if (!rightCreate) {
                    throw new ResourceNotFoundRunTime("У вас нет права на создание WORK_TIME_CREATE");
                }
                break;
            case "changeuser":
                if (!rightChangeUser) {
                    throw new ResourceNotFoundRunTime("У вас нет права на изменение пользователя WORK_TIME_CHANGE_USER");
                }
                break;
        }
        return true;
    }

    public Timestamp getLastTime(Long [] taskId) {
        Page<WorkTime> workTimes = findWorkTime(taskId,null,null,null,null,null,null,null,1,1);

        return workTimes.getSize() == 1 ? workTimes.getContent().get(0).getWorkDate() : null ;
    }
}
