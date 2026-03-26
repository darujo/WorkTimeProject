package ru.darujo.service;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.darujo.dto.information.MessageInfoDto;
import ru.darujo.dto.information.MessageType;
import ru.darujo.dto.project.ProjectDto;
import ru.darujo.dto.ratestage.WorkStageDto;
import ru.darujo.dto.work.WorkPlanTime;
import ru.darujo.dto.workrep.ProjectUpdateInter;
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.integration.InfoServiceIntegration;
import ru.darujo.integration.RateServiceIntegration;
import ru.darujo.integration.UserServiceIntegration;
import ru.darujo.model.*;
import ru.darujo.repository.WorkLittleRepository;
import ru.darujo.repository.WorkRepository;
import ru.darujo.specifications.Specifications;
import ru.darujo.url.UrlWorkTime;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
public class WorkService {

    private WorkRepository workRepository;
    private WorkProjectLittleService workProjectLittleService;
    private ReleaseProjectService releaseProjectService;

    @Autowired
    public void setWorkRepository(WorkRepository workRepository) {
        this.workRepository = workRepository;
    }

    private WorkLittleRepository workLittleRepository;

    @Autowired
    public void setWorkLittleRepository(WorkLittleRepository workLittleRepository) {
        this.workLittleRepository = workLittleRepository;
    }

    private RateServiceIntegration rateServiceIntegration;

    @Autowired
    public void setRateServiceIntegration(RateServiceIntegration rateServiceIntegration) {
        this.rateServiceIntegration = rateServiceIntegration;
    }

    ReleaseService releaseService;

    @Autowired
    public void setReleaseService(ReleaseService releaseService) {
        this.releaseService = releaseService;
    }

    InfoServiceIntegration infoServiceIntegration;

    @Autowired
    public void setInfoServiceIntegration(InfoServiceIntegration infoServiceIntegration) {
        this.infoServiceIntegration = infoServiceIntegration;
    }

    private WorkProjectService workProjectService;

    @Autowired
    public void setWorkProjectService(WorkProjectService workProjectService) {
        this.workProjectService = workProjectService;
    }

    private static UserServiceIntegration userServiceIntegration;

    @Autowired
    public void setUserServiceIntegration(UserServiceIntegration userServiceIntegration) {
        WorkService.userServiceIntegration = userServiceIntegration;
    }

    private static final Map<Long, ProjectDto> projectDtoMap = new HashMap<>();

    public static ProjectDto getProjectDto(Long projectId) {
        ProjectDto projectDto = projectDtoMap.get(projectId);
        if (projectDto == null) {
            projectDto = new ProjectDto(null, "неизвестный", "неизвестный", 5, "этап 0", "этап 1", "этап 2", "этап 3", "этап 4");
        }
        return projectDto;
    }

    //    @PostConstruct
    public void init() {
        try {
            userServiceIntegration.getProjects(null, null).forEach(projectDto ->
                    projectDtoMap.put(projectDto.getId(), projectDto));
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
        }

    }

    public Work findById(long id) {
        return workRepository.findById(id).orElseThrow(() -> new ResourceNotFoundRunTime("Задача не найден"));
    }

    public WorkFull findById(long id, long projectId) {
        Work work = findById(id);
        WorkProject workProject = workProjectService.getWorkProject(work, projectId);
        return new WorkFull(work, workProject);
    }

    public WorkLittleFull findLittleById(long workId, Long projectId) {
        WorkLittle workLittle = workLittleRepository.findById(workId).orElseThrow(() -> new ResourceNotFoundRunTime("Задача не найден"));
        return new WorkLittleFull(workLittle, workProjectLittleService.getWorkProject(workLittle, projectId));
    }

