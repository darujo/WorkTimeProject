package ru.darujo.service;

import jakarta.transaction.Transactional;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.darujo.assistant.color.ColorRGB;
import ru.darujo.assistant.helper.ColorHelper;
import ru.darujo.convertor.WorkConvertor;
import ru.darujo.dto.ColorDto;
import ru.darujo.dto.MapStringFloat;
import ru.darujo.dto.PageDto;
import ru.darujo.dto.PageObjDto;
import ru.darujo.dto.calendar.WeekWorkDto;
import ru.darujo.dto.project.ProjectDto;
import ru.darujo.dto.user.UserDto;
import ru.darujo.dto.workperiod.WorkUserTime;
import ru.darujo.dto.workrep.*;
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.integration.CalendarServiceIntegration;
import ru.darujo.integration.TaskServiceIntegration;
import ru.darujo.integration.UserServiceIntegration;
import ru.darujo.integration.WorkTimeServiceIntegration;
import ru.darujo.model.*;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class WorkRepService {
    private TaskServiceIntegration taskServiceIntegration;
    private ReleaseProjectService releaseProjectService;

    @Autowired
    public void setTaskServiceIntegration(TaskServiceIntegration taskServiceIntegration) {
        this.taskServiceIntegration = taskServiceIntegration;
    }

    private WorkTimeServiceIntegration workTimeServiceIntegration;

    @Autowired
    public void setWorkTimeServiceIntegration(WorkTimeServiceIntegration workTimeServiceIntegration) {
        this.workTimeServiceIntegration = workTimeServiceIntegration;
    }

    private UserServiceIntegration userServiceIntegration;

    @Autowired
    public void setUserServiceIntegration(UserServiceIntegration userServiceIntegration) {
        this.userServiceIntegration = userServiceIntegration;
    }

    private WorkService workService;

    @Autowired
    public void setWorkService(WorkService workService) {
        this.workService = workService;
    }

    private CalendarServiceIntegration calendarServiceIntegration;

    @Autowired
    public void setCalendarServiceIntegration(CalendarServiceIntegration calendarServiceIntegration) {
        this.calendarServiceIntegration = calendarServiceIntegration;
    }

    private WorkProjectService workProjectService;

    @Autowired
    public void setWorkProjectService(WorkProjectService workProjectService) {
        this.workProjectService = workProjectService;
    }

    @Transactional
    public List<WorkRepDto> getWorkRep(String name, Boolean availWork, Integer stageZiGe, Integer stageZiLe, Long releaseId, Long projectIdPar, List<String> sort, Boolean addMedium) {
        workService.init();
        List<WorkRepDto> workRepDTOs = new ArrayList<>();
        List<Work> works;
        if (projectIdPar != null) {
            works = workService.getWorkList(name, stageZiGe, stageZiLe, releaseId, projectIdPar, sort);
        } else {
            works = workService.getWorkList(name, stageZiGe, stageZiLe, releaseId, sort);
        }
        WorkRepDto workRepDto;
        if (addMedium) {
            workRepDto = new WorkRepDto(null, null, "Средние по оцененным ЗИ", new ArrayList<>());
        } else {
            workRepDto = null;
        }
        Map<Long, WorkRepProjectDto> workRepProjectDtoMap = new HashMap<>();

        works.forEach(work ->
                {
                    List<WorkRepProjectDto> workRepProjectDtoList = new ArrayList<>();
                    work.getProjectList().stream().filter(projectId -> projectIdPar == null || projectId.equals(projectIdPar)).forEach(projectId -> {
                        boolean availWorkTime = false;
                        if (availWork != null) {
                            availWorkTime = taskServiceIntegration.availWorkTime(work.getId(), work.getChildIdList(), projectId);
                        }
                        if (availWork == null ||
                                (availWork && availWorkTime) ||
                                (!availWork && !availWorkTime)) {
                            WorkProject workProject = workProjectService.getWorkProjectOrEmpty(work, projectId);
                            ProjectDto projectDto = WorkService.getProjectDto(projectId);
                            ReleaseProject releaseProject;
                            if (work.getRelease() != null) {
                                releaseProject = releaseProjectService.findReleaseProject(work.getRelease(), projectId);
                            } else {
                                releaseProject = null;
                            }
                            WorkRepProjectDto workRepProjectDto = new WorkRepProjectDto(work.getId(),
                                    projectId,
                                    workProject.getStartTaskPlan(),
                                    workProject.getStartTaskFact(),
                                    workProject.getAnaliseEndPlan(),
                                    workProject.getAnaliseEndFact(),
                                    workProject.getIssuePrototypePlan(),
                                    workProject.getIssuePrototypeFact(),
                                    workProject.getDebugEndPlan(),
                                    workProject.getDebugEndFact(),
                                    work.getRelease() != null ? work.getRelease().getName() : null,
                                    work.getRelease() != null ? work.getRelease().getIssuingReleasePlan() : null,
                                    releaseProject != null ? releaseProject.getIssuingReleaseFact() : null,
                                    workProject.getReleaseEndPlan(),
                                    workProject.getReleaseEndFact(),
                                    workProject.getOpeEndPlan(),
                                    workProject.getOpeEndFact(),
                                    getFactWork(workProject, projectDto.getStageEnd(), 0),
                                    getFactWork(workProject, projectDto.getStageEnd(), 1),
                                    getFactWork(workProject, projectDto.getStageEnd(), 2),
                                    getFactWork(workProject, projectDto.getStageEnd(), 3),
                                    getFactWork(workProject, projectDto.getStageEnd(), 4),
                                    getFactWork(workProject, projectDto.getStageEnd(), 5),
                                    workProject.getIssuePrototypePlan(),
                                    workProject.getIssuePrototypeFact(),
                                    workProject.getRated(),
                                    work.getChildWork() == null || work.getChildWork().isEmpty() ? null : work.getChildWork().stream().map(WorkLittle::getId).toList()

                            );
                            workService.updateProjectInfo(workRepProjectDto);
                            workRepProjectDtoList.add(workRepProjectDto);

                        }
                    });
                    if (!workRepProjectDtoList.isEmpty()) {

                        workRepProjectDtoList.forEach(workRepProjectDto -> {
                            workService.updWorkPlanTime(workRepProjectDto);

                            if (addMedium && workRepProjectDto.getRated() != null && workRepProjectDto.getRated()) {
                                WorkRepProjectDto workRepProjectDtoTotal = workRepProjectDtoMap.computeIfAbsent(workRepProjectDto.getProjectId(), projectId ->
                                {
                                    WorkRepProjectDto workRepProjectDtoNew = new WorkRepProjectDto(null, workRepProjectDto.getProjectId(), null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 0f, 0f, 0f, 0f, 0f, 0f, null, null, true, null);
                                    workRepProjectDtoNew.setLaborAnalise(0f);
                                    workRepProjectDtoNew.setLaborDevelop(0f);
                                    workRepProjectDtoNew.setLaborDebug(0f);
                                    workRepProjectDtoNew.setLaborRelease(0f);
                                    workRepProjectDtoNew.setLaborOPE(0f);
                                    workRepDto.getWorkRepProjectDtoList().add(workRepProjectDtoNew);
                                    return workRepProjectDtoNew;
                                });
                                workRepProjectDtoTotal.addLaborAnalise(workRepProjectDto.getLaborAnalise());
                                workRepProjectDtoTotal.addLaborDevelop(workRepProjectDto.getLaborDevelop());
                                workRepProjectDtoTotal.addLaborDebug(workRepProjectDto.getLaborDebug());
                                workRepProjectDtoTotal.addLaborRelease(workRepProjectDto.getLaborRelease());
                                workRepProjectDtoTotal.addLaborOPE(workRepProjectDto.getLaborOPE());
                                workRepProjectDtoTotal.addTimeAnalise(workRepProjectDto.getTimeAnalise());
                                workRepProjectDtoTotal.addTimeDevelop(workRepProjectDto.getTimeDevelop());
                                workRepProjectDtoTotal.addTimeDebug(workRepProjectDto.getTimeDebug());
                                workRepProjectDtoTotal.addTimeRelease(workRepProjectDto.getTimeRelease());
                                workRepProjectDtoTotal.addTimeOPE(workRepProjectDto.getTimeOPE());
                                workRepProjectDtoTotal.addTimeWender(workRepProjectDto.getTimeWender());
                                workRepProjectDtoTotal.addCountOne();

                            }
                        });
                        workRepDTOs.add(
                                new WorkRepDto(
                                        work.getId(),
                                        work.getCodeZi(),
                                        work.getName(),
                                        workRepProjectDtoList
                                )
                        );
                    }
                }
        );
        if (workRepDto != null) {
            workRepDto.getWorkRepProjectDtoList().forEach(workRepProjectDto -> {
                workRepProjectDto.applyCount();
                workService.updateProjectInfo(workRepProjectDto);
            });

            workRepDTOs.add(workRepDto);
        }
        return workRepDTOs;
    }

    @Transactional
    public PageDto<WorkFactDto> getWorkFactRep(Integer page,
                                               Integer size,
                                               String userName,
                                               String nameZi,
                                               Integer stageZiGe,
                                               Integer stageZiLe,
                                               Long codeSap,
                                               String codeZiSearch,
                                               String task,
                                               Long releaseId,
                                               List<String> sort,
                                               boolean hideNotTime) {
        AtomicInteger num = new AtomicInteger();
        List<WorkFactDto> workFactDTOs = new ArrayList<>();
        Page<@NonNull Work> workPage = workService.findWorks(page, size, nameZi, sort, stageZiGe, stageZiLe, codeSap, codeZiSearch, task, releaseId);
        workPage.forEach(work -> work.getProjectList().forEach(projectId ->
                {
                    ProjectDto projectDto = WorkService.getProjectDto(projectId);

                    WorkFactDto workFactDto = null;
                    int userProject = 0;
                    WorkProject workProject = workProjectService.getWorkProjectOrEmpty(work, projectId);
                    Set<String> users = taskServiceIntegration.getListUser(work.getId(), projectId, null).getList();
                    if (userName != null) {
                        if (users.contains(userName)) {
                            users = new HashSet<>();
                            users.add(userName);
                        } else {
                            users = null;
                        }
                    }
                    if (users != null && !users.isEmpty()) {
                        List<String> userList = new ArrayList<>(users);
                        for (int i = 0; i < users.size(); i++) {
                            userProject++;
                            String user = userList.get(i);
                            String codeZi;
                            String name;
                            if (workFactDto == null) {
                                codeZi = work.getCodeZi();
                                name = work.getName();
                            } else {
                                codeZi = null;
                                name = null;
                            }
                            UserDto userDto;
                            try {
                                userDto = userServiceIntegration.getUserDto(null, user);
                            } catch (ResourceNotFoundRunTime ex) {
                                userDto = new UserDto(-1L, "", "логином", "Не найден пользователь с", user, false);
                            }
                            workFactDto = new WorkFactDto(
                                    num.incrementAndGet(),
                                    codeZi,
                                    name,
                                    users.size(),
                                    user,
                                    userDto.getFirstName(),
                                    userDto.getLastName(),
                                    userDto.getPatronymic(),
                                    getFactWork(workProject, projectDto.getStageEnd(), 0, user, true),
                                    getFactWork(workProject, projectDto.getStageEnd(), 1, user, true),
                                    getFactWork(workProject, projectDto.getStageEnd(), 2, user, true),
                                    getFactWork(workProject, projectDto.getStageEnd(), 3, user, true),
                                    getFactWork(workProject, projectDto.getStageEnd(), 4, user, true),
                                    getFactWork(workProject, projectDto.getStageEnd(), 5, user, true)
                            );
                            workFactDTOs.add(workFactDto);

                        }

                    } else {
                        if (!hideNotTime) {
                            userProject++;
                            workFactDto = new WorkFactDto(
                                    num.incrementAndGet(),
                                    work.getCodeZi(),
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

                            );
                            workFactDTOs.add(
                                    workFactDto
                            );
                        }
                    }
                    if (workFactDto != null) {
                        workFactDto.setUserCol(userProject);
                    }
                })
        );
        return new PageDto<>(workPage.getTotalPages(), workPage.getNumber(), workPage.getSize(), workFactDTOs);
    }

    @Transactional
    public MapStringFloat getFactWorkStage(List<Long> workIdList, String nikName, Integer stage, Long projectId) {
        MapStringFloat mapStringFloat = new MapStringFloat();
        Map<String, Float> usersTime = new HashMap<>();
        mapStringFloat.setList(usersTime);
        workIdList.forEach(workId -> {
            WorkFull workFull = workService.findById(workId, projectId);
            Set<String> users;
            if (nikName != null && !nikName.isEmpty()) {
                users = new HashSet<>();
                users.add(nikName);
            } else {
                users = taskServiceIntegration.getListUser(workId, projectId, null).getList();
            }
            ProjectDto projectDto = WorkService.getProjectDto(projectId);
            users.forEach(user -> {
                Float time = getFactWork(workFull.getWorkProject(), projectDto.getStageEnd(), stage, user, false);
                if (time > 0) {

                    usersTime.compute(user, (k, savaTime) -> (savaTime == null ? 0 : savaTime) + time);

                }
            });
        });
        return mapStringFloat;
    }

    private Float getFactWork(WorkProject workProject, Integer stageEnd, Integer stage) {
        return getFactWork(workProject, stageEnd, stage, null, true);
    }

    public Float getFactWork(WorkProject workProject, Integer projectStageEnd, Integer stage, String nikName, boolean addChild) {
        List<Long> childList;
        if (addChild) {
            childList = workProject.getWork().getChildIdList();
        } else {
            childList = null;
        }
        if (stage == 0) {
            return taskServiceIntegration.getTimeWork(
                    workProject.getWork().getId(),
                    childList,
                    workProject.getProjectId(),
                    nikName,
                    null,
                    projectStageEnd != null && projectStageEnd == 1 ? workProject.getAnaliseEndFact() : getTimeDevelop(workProject),
                    "analise");
        } else if (stage == 1 && (projectStageEnd == null || projectStageEnd > 1)) {
            return taskServiceIntegration.getTimeWork(
                    workProject.getWork().getId(),
                    childList,
                    workProject.getProjectId(),
                    nikName,
                    null,
                    getTimeDevelop(workProject),
                    "develop");
        } else if (stage == 2 && (projectStageEnd == null || projectStageEnd > 2)) {
            return taskServiceIntegration.getTimeWork(
                    workProject.getWork().getId(),
                    childList,
                    workProject.getProjectId(),
                    nikName,
                    getTimeDevelop(workProject),
                    workProject.getDebugEndFact());
        } else if (stage == 3 && (projectStageEnd == null || projectStageEnd > 3)) {
            return taskServiceIntegration.getTimeWork(
                    workProject.getWork().getId(),
                    childList,
                    workProject.getProjectId(),
                    nikName,
                    workProject.getDebugEndFact(),
                    workProject.getReleaseEndFact());
        } else if (stage == 4 && (projectStageEnd == null || projectStageEnd > 4)) {
            return taskServiceIntegration.getTimeWork(
                    workProject.getWork().getId(),
                    childList,
                    workProject.getProjectId(),
                    nikName,
                    workProject.getReleaseEndFact(),
                    workProject.getOpeEndFact());
        } else if (stage == 5) {
            Timestamp timestamp = null;
            if (projectStageEnd == null) {
                timestamp = workProject.getOpeEndFact();
            } else if (projectStageEnd == 1) {
                timestamp = workProject.getAnaliseEndFact();
            } else if (projectStageEnd == 2) {
                timestamp = getTimeDevelop(workProject);
            } else if (projectStageEnd == 3) {
                timestamp = workProject.getDebugEndFact();
            } else if (projectStageEnd == 4) {
                timestamp = workProject.getReleaseEndFact();
            } else if (projectStageEnd == 5) {
                timestamp = workProject.getOpeEndFact();
            }


            return taskServiceIntegration.getTimeWork(
                    workProject.getWork().getId(),
                    childList,
                    workProject.getProjectId(),
                    nikName,
                    timestamp,
                    null);
        } else {
            return 0f;
        }
    }

    private Timestamp getTimeDevelop(WorkProject work) {
        Timestamp timestampDevelop;
        if (work.getIssuePrototypeFact() == null
                && work.getDebugEndFact() == null
                && work.getReleaseEndFact() == null
                && work.getOpeEndFact() == null) {
            timestampDevelop = new Timestamp(new Date().getTime());
        } else {
            timestampDevelop = work.getIssuePrototypeFact();
        }
        return timestampDevelop;
    }


    public List<WorkUserTime> getWeekWork(boolean ziSplit, Boolean addTotal, String nikName, Boolean weekSplit, Timestamp dateStart, Timestamp dateEnd,
                                          Integer page, Integer size, String name, Long projectId, Integer stageZiGe, Integer stageZiLe, Long codeSap, String codeZi, String task, List<Long> releaseIdList, List<String> sort) {
        List<WorkUserTime> workUserTimes = new ArrayList<>();
        if (ziSplit) {
            Page<@NonNull WorkLittleFull> works = workService.findWorkLittle(page, size, name, sort, stageZiGe, stageZiLe, codeSap, codeZi, task, releaseIdList, projectId);
            works.forEach(workLittleFull -> {
                        WorkLittle work = workLittleFull.getWork();
                        workUserTimes.add(new WorkUserTime(
                                work.getId(),
                                work.getCodeSap(),
                                work.getCodeZi(),
                                work.getName(),
                                taskServiceIntegration.getWorkUserOrZi(work.getId(), projectId, nikName, addTotal))
                        );
                    }
            );

        } else {
            workUserTimes.add(new WorkUserTime(
                    null,
                    null,
                    null,
                    "Без ЗИ",
                    workTimeServiceIntegration.getWorkUserOrZiBig(null, nikName, addTotal, weekSplit, dateStart, dateEnd))
            );
        }
        return workUserTimes;
    }

    public PageObjDto<WorkGraphsDto> getWorkGraphRep(Integer page,
                                                     Integer size,
                                                     String nameZi,
                                                     Integer stageZiGe,
                                                     Integer stageZiLe,
                                                     Long codeSap,
                                                     String codeZiSearch,
                                                     String task,
                                                     Long releaseId,
                                                     List<String> sort,
                                                     Timestamp dateStart,
                                                     Timestamp dateEnd,
                                                     String period) {
        workService.init();
        List<WeekWorkDto> weekWorkDTOs = calendarServiceIntegration.getPeriodTime(dateStart, dateEnd, period);
        Page<@NonNull Work> workPage = workService.findWorks(page, size, nameZi, sort, stageZiGe, stageZiLe, codeSap, codeZiSearch, task, releaseId);
        List<WorkGraphDto> workGraphDTOs =
                workPage.map(work -> {
                    List<ProjectGraphDto> projectGraphDtoList = new ArrayList<>();
                    work.getProjectList().forEach(projectId -> {
                        WorkProject workProject = workProjectService.getWorkProjectOrEmpty(work, projectId);
                        ProjectGraphDto projectGraphDto = new ProjectGraphDto(projectId,
                                weekWorkDTOs.stream().map(weekWorkDto -> new WorkPeriodColorDto(weekWorkDto, getColor(weekWorkDto, workProject, true))).collect(Collectors.toList()),
                                weekWorkDTOs.stream().map(weekWorkDto -> new WorkPeriodColorDto(weekWorkDto, getColor(weekWorkDto, workProject, false))).collect(Collectors.toList())
                        );
                        workService.updateProjectInfo(projectGraphDto);
                        projectGraphDtoList.add(projectGraphDto);
                    });
                    return new WorkGraphDto(WorkConvertor.getWorkLittleDto(work), projectGraphDtoList

                    );
                }).toList();
        WorkGraphsDto workFactDTOs = new WorkGraphsDto(weekWorkDTOs, workGraphDTOs);

        return new PageObjDto<>(workPage.getTotalPages(), workPage.getNumber(), workPage.getSize(), workFactDTOs);
    }


    Map<Integer, ColorDto> colorDtoMap = new HashMap<>();

    private ColorDto getColor(WeekWorkDto weekWorkDto, WorkProject work, boolean plan) {
        List<String> colorTypes;
        if (plan) {
            colorTypes = getPeriodPlanTypes(work, weekWorkDto.getDayStart(), weekWorkDto.getDayEnd());
        } else {
            colorTypes = getPeriodFactTypes(work, weekWorkDto.getDayStart(), weekWorkDto.getDayEnd());
        }
        return getColor(colorTypes);

    }

    public Timestamp getDatePlusDay(Timestamp date, Integer day) {
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, day);
        return new Timestamp(cal.getTimeInMillis());

    }

    private List<String> getPeriodFactTypes(WorkProject work, Timestamp dayStart, Timestamp dayEnd) {
        List<String> colorTypes = new ArrayList<>();
        if (isPeriodIntersect(work.getAnaliseStartFact(), work.getAnaliseEndFact(), dayStart, dayEnd)) {
            colorTypes.add("analise");
        }
        if (isPeriodIntersect(work.getDevelopStartFact(), work.getIssuePrototypeFact(), dayStart, dayEnd)) {
            colorTypes.add("develop");
        }
        if (isPeriodIntersect(work.getDebugStartFact(), work.getDebugEndFact(), dayStart, dayEnd)) {
            colorTypes.add("debug");
        }
        if (isPeriodIntersect(work.getReleaseStartFact(), work.getReleaseEndFact(), dayStart, dayEnd)) {
            colorTypes.add("release");
        }
        if (isPeriodIntersect(work.getOpeStartFact(), work.getOpeEndFact(), dayStart, dayEnd)) {
            colorTypes.add("Ope");
        }
        return colorTypes;
    }

    private List<String> getPeriodPlanTypes(WorkProject work, Timestamp dayStart, Timestamp dayEnd) {
        List<String> colorTypes = new ArrayList<>();
        if (isPeriodIntersect(work.getAnaliseStartPlan(), work.getAnaliseEndPlan(), dayStart, dayEnd)) {
            colorTypes.add("analise");
        }
        if (isPeriodIntersect(work.getDevelopStartPlan(), work.getIssuePrototypePlan(), dayStart, dayEnd)) {
            colorTypes.add("develop");
        }
        if (isPeriodIntersect(work.getDebugStartPlan(), work.getDebugEndPlan(), dayStart, dayEnd)) {
            colorTypes.add("debug");
        }
        if (isPeriodIntersect(work.getReleaseStartPlan(), work.getReleaseEndPlan(), dayStart, dayEnd)) {
            colorTypes.add("release");
        }
        if (isPeriodIntersect(work.getOpeStartPlan(), work.getOpeEndPlan(), dayStart, dayEnd)) {
            colorTypes.add("ope");
        }
        if (isPeriodIntersect(getDatePlusDay(work.getOpeEndPlan(), 1), getDatePlusDay(work.getOpeEndPlan(), 15), dayStart, dayEnd)) {
            colorTypes.add("public");
        }
        return colorTypes;
    }

    private boolean isPeriodIntersect(Timestamp analiseStartFact, Timestamp analiseEndFact, Timestamp dayStart, Timestamp dayEnd) {
        if (analiseStartFact == null || analiseEndFact == null) {
            return false;
        }
        return (analiseStartFact.compareTo(dayStart) <= 0 && dayStart.compareTo(analiseEndFact) <= 0)
                || (analiseStartFact.compareTo(dayEnd) <= 0 && dayEnd.compareTo(analiseEndFact) <= 0)
                || (dayStart.compareTo(analiseEndFact) <= 0 && analiseEndFact.compareTo(dayEnd) <= 0);
    }

    ColorRGB color;

    private ColorDto getColor(List<String> types) {
        ColorDto colorDto = colorDtoMap.get(types.hashCode());
        if (colorDto != null) {
            return colorDto;
        }
        color = null;
        types.forEach(type -> addColor(getColor(type)));
        if (color == null) {
            return null;
        }
        color.save();
        return color;

    }

    private ColorRGB getColor(String type) {
        return switch (type) {
            case "analise" -> ColorHelper.ANALISE;
            case "develop" -> ColorHelper.DEVELOP;
            case "debug" -> ColorHelper.DEBUG;
            case "release" -> ColorHelper.RELEASE;
            case "ope" -> ColorHelper.OPE;
            case "public" -> ColorHelper.PUBLIC;
            default -> ColorHelper.WHITE;
        };
    }

    private void addColor(ColorRGB colorRGB) {
        if (color == null) {
            color = colorRGB;
        } else {
            color.addColor(colorRGB);
        }
    }


    @Autowired
    public void setReleaseProjectService(ReleaseProjectService releaseProjectService) {
        this.releaseProjectService = releaseProjectService;
    }
}
