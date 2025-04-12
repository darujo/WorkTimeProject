package ru.darujo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.darujo.dto.MapStringFloat;
import ru.darujo.dto.PageDto;
import ru.darujo.dto.UserDto;
import ru.darujo.dto.workperiod.WorkUserTime;
import ru.darujo.dto.workrep.WorkFactDto;
import ru.darujo.dto.workrep.WorkRepDto;
import ru.darujo.exceptions.ResourceNotFoundException;
import ru.darujo.integration.TaskServiceIntegration;
import ru.darujo.integration.UserServiceIntegration;
import ru.darujo.integration.WorkTimeServiceIntegration;
import ru.darujo.model.Work;
import ru.darujo.model.WorkLittle;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class WorkRepService {
    private TaskServiceIntegration taskServiceIntegration;

    @Autowired
    public void setTaskServiceIntegration(TaskServiceIntegration taskServiceIntegration) {
        this.taskServiceIntegration = taskServiceIntegration;
    }

    private WorkTimeServiceIntegration workTimeServiceIntegration;

    @Autowired
    public void setWorkTimeServiceIntegration(WorkTimeServiceIntegration workTimeServiceIntegration) {
        this.workTimeServiceIntegration = workTimeServiceIntegration;
    }

    UserServiceIntegration userServiceIntegration;

    @Autowired
    public void setUserServiceIntegration(UserServiceIntegration userServiceIntegration) {
        this.userServiceIntegration = userServiceIntegration;
    }

    private WorkService workService;

    @Autowired
    public void setWorkService(WorkService workService) {
        this.workService = workService;
    }

    public List<WorkRepDto> getWorkRep(String name, Boolean availWork, Integer stageZiGe, Integer stageZiLe, String release, String[] sort) {
        List<WorkRepDto> workRepDTOs = new ArrayList<>();
        List<Work> works = workService.getWorkList(name, stageZiGe, stageZiLe, release, sort);
        works.forEach(work ->
                {
                    boolean availWorkTime = false;
                    if (availWork != null) {
                        availWorkTime = taskServiceIntegration.availWorkTime(work.getId());
                    }
                    if (availWork == null ||
                            (availWork && availWorkTime) ||
                            (!availWork && !availWorkTime)) {
                        workRepDTOs.add(
                                new WorkRepDto(
                                        work.getId(),
                                        work.getCodeZI(),
                                        work.getName(),
                                        work.getStartTaskPlan(),
                                        work.getStartTaskFact(),
                                        work.getAnaliseEndPlan(),
                                        work.getAnaliseEndFact(),
                                        work.getDevelopEndPlan(),
                                        work.getDevelopEndFact(),
                                        work.getDebugEndPlan(),
                                        work.getDebugEndFact(),
                                        work.getRelease(),
                                        work.getIssuingReleasePlan(),
                                        work.getIssuingReleaseFact(),
                                        work.getReleaseEndPlan(),
                                        work.getReleaseEndFact(),
                                        work.getOpeEndPlan(),
                                        work.getOpeEndFact(),
                                        getFactWork(work, 0),
                                        getFactWork(work, 1),
                                        getFactWork(work, 2),
                                        getFactWork(work, 3),
                                        getFactWork(work, 4),
                                        getFactWork(work, 5)

                                )
                        );
                    }
                }
        );
        workRepDTOs.forEach(workService::updWorkPlanTime);
        return workRepDTOs;
    }


    public PageDto<WorkFactDto> getWorkFactRep(Integer page,
                                               Integer size,
                                               String userName,
                                               String nameZi,
                                               Integer stageZiGe,
                                               Integer stageZiLe,
                                               Long codeSap,
                                               String codeZiSearch,
                                               String task,
                                               String release,
                                               String sort,
                                               boolean hideNotTime) {
        AtomicInteger num = new AtomicInteger();
        List<WorkFactDto> workFactDTOs = new ArrayList<>();
        Page<Work> workPage = workService.findWorks(page, size, nameZi, sort, stageZiGe, stageZiLe, codeSap, codeZiSearch, task, release);
        workPage.forEach(work -> {
                    Set<String> users = taskServiceIntegration.getListUser(work.getId(), null).getList();
                    if (userName != null) {
                        if (users.contains(userName)) {
                            users = new HashSet<>();
                            users.add(userName);
                        } else {
                            users = null;
                        }
                    }
                    if (users != null && users.size() != 0) {
                        List<String> userList = new ArrayList<>(users);
                        for (int i = 0; i < users.size(); i++) {
                            String user = userList.get(i);
                            String codeZi;
                            String name;
                            if (i == 0) {
                                codeZi = work.getCodeZI();
                                name = work.getName();
                            } else {
                                codeZi = null;
                                name = null;
                            }
                            UserDto userDto;
                            try {
                                userDto = userServiceIntegration.getUserDto(null, user);
                            } catch (ResourceNotFoundException ex) {
                                userDto = new UserDto(-1L, "", "логином", "Не найден пользователь с", user);
                            }
                            workFactDTOs.add(
                                    new WorkFactDto(
                                            num.incrementAndGet(),
                                            codeZi,
                                            name,
                                            users.size(),
                                            user,
                                            userDto.getFirstName(),
                                            userDto.getLastName(),
                                            userDto.getPatronymic(),
                                            getFactWork(work, 0, user),
                                            getFactWork(work, 1, user),
                                            getFactWork(work, 2, user),
                                            getFactWork(work, 3, user),
                                            getFactWork(work, 4, user),
                                            getFactWork(work, 5, user)
                                    )
                            );

                        }
                    } else {
                        if (!hideNotTime) {
                            workFactDTOs.add(
                                    new WorkFactDto(
                                            num.incrementAndGet(),
                                            work.getCodeZI(),
                                            work.getName(),
                                            1,
                                            null,
                                            null,
                                            null,
                                            null,
                                            null,
                                            null,
                                            null,
                                            null,
                                            null,
                                            null

                                    )
                            );
                        }
                    }

                }
        );
        return new PageDto<>(workPage.getTotalPages(), workPage.getNumber(), workPage.getSize(), workFactDTOs);
    }

    public MapStringFloat getFactWorkStage0(Long workId, String nikName) {
        MapStringFloat mapStringFloat = new MapStringFloat();
        Map<String, Float> usersTime = new HashMap<>();
        mapStringFloat.setList(usersTime);
        Work work = workService.findById(workId).orElseThrow(() -> new ResourceNotFoundException("Не найдено ЗИ с id = " + workId));
        Set<String> users;
        if (nikName != null && !nikName.equals("")) {
            users = new HashSet<>();
            users.add(nikName);
        } else {
            users = taskServiceIntegration.getListUser(workId, work.getDevelopEndFact()).getList();
        }
        users.forEach(user -> {
            Float time = getFactWork(work, 0, user);
            if (time > 0) {
                usersTime.put(user, time);
            }
        });
        return mapStringFloat;
    }

    public Float getFactWork(Long workId, Integer stage, String nikName) {
        Work work = workService.findById(workId).orElseThrow(() -> new ResourceNotFoundException("Не найдено ЗИ с id = " + workId));
        return getFactWork(work, stage, nikName);
    }

    private Float getFactWork(Work work, Integer stage) {
        return getFactWork(work, stage, null);
    }

    public Float getFactWork(Work work, Integer stage, String nikName) {
        if (stage == 0) {
            return taskServiceIntegration.getTimeWork(work.getId(),
                    nikName,
                    null,
                    getTimeDevelop(work),
                    "analise");
        }
        if (stage == 1) {
            return taskServiceIntegration.getTimeWork(
                    work.getId(),
                    nikName,
                    null,
                    getTimeDevelop(work),
                    "develop");
        }
        if (stage == 2) {
            return taskServiceIntegration.getTimeWork(work.getId(),
                    nikName,
                    getTimeDevelop(work),
                    work.getDebugEndFact());
        }
        if (stage == 3) {
            return taskServiceIntegration.getTimeWork(work.getId(),
                    nikName,
                    work.getDebugEndFact(),
                    work.getReleaseEndFact());
        }
        if (stage == 4) {
            return taskServiceIntegration.getTimeWork(work.getId(),
                    nikName,
                    work.getReleaseEndFact(),
                    work.getOpeEndFact());
        }
        if (stage == 5) {
            taskServiceIntegration.getTimeWork(work.getId(),
                    nikName,
                    work.getOpeEndFact(),
                    null);
        }
        return 0f;
    }

    private Timestamp getTimeDevelop(Work work) {
        Timestamp timestampDevelop;
        if (work.getAnaliseEndFact() == null
                && work.getDevelopEndFact() == null
                && work.getDebugEndFact() == null
                && work.getReleaseEndFact() == null
                && work.getOpeEndFact() == null) {
            timestampDevelop = new Timestamp(new Date().getTime());
        } else {
            timestampDevelop = work.getDevelopEndFact();
        }
        return timestampDevelop;
    }


    public List<WorkUserTime> getWeekWork(boolean ziSplit, Boolean addTotal, String nikName, boolean weekSplit, Timestamp dateStart, Timestamp dateEnd) {
        List<WorkUserTime> workUserTimes = new ArrayList<>();
        if (ziSplit) {
            Iterable<WorkLittle> works = workService.findWorkLittle(null, null, nikName, null, null, 5);
            works.forEach(work ->
                    workUserTimes.add(new WorkUserTime(
                            work.getId(),
                            work.getCodeSap(),
                            work.getCodeZI(),
                            work.getName(),
                            work.getStageZI(),
                            taskServiceIntegration.getWorkUserOrZi(work.getId(), nikName))
                    )
            );

        } else {

            workUserTimes.add(new WorkUserTime(
                    null,
                    null,
                    null,
                    "Без ЗИ",
                    null,
                    workTimeServiceIntegration.getWorkUserOrZiBig(null, nikName, addTotal, weekSplit, dateStart, dateEnd))
            );


        }
        return workUserTimes;
    }
}