    public void checkWork(Work work, WorkProject workProject) {
        if (work.getChildWork() != null && !work.getChildWork().isEmpty() && work.getWorkParent() != null) {
            throw new ResourceNotFoundRunTime("У ЗИ может быть родитель или потомки");
        }
        Release release;
        ReleaseProject releaseProject;
        if (work.getId() != null) {
            release = findById(work.getId()).getRelease();

            if (release != null) {
                releaseProject = releaseProjectService.findReleaseProject(release, workProject.getProjectId());
                if (releaseProject.getIssuingReleaseFact() != null) {
                    if (work.getRelease() == null || !work.getRelease().getId().equals(release.getId())) {
                        throw new ResourceNotFoundRunTime("Нельзя исключать ЗИ из релиза. Релиз выпущен.");
                    }
                } else {
                    if (work.getRelease() != null && !work.getRelease().getId().equals(release.getId())) {
                        release = releaseService.findById(work.getRelease().getId());
                        releaseProject = releaseProjectService.findReleaseProject(release, workProject.getProjectId());

                        if (releaseProject.getIssuingReleaseFact() != null) {
                            throw new ResourceNotFoundRunTime("Нельзя включать ЗИ в выпущенный релиз");
                        }
                    }
                }
            } else {
                if (work.getRelease() != null && work.getRelease().getId() != null) {
                    release = releaseService.findById(work.getRelease().getId());
                    releaseProject = releaseProjectService.findReleaseProject(release, workProject.getProjectId());

                    if (releaseProject.getIssuingReleaseFact() != null) {
                        throw new ResourceNotFoundRunTime("Нельзя исключать ЗИ из выпущенного релиза");
                    }
                }
            }
        } else {
            if (work.getRelease() != null && work.getRelease().getId() != null) {
                release = releaseService.findById(work.getRelease().getId());
                releaseProject = releaseProjectService.findReleaseProject(release, workProject.getProjectId());

                if (releaseProject.getIssuingReleaseFact() != null) {
                    throw new ResourceNotFoundRunTime("Нельзя включать ЗИ в выпущенный релиз");
                }
            }
        }
        ProjectDto projectDto = getProjectDto(workProject.getProjectId());
        checkDate(workProject.getAnaliseStartFact(), workProject.getAnaliseEndFact(), "начала \"" + projectDto.getStage0Name() + "\" (факт)", "конца \"" + projectDto.getStage0Name() + "\" (факт)");
        checkDate(workProject.getDevelopStartFact(), workProject.getIssuePrototypeFact(), "начала \"" + projectDto.getStage1Name() + "\" (факт)", "конца \"" + projectDto.getStage1Name() + "\" (факт)");
        checkDate(workProject.getDebugStartFact(), workProject.getDebugEndFact(), "начала \"" + projectDto.getStage2Name() + "\" (факт)", "конца \"" + projectDto.getStage2Name() + "\" (факт)");
        checkDate(workProject.getReleaseStartFact(), workProject.getReleaseEndFact(), "начала \"" + projectDto.getStage3Name() + "\" (факт)", "конца \"" + projectDto.getStage3Name() + "\" (факт)");
        checkDate(workProject.getOpeStartFact(), workProject.getOpeEndFact(), "начала \"" + projectDto.getStage4Name() + "\" (факт)", "конца \"" + projectDto.getStage4Name() + "\" (факт)");

        checkDate(workProject.getAnaliseEndFact(), workProject.getIssuePrototypeFact(), "конца \"" + projectDto.getStage0Name() + "\" (факт)", "конца \"" + projectDto.getStage1Name() + "\" (факт)");
        checkDate(workProject.getIssuePrototypeFact(), workProject.getDebugEndFact(), "конца \"" + projectDto.getStage1Name() + "\" (факт)", "конца \"" + projectDto.getStage2Name() + "\" (факт)");
        checkDate(workProject.getDebugEndFact(), workProject.getReleaseEndFact(), "конца \"" + projectDto.getStage2Name() + "\" (факт)", "конца \"" + projectDto.getStage3Name() + "\" (факт)");
        checkDate(workProject.getReleaseEndFact(), workProject.getOpeEndFact(), "конца \"" + projectDto.getStage3Name() + "\" (факт)", "конца \"" + projectDto.getStage4Name() + "\" (факт)");

        checkDate(workProject.getAnaliseStartPlan(), workProject.getAnaliseEndPlan(), "начала \"" + projectDto.getStage0Name() + "\" (план)", "конца \"" + projectDto.getStage0Name() + "\" (план)");
        checkDate(workProject.getDevelopStartPlan(), workProject.getIssuePrototypePlan(), "начала \"" + projectDto.getStage1Name() + "\" (план)", "конца \"" + projectDto.getStage1Name() + "\" (план)");
        checkDate(workProject.getDebugStartPlan(), workProject.getDebugEndPlan(), "начала \"" + projectDto.getStage2Name() + "\" (план)", "конца \"" + projectDto.getStage2Name() + "\" (план)");
        checkDate(workProject.getReleaseStartPlan(), workProject.getReleaseEndPlan(), "начала \"" + projectDto.getStage3Name() + "\" (план)", "конца \"" + projectDto.getStage3Name() + "\" (план)");
        checkDate(workProject.getOpeStartPlan(), workProject.getOpeEndPlan(), "начала \"" + projectDto.getStage4Name() + "\" (план)", "конца \"" + projectDto.getStage4Name() + "\" (план)");

        checkDate(workProject.getAnaliseEndPlan(), workProject.getIssuePrototypePlan(), "конца \"" + projectDto.getStage0Name() + "\" (план)", "конца \"" + projectDto.getStage1Name() + "\" (план)");
        checkDate(workProject.getIssuePrototypePlan(), workProject.getDebugEndPlan(), "конца \"" + projectDto.getStage1Name() + "\" (план)", "конца \"" + projectDto.getStage2Name() + "\" (план)");
        checkDate(workProject.getDebugEndPlan(), workProject.getReleaseEndPlan(), "конца \"" + projectDto.getStage2Name() + "\" (план)", "конца \"" + projectDto.getStage3Name() + "\" (план)");
        checkDate(workProject.getReleaseEndPlan(), workProject.getOpeEndPlan(), "конца \"" + projectDto.getStage3Name() + "\" (план)", "конца \"" + projectDto.getStage4Name() + "\" (план)");


    }

