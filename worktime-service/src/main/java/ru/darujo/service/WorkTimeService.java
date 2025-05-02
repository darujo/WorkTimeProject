package ru.darujo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.darujo.convertor.WorkTimeConvertor;
import ru.darujo.dto.*;
import ru.darujo.dto.user.UserFio;
import ru.darujo.dto.user.UserDto;
import ru.darujo.exceptions.ResourceNotFoundException;
import ru.darujo.integration.CalendarServiceIntegration;
import ru.darujo.integration.TaskServiceIntegration;
import ru.darujo.integration.UserServiceIntegration;
import ru.darujo.model.WorkTime;
import ru.darujo.repository.WorkTimeRepository;
import ru.darujo.repository.specifications.WorkTimeSpecifications;

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

    public WorkTime saveWorkTime(WorkTime workTime, boolean check) {
        if (check) {
            validWorkTime(workTime);
        }

        Boolean ok = taskServiceIntegration.setTaskRefreshTime(workTime.getTaskId());
        System.out.println("обновили время у задачи " + ok);

        return workTimeRepository.save(workTime);
    }

    public void deleteWorkTime(Long id) {
        workTimeRepository.deleteById(id);
    }

    public Iterable<WorkTime> findWorkTime(Long[] taskId, String nikName, Date dateLt, Date dateLe, Date dateGT, Date dateGE, Integer type, String comment, Integer page, Integer size) {
        Specification<WorkTime> specification = Specification.where(null);
        Sort sort = null;
        if (taskId != null) {
            specification = WorkTimeSpecifications.in(specification, "taskId", taskId);
            if (taskId.length == 1) {
                sort = Sort.by("taskId");
            }
        }
        if (nikName != null) {
            try {
                List<String> users = Objects.requireNonNull(getUsers(nikName)).stream().map(UserDto::getNikName).collect(Collectors.toList());
                specification = WorkTimeSpecifications.in(specification, "nikName", users);
                if (users.size() == 1) {
                    if (sort == null)
                        sort = Sort.by("nikName");
                    else {
                        sort.and(Sort.by("nikName"));
                    }
                }

            }
            catch (NullPointerException ex) {
                throw new ResourceNotFoundException("Нет пользователей с Ролью или Логином " + nikName);
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
        if (comment != null && !comment.equals("")) {
            specification = specification.and(WorkTimeSpecifications.like("comment", comment));
        }
        if (sort == null)
            sort = Sort.by(Sort.Direction.DESC, "workDate");
        else {
            sort = sort.and(Sort.by(Sort.Direction.DESC, "workDate"));
        }
        if (page == null) {
            return workTimeRepository.findAll(specification, sort);

        } else {
            return workTimeRepository.findAll(specification, PageRequest.of(page - 1, size, sort));
        }
    }

    public List<WorkTime> findWorkTimeTask(String taskDEVBO, String taskBts, String nikName, Date dateLt, Date dateLe, Date dateGT, Date dateGE, Integer type, String comment) {
        List<WorkTime> workTimes = new ArrayList<>();
        List<Long> taskIdList = taskServiceIntegration.getTaskList(taskDEVBO, taskBts);
        if (taskIdList == null) {
            return null;
        }

        findWorkTime(taskIdList.toArray(new Long[0]), nikName, dateLt, dateLe, dateGT, dateGE, type, comment, null, null)
                .forEach(workTimes::add)
        ;
        return workTimes;
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
        if (workTime.getComment() == null || workTime.getComment().isEmpty()) {
            throw new ResourceNotFoundException("Не задан комментарий");
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
        } catch (ResourceNotFoundException e) {
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
        } catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
            userFio.setFirstName("Не найден пользователь с ником " + userFio.getNikName());
        }
    }

    public List<UserDto> getUsers(String role) {
        try {
            return userServiceIntegration.getUserDTOs(role);
        } catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Boolean getAvailTime(long taskId) {
        Specification<WorkTime> specification = Specification.where(WorkTimeSpecifications.taskIdEQ(taskId));
        return workTimeRepository.findAll(specification).size() > 0;
    }

    public boolean checkRight(String right, boolean rightEdit, boolean rightCreate, boolean rightChangeUser) {
        right = right.toLowerCase();
        switch (right) {
            case "edit":
                if (!rightEdit) {
                    throw new ResourceNotFoundException("У вас нет права на редактирование WORK_TIME_EDIT");
                }
                break;
            case "create":
                if (!rightCreate) {
                    throw new ResourceNotFoundException("У вас нет права на создание WORK_TIME_CREATE");
                }
                break;
            case "changeuser":
                if (!rightChangeUser) {
                    throw new ResourceNotFoundException("У вас нет права на изменение пользователя WORK_TIME_CHANGE_USER");
                }
                break;
        }
        return true;
    }
}
