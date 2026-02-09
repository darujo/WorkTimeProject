package ru.darujo.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class WorkService {

    private WorkRepository workRepository;
    private WorkProjectLittleService workProjectLittleService;

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

    @PostConstruct
    public static void init() {
        try {
            userServiceIntegration.getProjects(null, null).forEach(projectDto ->
                    projectDtoMap.put(projectDto.getId(), projectDto));
        } catch (RuntimeException ignore){

        }

    }

    public static Map<Long, ProjectDto> getProjectDtoMap() {
        if (projectDtoMap.isEmpty()) {
            init();
        }
        return projectDtoMap;
    }


    public WorkFull findById(long id, long projectId) {
        Work work = workRepository.findById(id).orElseThrow(() -> new ResourceNotFoundRunTime("Задача не найден"));
        WorkProject workProject = workProjectService.getWorkProject(work, projectId);
        return new WorkFull(work, workProject);
    }

    public WorkLittleFull findLittleById(long workId, Long projectId) {
        WorkLittle workLittle = workLittleRepository.findById(workId).orElseThrow(() -> new ResourceNotFoundRunTime("Задача не найден"));
        return new WorkLittleFull(workLittle, workProjectLittleService.getWorkProject(workLittle, projectId));
    }

    public void checkWork(Work work, WorkProject workProject) {
        Release release;
        if (work.getId() != null) {
            release = workProjectService.getWorkProjectOrEmpty(work, workProject.getProjectId()).getRelease();
            if (release != null) {
                if (release.getIssuingReleaseFact() != null) {
                    if (workProject.getRelease() == null || !workProject.getRelease().getId().equals(release.getId())) {
                        throw new ResourceNotFoundRunTime("Нельзя исключать ЗИ из релиза. Релиз выпущен.");
                    }
                } else {
                    if (workProject.getRelease() != null && !workProject.getRelease().getId().equals(release.getId())) {
                        release = releaseService.findById(workProject.getRelease().getId());
                        if (release.getIssuingReleaseFact() != null) {
                            throw new ResourceNotFoundRunTime("Нельзя включать ЗИ в выпущенный релиз");
                        }
                    }
                }
            } else {
                if (workProject.getRelease() != null && workProject.getRelease().getId() != null) {
                    release = releaseService.findById(workProject.getRelease().getId());
                    if (release.getIssuingReleaseFact() != null) {
                        throw new ResourceNotFoundRunTime("Нельзя включать ЗИ в выпущенный релиз");
                    }
                }
            }
        } else {
            if (workProject.getRelease() != null && workProject.getRelease().getId() != null) {
                release = releaseService.findById(workProject.getRelease().getId());
                if (release.getIssuingReleaseFact() != null) {
                    throw new ResourceNotFoundRunTime("Нельзя включать ЗИ в выпущенный релиз");
                }
            }
        }
        checkDate(workProject.getAnaliseStartFact(), workProject.getAnaliseEndFact(), "начала анализа (факт)", "конца анализа (факт)");
        checkDate(workProject.getDevelopStartFact(), workProject.getIssuePrototypeFact(), "начала разработки (факт)", "конца разработки (факт)");
        checkDate(workProject.getDebugStartFact(), workProject.getDebugEndFact(), "начала отладки (факт)", "конца отладки (факт)");
        checkDate(workProject.getReleaseStartFact(), workProject.getReleaseEndFact(), "начала тестирования релиза (факт)", "конца тестирования релиза (факт)");
        checkDate(workProject.getOpeStartFact(), workProject.getOpeEndFact(), "начала ОПЭ (факт)", "конца ОПЭ (факт)");

        checkDate(workProject.getAnaliseEndFact(), workProject.getIssuePrototypeFact(), "конца анализа (факт)", "конца разработки (факт)");
        checkDate(workProject.getIssuePrototypeFact(), workProject.getDebugEndFact(), "конца разработки (факт)", "конца отладки (факт)");
        checkDate(workProject.getDebugEndFact(), workProject.getReleaseEndFact(), "конца отладки (факт)", "конца тестирования релиза (факт)");
        checkDate(workProject.getReleaseEndFact(), workProject.getOpeEndFact(), "конца тестирования релиза (факт)", "конца ОПЭ (факт)");

        checkDate(workProject.getAnaliseStartPlan(), workProject.getAnaliseEndPlan(), "начала анализа (план)", "конца анализа (план)");
        checkDate(workProject.getDevelopStartPlan(), workProject.getIssuePrototypePlan(), "начала разработки (план)", "конца разработки (план)");
        checkDate(workProject.getDebugStartPlan(), workProject.getDebugEndPlan(), "начала отладки (план)", "конца отладки (план)");
        checkDate(workProject.getReleaseStartPlan(), workProject.getReleaseEndPlan(), "начала тестирования релиза (план)", "конца тестирования релиза (план)");
        checkDate(workProject.getOpeStartPlan(), workProject.getOpeEndPlan(), "начала ОПЭ (план)", "конца ОПЭ (план)");

        checkDate(workProject.getAnaliseEndPlan(), workProject.getIssuePrototypePlan(), "конца анализа (план)", "конца разработки (план)");
        checkDate(workProject.getIssuePrototypePlan(), workProject.getDebugEndPlan(), "конца разработки (план)", "конца отладки (план)");
        checkDate(workProject.getDebugEndPlan(), workProject.getReleaseEndPlan(), "конца отладки (план)", "конца тестирования релиза (план)");
        checkDate(workProject.getReleaseEndPlan(), workProject.getOpeEndPlan(), "конца тестирования релиза (план)", "конца ОПЭ (план)");


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
            WorkProject workSave = workProjectService.getWorkProject(workFull.getWork(), workFull.getWorkProject().getProjectId());
            if (workSave != null) {
                ratedOld = workSave.getRated();
                stageOld = workSave.getStageZi();
                releaseNameOld = workSave.getRelease() != null ? workSave.getRelease().getName() : null;
            }
        }
        workProjectService.updateWorkLastDevelop(workFull.getWorkProject());
        Work work = workRepository.save(workFull.getWork());
        workProjectService.save(workFull.getWorkProject());
        changeWork(login, work, workFull.getWorkProject(), stageOld, releaseNameOld, ratedOld);
        return workFull;
    }

    private String getMesChangRated(String login, WorkLittleInterface work, WorkProjectInter workProject, String projectName) {
        return workProject.getRated() ?
                String.format("%s проставил <u><b>оценка выполнена</b></u> по ЗИ %s %s в проекте %s",
                        login,
                        work.getCodeSap(),
                        UrlWorkTime.getUrlRate(work.getId(), work.getName()),
                        projectName) :
                String.format("%s <u><b>отменил оценку</b></u> по ЗИ %s %s в проекте %s ",
                        login,
                        work.getCodeSap(),
                        UrlWorkTime.getUrlRate(work.getId(), work.getName()),
                        projectName);
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
        if (sort != null && sort.length() > 8 && sort.startsWith("release.")) {
            if (projectId == null) {
                log.error("Сортировка по релизу не возможна без выбора проекта");
                sort = null;
            }
            specification = Specification.unrestricted();
        } else {
            specification = Specification.where(Specifications.queryDistinctTrue());
        }

        specification = Specifications.like(specification, "name", name);
        specification = Specifications.like(specification, "codeZi", codeZi);
        specification = Specifications.eq(specification, "codeSap", codeSap);
        if (projectId != null) {
            List<Work> workList = null;
            if (name != null || codeSap != null || codeZi != null) {
                workList = workRepository.findAll(specification);
            }
            return workProjectService.getWorkFull(page, size, sort, stageZiGe, stageZiLe, task, releaseId, projectId, workList);
        }
        specification = Specifications.in(specification, "id", workProjectLittleService.getListWorkId(task, releaseId, stageZiLe, stageZiGe, null));

        Page<@NonNull Work> workPage;
        if (sort == null) {
            if (page != null && size != null) {
                workPage = workRepository.findAll(specification, PageRequest.of(page - 1, size));
            } else {
                workPage = new PageImpl<>(workRepository.findAll(specification));
            }

        } else {
            if (page != null && size != null) {
                workPage = workRepository.findAll(specification, PageRequest.of(page - 1, size, Sort.Direction.ASC, sort));
            } else {
                workPage = new PageImpl<>(workRepository.findAll(specification, Sort.by(sort)));
            }
        }
        return workPage.map(work -> new WorkFull(work, null));
    }

    public Page<@NonNull WorkLittleFull> findWorkLittle(Integer page, Integer size, String name, String sort, Integer stageZiGe, Integer stageZiLe, Long codeSap, String codeZi, String task, List<Long> releaseIdArray, Long projectId) {
        Specification<@NonNull WorkLittle> specification;
        if (sort != null && sort.length() > 8 && sort.startsWith("release.")) {
            if (projectId == null) {
                sort = null;
                log.error("Little не возможна сортировка по релизу без проекта");
            }
            specification = Specification.unrestricted();
        } else {
            specification = Specification.where(Specifications.queryDistinctTrue());
        }
        specification = Specifications.eq(specification, "codeSap", codeSap);
        specification = Specifications.like(specification, "codeZi", codeZi);
        specification = Specifications.like(specification, "name", name);
        if (projectId != null) {
            List<WorkLittle> workLittleList = null;
            if (codeSap != null || codeZi != null || name != null) {
                workLittleList = workLittleRepository.findAll(specification);
            }
            return workProjectLittleService.getWorkFull(page, size, sort, stageZiGe, stageZiLe, task, releaseIdArray, projectId, null, workLittleList);
        }
        specification = Specifications.in(specification, "id", workProjectLittleService.getListWorkId(task, null, stageZiLe, stageZiGe, releaseIdArray));


        Page<@NonNull WorkLittle> workPage;
        if (page == null) {
            if (sort == null) {
                workPage = new PageImpl<>(workLittleRepository.findAll(specification));
            } else {
                workPage = new PageImpl<>(workLittleRepository.findAll(specification, Sort.by(sort)));
            }
        } else if (sort == null) {
            workPage = workLittleRepository.findAll(specification, PageRequest.of(page - 1, size));
        } else {
            workPage = workLittleRepository.findAll(specification, PageRequest.of(page - 1, size, Sort.by(sort)));
        }
        return workPage.map(workLittle -> new WorkLittleFull(workLittle, null));
    }

    public List<Work> getWorkList(String name, Integer stageZiGe, Integer stageZiLe, Long releaseId, String[] sort) {
        List<Work> works;
        Specification<@NonNull Work> specification = Specification.unrestricted();
        Sort sortWork = null;
        if (sort != null) {
            for (String sortField : sort) {
                if (sortField.equals("codeZI") || sortField.equals("name")) {
                    sortWork = sortWork == null ? Sort.by(sortField) : sortWork.and(Sort.by(sortField));
                } else {
                    log.error("Сортировка по полю {} не возможна", sortField);
                }
            }
        }
        specification = Specifications.like(specification, "name", name);
        specification = Specifications.in(specification, "id", workProjectService.getWorkIdList(releaseId, stageZiGe, stageZiLe));

        if (sortWork == null) {
            works = workRepository.findAll(specification);
        } else {
            works = workRepository.findAll(specification, sortWork);
        }
        return works;
    }

    public void updWorkPlanTime(WorkPlanTime workPlanTime) {
        WorkStageDto workStageDto = rateServiceIntegration.getTimePlan(workPlanTime.getWorkId(), workPlanTime.getProjectId());
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

    public WorkLittle setRated(String login, long workId, Long projectId, Boolean rated) {
        WorkLittle work = workLittleRepository.findById(workId).orElseThrow(() -> new ResourceNotFoundRunTime("Не найдена работа с таким Id"));
        WorkProjectLittle workProjectLittle = workProjectLittleService.getWorkProjectOrEmpty(work, projectId);
        if (workProjectLittle.getRated() == null || !workProjectLittle.getRated().equals(rated)) {
            workProjectLittle.setRated(rated);
            workProjectLittle = workProjectLittleService.save(workProjectLittle);
            ProjectDto projectDto = projectDtoMap.get(workProjectLittle.getProjectId());
            sendInform(login, MessageType.ESTIMATION_WORK, getMesChangRated(login, work, workProjectLittle, projectDto == null ? "" : projectDto.getName() + " (" + projectDto.getCode() + ")"));
        }
        return work;

    }

    @Transactional
    public void setReleaseAndStageZi(String login, Long workId, Long projectId, Long releaseId, Integer stageZI) {
        WorkLittle workLittle = workLittleRepository.findById(workId).orElseThrow(() -> new ResourceNotFoundRunTime("Не найдено ЗИ"));
        Release release = releaseService.findOptionalById(releaseId).orElse(null);
        WorkProjectLittle workProjectLittle = workProjectLittleService.getWorkProjectOrEmpty(workLittle, projectId);
        Boolean ratedOld = workProjectLittle.getRated();
        Integer stageOld = workProjectLittle.getStageZi();
        String releaseNameOld = workProjectLittle.getRelease() != null ? workProjectLittle.getRelease().getName() : null;
        workProjectLittle.setStageZi(stageZI);
        workProjectLittle.setRelease(release);

        workProjectLittle = workProjectLittleService.save(workProjectLittle);
        changeWork(login, workLittle, workProjectLittle, stageOld, releaseNameOld, ratedOld);
    }

    private void changeWork(String login, WorkLittleInterface workLittle, WorkProjectInter workProject, Integer stageOld, String releaseNameOld, Boolean ratedOld) {
        String releaseNameNew = workProject.getRelease() != null ? workProject.getRelease().getName() : null;
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


    public boolean setWorkDate(long workId, Long projectId, Timestamp date) {
        Work work = workRepository.findById(workId).orElseThrow(() -> new ResourceNotFoundRunTime("Не найдено ЗИ с id " + workId));
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

}