    public void checkDate(Timestamp dateStart, Timestamp dateEnd, String dateStartMes, String dateEndMes) {
        if (dateStart != null && dateEnd != null && dateStart.compareTo(dateEnd) > 0) {
            throw new ResourceNotFoundRunTime("Дата " + dateEndMes + " не может быть раньше " + dateStartMes);
        }
    }

    @Transactional
    public WorkFull saveWork(String login, WorkFull workFull) {
        checkWork(workFull.getWork(), workFull.getWorkProject());
        Boolean ratedOld = null;
        Integer stageOld = null;
        String releaseNameOld = null;
        if (workFull.getWork().getId() != null) {
            Work workSave = findById(workFull.getWork().getId());
            releaseNameOld = workSave.getRelease() != null ? workSave.getRelease().getName() : null;
            WorkProject workProjectSave = workProjectService.getWorkProject(workFull.getWork(), workFull.getWorkProject().getProjectId());
            if (workProjectSave != null) {
                ratedOld = workProjectSave.getRated();
                stageOld = workProjectSave.getStageZi();

            }

        }
        if (workFull.getWork().getProjectList() == null) {
            workFull.getWork().setProjectList(new ArrayList<>());
        }
        workProjectService.updateWorkLastDevelop(workFull.getWorkProject());
        if (!workFull.getWork().getProjectList().contains(workFull.getWorkProject().getProjectId())) {
            workFull.getWork().getProjectList().add(workFull.getWorkProject().getProjectId());
        }
        Work work = workRepository.save(workFull.getWork());
        workProjectService.save(workFull.getWorkProject());
        updateProjectList(work);
        changeWork(login, work, workFull.getWorkProject(), stageOld, releaseNameOld, ratedOld);
        return workFull;
    }

    private void updateProjectList(Work work) {
        work.getProjectList().forEach(projectId -> {
            WorkProject workProject = workProjectService.getWorkProject(work, projectId);
            if (workProject == null) {
                workProject = new WorkProject(projectId, work, 0);
                workProjectService.save(workProject);
            }
        });

    }

    private String getMesChangRated(String login, WorkLittleInterface work, WorkProjectInter workProject, String projectName) {
        return workProject.getRated() ?
                String.format("%s проставил <u><b>оценка выполнена</b></u> по ЗИ %s %s в проекте %s оценка %s",
                        login,
                        work.getCodeSap(),
                        UrlWorkTime.getUrlRate(work.getId(), work.getName()),
                        projectName,
                        UrlWorkTime.getUrlRateAll(work.getId(), "ЗИ целиком")) :
                String.format("%s <u><b>отменил оценку</b></u> по ЗИ %s %s в проекте %s оценка %s",
                        login,
                        work.getCodeSap(),
                        UrlWorkTime.getUrlRate(work.getId(), work.getName()),
                        projectName,
                        UrlWorkTime.getUrlRateAll(work.getId(), "ЗИ целиком"));
    }

    private void sendInform(String login, MessageType type, String text) {
        infoServiceIntegration.addMessage(new MessageInfoDto(new Timestamp(System.currentTimeMillis()), login, type, text));
    }


    public void deleteWork(Long id) {
        workLittleRepository.deleteById(id);
    }

