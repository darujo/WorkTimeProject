package ru.darujo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.darujo.dto.*;
import ru.darujo.dto.calendar.WeekWorkDto;
import ru.darujo.dto.workrep.UserWorkPeriodDto;
import ru.darujo.dto.workrep.WorkPeriodDto;
import ru.darujo.integration.CalendarServiceIntegration;
import ru.darujo.integration.TaskServiceIntegration;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Primary
public class WorkTimeRepService {
    private TaskServiceIntegration taskServiceIntegration;

    @Autowired
    public void setWorkServiceIntegration(TaskServiceIntegration taskServiceIntegration) {
        this.taskServiceIntegration = taskServiceIntegration;
    }

    WorkTimeService workTimeService;

    @Autowired
    public void setWorkTimeService(WorkTimeService workTimeService) {
        this.workTimeService = workTimeService;
    }

    CalendarServiceIntegration calendarServiceIntegration;

    @Autowired
    public void setCalendarServiceIntegration(CalendarServiceIntegration calendarServiceIntegration) {
        this.calendarServiceIntegration = calendarServiceIntegration;
    }

    public float getTimeWork(Long taskId, String nikName, Date dateGt, Date dateLe, String typeStr) {
        ArrayList<Integer> types = new ArrayList<>();

        if (typeStr != null && typeStr.equals("analise")) {
            types.add(2);
            types.add(3);

        } else if (typeStr != null && typeStr.equals("develop")) {
            types.add(1);
            types.add(4);
        } else {
            types.add(null);
        }

        AtomicReference<Float> time = new AtomicReference<>((float) 0);
        for (Integer type : types) {

            workTimeService.findWorkTime(taskId, nikName, null, dateLe, dateGt, null, type, null, null, null).

                    forEach(workTime ->
                            time.set(time.get() + workTime.getWorkTime())
                    );
        }
        return time.get();
    }

    public ListString getFactUser(Long taskId, Date dateLe) {
        ListString users = new ListString();
        workTimeService.findWorkTime(taskId, null, null, dateLe, null, null, null, null, null, null).forEach(workTime -> users.getList().add(workTime.getNikName()));
        return users;
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
                    workTimeService.findWorkTime(null, nikName, null, weekWorkDto.getDayEnd(), null, weekWorkDto.getDayStart(), null, null, null, null)
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
                    } else {
                        userWorkDto.setUserCol(1);
                        userWorkDtoMap.put("", userWorkDto);
                    }
                    userWorkDtoMap.forEach((nik, userWork) -> {
                        workTimeService.updFio(userWork);
                        userWorkDTOs.add(
                                userWork.addTimeAll());
                    });
                });
        return userWorkDTOs;

    }

    public Boolean getAvailTime(long taskId) {
        return workTimeService.getAvailTime(taskId);
    }

    public List<UserWorkPeriodDto> getUserWork(String nikName, String periodSplit, Timestamp dateStart, Timestamp dateEnd) {
        List<WeekWorkDto> weekWorkDTOs;
        List<UserWorkPeriodDto> userWeekWorkPeriodDTOs = new ArrayList<>();
        if (periodSplit != null) {
            weekWorkDTOs = calendarServiceIntegration.getPeriodTime(dateStart, dateEnd, periodSplit);
        } else {
            weekWorkDTOs = new ArrayList<>();
            weekWorkDTOs.add(new WeekWorkDto(dateStart, dateEnd, calendarServiceIntegration.getWorkTime(dateStart, dateEnd)));
        }
        List<UserDto> userDTOs;

        if (nikName == null) {
            userDTOs = workTimeService.getUsers(null);
        } else if (nikName.substring(0, 5).equalsIgnoreCase("role_")) {
            userDTOs = workTimeService.getUsers(nikName.substring(5));
        } else {
            userDTOs = new ArrayList<>();
            userDTOs.add(new UserDto(nikName));
        }
        if (userDTOs == null || userDTOs.size() == 0) {

            List<WorkPeriodDto> weekWorkPeriodDTOs = weekWorkDTOs.stream().map(weekWorkDto -> new WorkPeriodDto(weekWorkDto, null)).collect(Collectors.toList());
            userWeekWorkPeriodDTOs.add(new UserWorkPeriodDto("", weekWorkPeriodDTOs));
            return userWeekWorkPeriodDTOs;
        }
        for (UserDto user : userDTOs) {
            List<WorkPeriodDto> weekWorkPeriodDTOs = new ArrayList<>();

            AtomicReference<Float> timePlan = new AtomicReference<>(0f);
            AtomicReference<Float> timeFact = new AtomicReference<>(0f);
            weekWorkDTOs
                    .forEach(weekWorkDto -> {
                        List<WorkTimeDto> workTimeDtoList = new ArrayList<>();
                        WorkTimeDto workTimeDto = new WorkTimeDto();
                        workTimeDtoList.add(workTimeDto);
                        AtomicReference<Float> timeFactOne = new AtomicReference<>(0f);
                        timePlan.set(timePlan.get() + weekWorkDto.getTime());
                        workTimeService.findWorkTime(
                                        null,
                                        user.getNikName(),
                                        null,
                                        weekWorkDto.getDayEnd(),
                                        null,
                                        weekWorkDto.getDayStart(),
                                        null,
                                        null,
                                        null,
                                        null)
                                .forEach(workTime -> {
                                    timeFact.set(timeFact.get() + workTime.getWorkTime());
                                    timeFactOne.set(timeFactOne.get() + workTime.getWorkTime());
                                    workTimeDtoList.add(workTimeService.getWorkTimeDtoAndUpd(workTime));
                                });
                        workTimeDto.setWorkTime(timeFactOne.get());
                        weekWorkPeriodDTOs.add(new WorkPeriodDto(weekWorkDto, workTimeDtoList));
                    });
            WorkTimeDto workTimeDto = new WorkTimeDto(null, null, null, null, null, timeFact + " из " + timePlan, null);
            List<WorkTimeDto> workTimeDTOs = new ArrayList<>();
            workTimeDTOs.add(workTimeDto);
            // Добавим итог
            WorkPeriodDto workPeriodDto = new WorkPeriodDto(null, null, 8f, workTimeDTOs);
            weekWorkPeriodDTOs.add(workPeriodDto);
            UserWorkPeriodDto userWorkPeriodDto = new UserWorkPeriodDto(user.getNikName(), weekWorkPeriodDTOs);
            workTimeService.updFio(userWorkPeriodDto);
            userWeekWorkPeriodDTOs.add(userWorkPeriodDto);
        }
        return userWeekWorkPeriodDTOs;
    }
}
