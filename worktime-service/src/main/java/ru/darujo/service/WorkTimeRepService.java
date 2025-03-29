package ru.darujo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.darujo.dto.*;
import ru.darujo.dto.calendar.WeekWorkDto;
import ru.darujo.integration.CalendarServiceIntegration;
import ru.darujo.integration.TaskServiceIntegration;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

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

    public ListString getFactUser(Long taskId) {
        ListString users = new ListString();
        workTimeService.findWorkTime(taskId, null, null, null, null, null, null, null, null, null).forEach(workTime -> users.getList().add(workTime.getNikName()));
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
                    }
//                        UserWorkDto userWorkDto1 = userWorkDtoMap.entrySet().stream().findFirst().map(stringUserWorkDtoEntry -> stringUserWorkDtoEntry.getValue());
//                    (s, userWorkDto1) -> {userWorkDto1.setUserCol(userWorkDtoMap.size();));
//                        userWorkDto[0].setUserCol(userWorkDtoMap.size());
                    else {
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
}