    public Page<@NonNull Work> findWorks(int page, int size, String name, String sort, Integer stageZiGe, Integer stageZiLe, Long codeSap, String codeZi, String task, Long releaseId) {
        return findAll(page, size, name, sort, stageZiGe, stageZiLe, codeSap, codeZi, task, releaseId, null).map(WorkFull::getWork);
    }

    @Transactional
    public Page<@NonNull WorkFull> findWorks(int page, int size, String name, String sort, Integer stageZiGe, Integer stageZiLe, Long codeSap, String codeZi, String task, Long releaseId, Long projectId) {
        return findAll(page, size, name, sort, stageZiGe, stageZiLe, codeSap, codeZi, task, releaseId, projectId);
    }


    public Page<@NonNull WorkFull> findAll(Integer page, Integer size, String name, String sort, Integer stageZiGe, Integer stageZiLe, Long codeSap, String codeZi, String task, Long releaseId, Long projectId) {
        Specification<@NonNull Work> specification;
        if (sort != null) {
            specification = Specification.unrestricted();
        } else {
            specification = Specification.where(Specifications.queryDistinctTrue());
        }

        specification = Specifications.like(specification, "name", name);
        specification = Specifications.like(specification, "codeZi", codeZi);
        specification = Specifications.eq(specification, "codeSap", codeSap);
        if (releaseId != null) {
            Release release = releaseService.findOptionalById(releaseId).orElse(null);
            specification = Specifications.eq(specification, "release", release);
        }
        if (projectId != null
                && (stageZiGe != null || (stageZiLe != null && stageZiLe != 9) || task != null)
                && (sort == null || !sort.startsWith("release"))) {
            List<Work> workList = null;
            if (name != null || codeSap != null || codeZi != null) {
                workList = workRepository.findAll(specification);
                if (workList.isEmpty()) {
                    return new PageImpl<>(new ArrayList<>());
                }
            }
            return workProjectService.getWorkFull(page, size, sort, stageZiGe, stageZiLe, task, projectId, workList);
        }

        specification = Specifications.in(specification, "id", workProjectLittleService.getListWorkId(task, releaseId, stageZiLe, stageZiGe, null));

        Page<@NonNull Work> workPage;
        workPage = findAll(page, size, sort, specification, workRepository);
        return workPage.map(work -> new WorkFull(work, projectId == null ? null : workProjectService.getWorkProject(work, projectId)));
    }

    private <T> Page<T> findAll(Integer page, Integer size, String sort, Specification<T> specification, JpaSpecificationExecutor<T> repository) {
        if (sort == null) {
            if (page != null && size != null) {
                return repository.findAll(specification, PageRequest.of(page - 1, size));
            } else {
                return new PageImpl<>(repository.findAll(specification));
            }

        } else {
            if (page != null && size != null) {
                return repository.findAll(specification, PageRequest.of(page - 1, size, Sort.Direction.ASC, sort));
            } else {
                return new PageImpl<>(repository.findAll(specification, Sort.by(sort)));
            }
        }
    }

    public Page<@NonNull WorkLittleFull> findWorkLittle(Integer page, Integer size, String name, String sort, Integer stageZiGe, Integer stageZiLe, Long codeSap, String codeZi, String task, List<Long> releaseIdArray, Long projectId) {
        Specification<@NonNull WorkLittle> specification;
        if (sort != null && sort.length() > 8 && sort.startsWith("release.")) {
            specification = Specification.unrestricted();
        } else {
            specification = Specification.where(Specifications.queryDistinctTrue());
        }
        specification = Specifications.eq(specification, "codeSap", codeSap);
        specification = Specifications.like(specification, "codeZi", codeZi);
        specification = Specifications.like(specification, "name", name);
        if (projectId != null && (sort == null || !sort.startsWith("release."))) {
            List<WorkLittle> workLittleList = null;
            if (codeSap != null || codeZi != null || name != null) {
                workLittleList = workLittleRepository.findAll(specification);
                if (workLittleList.isEmpty()) {
                    return new PageImpl<>(new ArrayList<>());
                }
            }
            return workProjectLittleService.getWorkFull(page, size, sort, stageZiGe, stageZiLe, task, releaseIdArray, projectId, null, workLittleList);
        }
        specification = Specifications.in(specification, "id", workProjectLittleService.getListWorkId(task, null, stageZiLe, stageZiGe, releaseIdArray));


        Page<@NonNull WorkLittle> workPage = findAll(page, size, sort, specification, workLittleRepository);
        return workPage.map(workLittle -> new WorkLittleFull(workLittle, projectId == null ? null : workProjectLittleService.getWorkProject(workLittle, projectId)));
    }

