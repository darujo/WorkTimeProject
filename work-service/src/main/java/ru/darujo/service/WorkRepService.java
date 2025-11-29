package ru.darujo.service;

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
import ru.darujo.dto.user.UserDto;
import ru.darujo.dto.workperiod.WorkUserTime;
import ru.darujo.dto.workrep.*;
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.integration.CalendarServiceIntegration;
import ru.darujo.integration.TaskServiceIntegration;
import ru.darujo.integration.UserServiceIntegration;
import ru.darujo.integration.WorkTimeServiceIntegration;
import ru.darujo.model.Work;
import ru.darujo.model.WorkLittle;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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

    CalendarServiceIntegration calendarServiceIntegration;

    @Autowired
    public void setCalendarServiceIntegration(CalendarServiceIntegration calendarServiceIntegration) {
        this.calendarServiceIntegration = calendarServiceIntegration;
    }

    public List<WorkRepDto> getWorkRep(String name, Boolean availWork, Integer stageZiGe, Integer stageZiLe, Long releaseId, String[] sort) {
        List<WorkRepDto> workRepDTOs = new ArrayList<>();
        List<Work> works = workService.getWorkList(name, stageZiGe, stageZiLe, releaseId, sort);
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
                                        work.getIssuePrototypePlan(),
                                        work.getIssuePrototypeFact(),
                                        work.getDebugEndPlan(),
                                        work.getDebugEndFact(),
                                        work.getRelease() != null ? work.getRelease().getName() : null,
                                        work.getRelease() != null ? work.getRelease().getIssuingReleasePlan() : null,
                                        work.getRelease() != null ? work.getRelease().getIssuingReleaseFact() : null,
                                        work.getReleaseEndPlan(),
                                        work.getReleaseEndFact(),
                                        work.getOpeEndPlan(),
                                        work.getOpeEndFact(),
                                        getFactWork(work, 0),
                                        getFactWork(work, 1),
                                        getFactWork(work, 2),
                                        getFactWork(work, 3),
                                        getFactWork(work, 4),
                                        getFactWork(work, 5),
                                        work.getIssuePrototypePlan(),
                                        work.getIssuePrototypeFact()

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
                                               Long releaseId,
                                               String sort,
                                               boolean hideNotTime) {
        AtomicInteger num = new AtomicInteger();
        List<WorkFactDto> workFactDTOs = new ArrayList<>();
        Page<@NonNull Work> workPage = workService.findWorks(page, size, nameZi, sort, stageZiGe, stageZiLe, codeSap, codeZiSearch, task, releaseId);
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
                    if (users != null && !users.isEmpty()) {
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
                            } catch (ResourceNotFoundRunTime ex) {
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
        Work work = workService.findById(workId);
        Set<String> users;
        if (nikName != null && !nikName.isEmpty()) {
            users = new HashSet<>();
            users.add(nikName);
        } else {
            users = taskServiceIntegration.getListUser(workId, work.getIssuePrototypeFact()).getList();
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
        Work work = workService.findById(workId);
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
                                          Integer page, Integer size, String name, Integer stageZiGe, Integer stageZiLe, Long codeSap, String codeZi, String task, Long releaseId, String sort) {
        List<WorkUserTime> workUserTimes = new ArrayList<>();
        if (ziSplit) {
            Iterable<WorkLittle> works = workService.findWorkLittle(page, size, name, sort, stageZiGe, stageZiLe, codeSap, codeZi, task, releaseId);
            works.forEach(work ->
                    workUserTimes.add(new WorkUserTime(
                            work.getId(),
                            work.getCodeSap(),
                            work.getCodeZI(),
                            work.getName(),
                            work.getStageZI(),
                            taskServiceIntegration.getWorkUserOrZi(work.getId(), nikName, addTotal))
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

    public PageObjDto<WorkGraphsDto> getWorkGraphRep(Integer page,
                                                     Integer size,
                                                     String nameZi,
                                                     Integer stageZiGe,
                                                     Integer stageZiLe,
                                                     Long codeSap,
                                                     String codeZiSearch,
                                                     String task,
                                                     Long releaseId,
                                                     String sort,
                                                     Timestamp dateStart,
                                                     Timestamp dateEnd,
                                                     String period) {
        List<WeekWorkDto> weekWorkDTOs = calendarServiceIntegration.getPeriodTime(dateStart, dateEnd, period);
        Page<@NonNull Work> workPage = workService.findWorks(page, size, nameZi, sort, stageZiGe, stageZiLe, codeSap, codeZiSearch, task, releaseId);
        List<WorkGraphDto> workGraphDTOs =
                workPage.map(work -> new WorkGraphDto(WorkConvertor.getWorkLittleDto(work),
                        weekWorkDTOs.stream().map(weekWorkDto -> new WorkPeriodColorDto(weekWorkDto, getColor(weekWorkDto, work, true))).collect(Collectors.toList()),
                        weekWorkDTOs.stream().map(weekWorkDto -> new WorkPeriodColorDto(weekWorkDto, getColor(weekWorkDto, work, false))).collect(Collectors.toList())
                )).toList();
        WorkGraphsDto workFactDTOs = new WorkGraphsDto(weekWorkDTOs, workGraphDTOs);

        return new PageObjDto<>(workPage.getTotalPages(), workPage.getNumber(), workPage.getSize(), workFactDTOs);
    }


    Map<Integer, ColorDto> colorDtoMap = new HashMap<>();

    private ColorDto getColor(WeekWorkDto weekWorkDto, Work work, boolean plan) {
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

    private List<String> getPeriodFactTypes(Work work, Timestamp dayStart, Timestamp dayEnd) {
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

    private List<String> getPeriodPlanTypes(Work work, Timestamp dayStart, Timestamp dayEnd) {
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
            return ColorHelper.WHITE;
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

}
