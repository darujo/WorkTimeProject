package ru.darujo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.darujo.dto.*;
import ru.darujo.dto.calendar.VacationDto;
import ru.darujo.dto.calendar.WeekWorkDto;
import ru.darujo.dto.user.UserDto;
import ru.darujo.dto.workperiod.UserWorkDto;
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

    public float getTimeWork(Long[] taskId, String nikName, Date dateGt, Date dateLe, String typeStr) {
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

    public ListString getFactUser(Long[] taskId, Date dateLe) {
        ListString users = new ListString();
        workTimeService.findWorkTime(taskId, null, null, dateLe, null, null, null, null, null, null).forEach(workTime -> users.getList().add(workTime.getNikName()));
        return users;
    }

    public List<UserWorkDto> getWeekWork(Long[] taskId, String nikName, boolean addTotal, boolean weekSplit, Timestamp dateStart, Timestamp dateEnd) {
        List<UserWorkDto> userWorkDTOs = new ArrayList<>();
        List<WeekWorkDto> weekWorkDTOs;
        if (taskId != null) {
            weekWorkDTOs = new ArrayList<>();
            weekWorkDTOs.add(new WeekWorkDto(null, null, null,null));
        } else if (weekSplit) {
            weekWorkDTOs = calendarServiceIntegration.getWeekTime(dateStart, dateEnd);
        } else {
            weekWorkDTOs = new ArrayList<>();
            weekWorkDTOs.add(new WeekWorkDto(dateStart, dateEnd, calendarServiceIntegration.getWorkTime(dateStart, dateEnd), null));
        }
        Map<Long, Integer> tasks = new HashMap<>();
        weekWorkDTOs
                .forEach(weekWorkDto -> {
                    LinkedHashMap<String, UserWorkDto> userWorkDtoMap = new LinkedHashMap<>();
                    UserWorkDto userWorkDtoTotal = null;
                    if (addTotal) {
                        userWorkDtoTotal = new UserWorkDto(
                                null,
                                "Итого",
                                null,
                                null,
                                weekWorkDto.getDayStart(),
                                weekWorkDto.getDayEnd(),
                                weekWorkDto.getTime());
                        userWorkDtoMap.put(userWorkDtoTotal.getNikName(), userWorkDtoTotal);
                    }
                    UserWorkDto finalUserWorkDtoTotal = userWorkDtoTotal;
                    workTimeService.findWorkTime(taskId, nikName, null, weekWorkDto.getDayEnd(), null, weekWorkDto.getDayStart(), null, null, null, null)
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
                                userWorkDto.addTask(type, workTime.getTaskId());
                                if (addTotal) {
                                    finalUserWorkDtoTotal.addTime(type, workTime.getWorkTime());
                                    finalUserWorkDtoTotal.addTask(type, workTime.getTaskId());
                                }

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
                    if (addTotal) {
                        userWorkDtoTotal.setUserCol(userWorkDtoMap.size());
                    } else if (userWorkDto.getNikName() != null) {

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
            weekWorkDTOs.add(new WeekWorkDto(dateStart, dateEnd, calendarServiceIntegration.getWorkTime(dateStart, dateEnd), null));
        }
        List<UserDto> userDTOs;

        if (nikName == null || nikName.equalsIgnoreCase("all")) {
            userDTOs = workTimeService.getUsers(null);
        } else {
            userDTOs = workTimeService.getUsers(nikName);
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

                        // добавим работы за период
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
                        // установим потраченое время
                        workTimeDto.setWorkTime(timeFactOne.get());
                        // добавим период и работы также устновим был ли отпуск
                        WorkPeriodDto workPeriodDto = new WorkPeriodDto(weekWorkDto, workTimeDtoList);
                        addVacation(user.getNikName(), workPeriodDto);
                        if(workPeriodDto.getAllVacation() == null || !workPeriodDto.getAllVacation()) {
                            timePlan.set(timePlan.get() + weekWorkDto.getTime());
                        }
                        weekWorkPeriodDTOs.add(workPeriodDto);
                    });
            WorkTimeDto workTimeDto = new WorkTimeDto(null, null, null, null, null, timeFact + " из " + timePlan, null);
            List<WorkTimeDto> workTimeDTOs = new ArrayList<>();
            workTimeDTOs.add(workTimeDto);
            // Добавим итог
            // TODO может зря убрал 8 часов
            WorkPeriodDto workPeriodDto = new WorkPeriodDto(null, null, null, null, workTimeDTOs);
            weekWorkPeriodDTOs.add(workPeriodDto);
            UserWorkPeriodDto userWorkPeriodDto = new UserWorkPeriodDto(user.getNikName(), weekWorkPeriodDTOs);
            workTimeService.updFio(userWorkPeriodDto);
            userWeekWorkPeriodDTOs.add(userWorkPeriodDto);
        }
        return userWeekWorkPeriodDTOs;
    }

    private void addVacation(String nikName, WorkPeriodDto workPeriodDto) {
        try {
            List<VacationDto> vacationDTOs = calendarServiceIntegration.getVacation(nikName, workPeriodDto.getDayStart(), workPeriodDto.getDayEnd());
            if (vacationDTOs.size() == 0) {
                return;
            }
            if (vacationDTOs.size() == 1 && vacationDTOs.get(0).getDateStart().compareTo(workPeriodDto.getDayStart()) <= 0 && vacationDTOs.get(0).getDateEnd().compareTo(workPeriodDto.getDayEnd()) >= 0) {
                workPeriodDto.setAllVacation(true);

            } else {
                workPeriodDto.setShotVacation(true);
            }
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