    public List<Work> getWorkList(String name, Integer stageZiGe, Integer stageZiLe, Long releaseId, Long projectId, String[] sort) {
        return findAll(null, null, name, sort[0], stageZiGe, stageZiLe, null, null, null, releaseId, projectId).map(WorkFull::getWork).getContent();
    }

    public List<Work> getWorkList(String name, Integer stageZiGe, Integer stageZiLe, Long releaseId, String[] sort) {
        List<Work> works;
        Specification<@NonNull Work> specification = Specification.unrestricted();
        Sort sortWork = null;
        if (sort != null) {
            for (String sortField : sort) {
                if (sortField.equals("codeZI") || sortField.equals("name") || sortField.startsWith("release")) {
                    sortWork = sortWork == null ? Sort.by(sortField) : sortWork.and(Sort.by(sortField));
                } else {
                    log.error("Сортировка по полю {} не возможна", sortField);
                }
            }
        }
        specification = Specifications.like(specification, "name", name);
        if (releaseId != null) {
            Release release = releaseService.findOptionalById(releaseId).orElse(null);
            specification = Specifications.eq(specification, "release", release);
        }
        specification = Specifications.in(specification, "id", workProjectService.getWorkIdList(stageZiGe, stageZiLe));

        if (sortWork == null) {
            works = workRepository.findAll(specification);
        } else {
            works = workRepository.findAll(specification, sortWork);
        }
        return works;
    }

    public void updWorkPlanTime(WorkPlanTime workPlanTime) {
        updWorkPlanTime(null, workPlanTime);
    }

    public void updWorkPlanTime(List<Long> childIdList, WorkPlanTime workPlanTime) {
        if (workPlanTime.getProjectId() == null) {
            return;
        }
        List<Long> workIdList;
        if (childIdList != null) {
            workIdList = childIdList;
        } else if (workPlanTime.getChildId() == null) {
            workIdList = new ArrayList<>();
            workIdList.add(workPlanTime.getWorkId());
        } else {
            workIdList = workPlanTime.getChildId();
        }
        WorkStageDto workStageDto = rateServiceIntegration.getTimePlan(workIdList, workPlanTime.getProjectId());
        workPlanTime.setLaborAnalise(workStageDto.getStage0());
        workPlanTime.setLaborDevelop(workStageDto.getStage1());
        workPlanTime.setLaborDebug(workStageDto.getStage2());
        workPlanTime.setLaborRelease(workStageDto.getStage3());
        workPlanTime.setLaborOPE(workStageDto.getStage4());
    }

    public boolean checkRight(String right, List<String> rights) {
        right = right.toLowerCase();
        switch (right) {
            case "edit":
            case "stageedit":
            case "criteriaedit":
            case "typeedit":
            case "ziedit":
                if (rights == null || !rights.contains("ZI_EDIT")) {
                    throw new ResourceNotFoundRunTime("У вас нет права на редактирование ZI_EDIT");
                }
                break;
            case "create":
            case "stagecreate":
            case "criteriacreate":
            case "typecreate":
            case "zicreate":
                if (rights == null || !rights.contains("ZI_CREATE")) {
                    throw new ResourceNotFoundRunTime("У вас нет права на редактирование ZI_CREATE");
                }
                break;
        }
        return true;
    }

    public WorkLittleFull setRated(String login, long workId, Long projectId, Boolean rated) {
        WorkLittle work = workLittleRepository.findById(workId).orElseThrow(() -> new ResourceNotFoundRunTime("Не найдена работа с таким Id"));
        init();
        WorkProjectLittle workProjectLittle = workProjectLittleService.getWorkProjectOrEmpty(work, projectId);
        if (workProjectLittle.getRated() == null || !workProjectLittle.getRated().equals(rated)) {
            workProjectLittle.setRated(rated);
            workProjectLittle = workProjectLittleService.save(workProjectLittle);
            ProjectDto projectDto = projectDtoMap.get(workProjectLittle.getProjectId());
            sendInform(login, MessageType.ESTIMATION_WORK, getMesChangRated(login, work, workProjectLittle, projectDto == null ? "" : projectDto.getName() + " (" + projectDto.getCode() + ")"));
        }
        return new WorkLittleFull(work, workProjectLittle);

    }

