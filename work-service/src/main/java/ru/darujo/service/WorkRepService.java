package ru.darujo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.darujo.dto.PageDto;
import ru.darujo.dto.UserDto;
import ru.darujo.dto.workrep.WorkFactDto;
import ru.darujo.dto.workrep.WorkRepDto;
import ru.darujo.exceptions.ResourceNotFoundException;
import ru.darujo.integration.TaskServiceIntegration;
import ru.darujo.integration.UserServiceIntegration;
import ru.darujo.model.Work;

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
                        Timestamp timestampDevelop = getTimeDevelop(work);

                        workRepDTOs.add(
                                new WorkRepDto(
                                        work.getId(),
                                        work.getCodeZI(),
                                        work.getName(),
                                        work.getStartTaskPlan(),
                                        work.getStartTaskFact(),
                                        work.getAnaliseEndPlan(),
                                        work.getAnaliseEndFact(),
                                        work.getLaborDevelop(),
                                        work.getDevelopEndPlan(),
                                        work.getDevelopEndFact(),
                                        work.getDebugEndPlan(),
                                        work.getDebugEndFact(),
                                        work.getLaborDebug(),
                                        work.getRelease(),
                                        work.getIssuingReleasePlan(),
                                        work.getIssuingReleaseFact(),
                                        work.getReleaseEndPlan(),
                                        work.getReleaseEndFact(),
                                        work.getLaborRelease(),
                                        work.getOpeEndPlan(),
                                        work.getOpeEndFact(),
                                        work.getLaborOPE(),
                                        taskServiceIntegration.getTimeWork(
                                                work.getId(),
                                                null,
                                                null,
                                                timestampDevelop,
                                                "analise"),
                                        taskServiceIntegration.getTimeWork(
                                                work.getId(),
                                                null,
                                                null,
                                                timestampDevelop,
                                                "develop"),
                                        taskServiceIntegration.getTimeWork(work.getId(),
                                                null,
                                                timestampDevelop,
                                                work.getDebugEndFact()),
                                        taskServiceIntegration.getTimeWork(work.getId(),
                                                null,
                                                work.getDebugEndFact(),
                                                work.getReleaseEndFact()),
                                        taskServiceIntegration.getTimeWork(work.getId(),
                                                null,
                                                work.getReleaseEndFact(),
                                                work.getOpeEndFact()),
                                        taskServiceIntegration.getTimeWork(work.getId(),
                                                null,
                                                work.getOpeEndFact(),
                                                null)

                                )
                        );
                    }
                }
        );
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
                    Set<String> users = taskServiceIntegration.getListUser(work.getId()).getList();
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
                            Timestamp timestampDevelop = getTimeDevelop(work);
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
                                            taskServiceIntegration.getTimeWork(work.getId(),
                                                    user,
                                                    null,
                                                    timestampDevelop,
                                                    "analise"),
                                            taskServiceIntegration.getTimeWork(
                                                    work.getId(),
                                                    user,
                                                    null,
                                                    timestampDevelop,
                                                    "develop"),
                                            taskServiceIntegration.getTimeWork(work.getId(),
                                                    user,
                                                    timestampDevelop,
                                                    work.getDebugEndFact()),
                                            taskServiceIntegration.getTimeWork(work.getId(),
                                                    user,
                                                    work.getDebugEndFact(),
                                                    work.getReleaseEndFact()),
                                            taskServiceIntegration.getTimeWork(work.getId(),
                                                    user,
                                                    work.getReleaseEndFact(),
                                                    work.getOpeEndFact()),
                                            taskServiceIntegration.getTimeWork(work.getId(),
                                                    user,
                                                    work.getOpeEndFact(),
                                                    null)

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


}