    @Transactional
    public void setReleaseAndStageZi(String login, Long workId, Long projectId, Long releaseId, Integer stageZI) {
        WorkLittle workLittle = workLittleRepository.findById(workId).orElseThrow(() -> new ResourceNotFoundRunTime("Не найдено ЗИ"));
        Release release = releaseService.findOptionalById(releaseId).orElse(null);
        WorkProjectLittle workProjectLittle = workProjectLittleService.getWorkProjectOrEmpty(workLittle, projectId);
        Boolean ratedOld = workProjectLittle.getRated();
        Integer stageOld = workProjectLittle.getStageZi();
        String releaseNameOld = workLittle.getRelease() != null ? workLittle.getRelease().getName() : null;
        workProjectLittle.setStageZi(stageZI);
        workLittle.setRelease(release);
        workLittleRepository.save(workLittle);
        workProjectLittle = workProjectLittleService.save(workProjectLittle);
        changeWork(login, workLittle, workProjectLittle, stageOld, releaseNameOld, ratedOld);
    }

    private void changeWork(String login, WorkLittleInterface workLittle, WorkProjectInter workProject, Integer stageOld, String releaseNameOld, Boolean ratedOld) {
        init();
        String releaseNameNew = workLittle.getRelease() != null ? workLittle.getRelease().getName() : null;
        StringBuilder workEditText = new StringBuilder();
        workEditText.append(ChangeObj("этап ЗИ", stageOld, workProject.getStageZi()));
        if (!ChangeObj("релиз", releaseNameOld, releaseNameNew).isEmpty()) {
            if (!workEditText.isEmpty()) {
                workEditText.append(",");
            }
            workEditText.append(ChangeObj("релиз", releaseNameOld, releaseNameNew));
        }
        if (!workEditText.isEmpty()
        ) {
            sendInform(login, MessageType.CHANGE_STAGE_WORK, String.format("%s сменил %s по ЗИ %s %s", login, workEditText, workLittle.getCodeSap(), UrlWorkTime.getUrlWorkSap(workLittle.getCodeSap(), workLittle.getName())));
        }
        if (ratedOld != null && !ratedOld.equals(workProject.getRated())) {
            ProjectDto projectDto = projectDtoMap.get(workProject.getProjectId());
            sendInform(login, MessageType.ESTIMATION_WORK, getMesChangRated(login, workLittle, workProject, projectDto == null ? "" : projectDto.getName() + " (" + projectDto.getCode() + ")"));
        }
    }

    private String ChangeObj(String text, Object oldObj, Object newObj) {
        if (oldObj == null) {
            if (newObj != null) {
                return String.format("проставлен <b>%s</b> %s", text, newObj);
            }
        } else if (newObj == null) {
            return String.format("удален <b>%s</b> %s", text, oldObj);
        } else {
            if (!oldObj.equals(newObj)) {
                return String.format("<b>%s</b> %s -> %s", text, oldObj, newObj);
            }
        }
        return "";
    }

    public Work addProject(long workId, Long projectId) {
        Work work = workRepository.findById(workId).orElseThrow(() -> new ResourceNotFoundRunTime("Не найдено ЗИ с id " + workId));
        if (!work.getProjectList().contains(projectId)) {
            work.getProjectList().add(projectId);
            workRepository.save(work);
        }
        return work;
    }

    public boolean setWorkDate(long workId, Long projectId, Timestamp date) {
        Work work = addProject(workId, projectId);
        return workProjectService.setWorkDate(work, projectId, date);
    }

    @Autowired
    public void setWorkProjectLittleService(WorkProjectLittleService workProjectLittleService) {
        this.workProjectLittleService = workProjectLittleService;
    }

    public void updateProjectInfo(ProjectUpdateInter projectUpdateInter) {
        ProjectDto projectDto = projectDtoMap.get(projectUpdateInter.getProjectId());
        if (projectDto != null) {
            projectUpdateInter.setProjectCode(projectDto.getCode());
            projectUpdateInter.setProjectName(projectDto.getName());
        }
    }

    public Boolean getRate(List<Long> workIdList, Long projectId) {
        AtomicReference<Boolean> rated = new AtomicReference<>();
        workIdList.forEach(workId -> {
            WorkProjectLittle workProject = findLittleById(workId, projectId).getWorkProject();
            if (workProject != null && workProject.getRated() != null) {
                if (rated.get() == null || rated.get()) {
                    rated.set(workProject.getRated());
                }
            } else {
                rated.set(false);
            }
        });
        return false;
    }

    @Autowired
    public void setReleaseProjectService(ReleaseProjectService releaseProjectService) {
        this.releaseProjectService = releaseProjectService;
    }
}