package ru.darujo.service;

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
import ru.darujo.dto.ratestage.WorkStageDto;
import ru.darujo.dto.work.WorkPlanTime;
import ru.darujo.exceptions.ResourceNotFoundException;
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.integration.InfoServiceIntegration;
import ru.darujo.integration.RateServiceIntegration;
import ru.darujo.integration.TaskServiceIntegration;
import ru.darujo.model.*;
import ru.darujo.repository.WorkLittleRepository;
import ru.darujo.repository.WorkRepository;
import ru.darujo.specifications.Specifications;
import ru.darujo.url.UrlWorkTime;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class WorkService {

    private WorkRepository workRepository;

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

    TaskServiceIntegration taskServiceIntegration;

    @Autowired
    public void setTaskServiceIntegration(TaskServiceIntegration taskServiceIntegration) {
        this.taskServiceIntegration = taskServiceIntegration;
    }

    public Work findById(long id) {
        return workRepository.findById(id).orElseThrow(() -> new ResourceNotFoundRunTime("Задача не найден"));
    }

    public Optional<WorkLittle> findLittleById(long id) {
        return workLittleRepository.findById(id);
    }

    public void checkWork(Work work) {
        Release release;
        if (work.getId() != null) {
            release = workRepository.findById(work.getId()).orElseThrow(() -> new ResourceNotFoundRunTime("ЗИ пропало(((")).getRelease();
            if (release != null) {
                if (release.getIssuingReleaseFact() != null) {
                    if (work.getRelease() == null || !work.getRelease().getId().equals(release.getId())) {
                        throw new ResourceNotFoundRunTime("Нельзя исключать ЗИ из релиза. Релиз выпущен.");
                    }
                } else {
                    if (work.getRelease() != null && !work.getRelease().getId().equals(release.getId())) {
                        release = releaseService.findById(work.getRelease().getId());
                        if (release.getIssuingReleaseFact() != null) {
                            throw new ResourceNotFoundRunTime("Нельзя включать ЗИ в выпущенный релиз");
                        }
                    }
                }
            } else {
                if (work.getRelease() != null && work.getRelease().getId() != null) {
                    release = releaseService.findById(work.getRelease().getId());
                    if (release.getIssuingReleaseFact() != null) {
                        throw new ResourceNotFoundRunTime("Нельзя включать ЗИ в выпущенный релиз");
                    }
                }
            }
        } else {
            if (work.getRelease() != null && work.getRelease().getId() != null) {
                release = releaseService.findById(work.getRelease().getId());
                if (release.getIssuingReleaseFact() != null) {
                    throw new ResourceNotFoundRunTime("Нельзя включать ЗИ в выпущенный релиз");
                }
            }
        }
        checkDate(work.getAnaliseStartFact(), work.getAnaliseEndFact(), "начала анализа (факт)", "конца анализа (факт)");
        checkDate(work.getDevelopStartFact(), work.getIssuePrototypeFact(), "начала разработки (факт)", "конца разработки (факт)");
        checkDate(work.getDebugStartFact(), work.getDebugEndFact(), "начала отладки (факт)", "конца отладки (факт)");
        checkDate(work.getReleaseStartFact(), work.getReleaseEndFact(), "начала тестирования релиза (факт)", "конца тестирования релиза (факт)");
        checkDate(work.getOpeStartFact(), work.getOpeEndFact(), "начала ОПЭ (факт)", "конца ОПЭ (факт)");

        checkDate(work.getAnaliseEndFact(), work.getIssuePrototypeFact(), "конца анализа (факт)", "конца разработки (факт)");
        checkDate(work.getIssuePrototypeFact(), work.getDebugEndFact(), "конца разработки (факт)", "конца отладки (факт)");
        checkDate(work.getDebugEndFact(), work.getReleaseEndFact(), "конца отладки (факт)", "конца тестирования релиза (факт)");
        checkDate(work.getReleaseEndFact(), work.getOpeEndFact(), "конца тестирования релиза (факт)", "конца ОПЭ (факт)");

        checkDate(work.getAnaliseStartPlan(), work.getAnaliseEndPlan(), "начала анализа (план)", "конца анализа (план)");
        checkDate(work.getDevelopStartPlan(), work.getIssuePrototypePlan(), "начала разработки (план)", "конца разработки (план)");
        checkDate(work.getDebugStartPlan(), work.getDebugEndPlan(), "начала отладки (план)", "конца отладки (план)");
        checkDate(work.getReleaseStartPlan(), work.getReleaseEndPlan(), "начала тестирования релиза (план)", "конца тестирования релиза (план)");
        checkDate(work.getOpeStartPlan(), work.getOpeEndPlan(), "начала ОПЭ (план)", "конца ОПЭ (план)");

        checkDate(work.getAnaliseEndPlan(), work.getIssuePrototypePlan(), "конца анализа (план)", "конца разработки (план)");
        checkDate(work.getIssuePrototypePlan(), work.getDebugEndPlan(), "конца разработки (план)", "конца отладки (план)");
        checkDate(work.getDebugEndPlan(), work.getReleaseEndPlan(), "конца отладки (план)", "конца тестирования релиза (план)");
        checkDate(work.getReleaseEndPlan(), work.getOpeEndPlan(), "конца тестирования релиза (план)", "конца ОПЭ (план)");


    }

    public void checkDate(Timestamp dateStart, Timestamp dateEnd, String dateStartMes, String dateEndMes) {
        if (dateStart != null && dateEnd != null && dateStart.compareTo(dateEnd) > 0) {
            throw new ResourceNotFoundRunTime("Дата " + dateEndMes + " не может быть раньше " + dateStartMes);
        }
    }

    @Transactional
    public Work saveWork(String login, Work work) {
        checkWork(work);
        Boolean ratedOld = null;
        Integer stageOld = null;
        String releaseNameOld = null;
        if (work.getId() != null) {
            Work workSave = workRepository.findById(work.getId()).orElse(null);
            if (workSave != null) {
                if (!workSave.getProjectId().equals(work.getProjectId())) {
                    throw new ResourceNotFoundRunTime("Нельзя сменить проект у ЗИ");
                }
                ratedOld = workSave.getRated();
                stageOld = workSave.getStageZi();
                releaseNameOld = workSave.getRelease() != null ? workSave.getRelease().getName() : null;
            }
        }
        updateWorkLastDevelop(work);
        work = workRepository.save(work);
        changeWork(login, work, stageOld, releaseNameOld, ratedOld);
        return work;
    }

    private String getMesChangRated(String login, WorkLittleInterface work) {
        return work.getRated() ? String.format("%s проставил <u><b>оценка выполнена</b></u> по ЗИ %s %s ", login, work.getCodeSap(), UrlWorkTime.getUrlRate(work.getId(), work.getName())) : String.format("%s <u><b>отменил оценку</b></u> по ЗИ %s %s ", login, work.getCodeSap(), UrlWorkTime.getUrlRate(work.getId(), work.getName()));
    }

    private void sendInform(String login, MessageType type, String text) {
        infoServiceIntegration.addMessage(new MessageInfoDto(new Timestamp(System.currentTimeMillis()), login, type, text));
    }

    public void updateWorkLastDevelop(Work work) {
        if (work.getId() == null) {
            return;
        }
        Work workSave;
        workSave = workRepository.findById(work.getId()).orElse(null);
        if (workSave == null) {
            return;
        }
        if (((work.getAnaliseEndFact() != null && !work.getAnaliseEndFact().equals(workSave.getAnaliseEndFact())) || (work.getIssuePrototypeFact() == null || !work.getIssuePrototypeFact().equals(workSave.getIssuePrototypeFact()))) || (work.getDevelopEndFact() == null || work.getDevelopEndFact().after(work.getIssuePrototypeFact()))) {
            SaveDateDevelopEndFact saveDateDevelopEndFact = checkSetDevelopEndDate(work, null);
            if (saveDateDevelopEndFact.isSave()) {
                if (work.getDevelopEndFact().before(saveDateDevelopEndFact.getDate())) {
                    saveDateDevelopEndFact.setDate(work.getDevelopEndFact());
                }

                work.setDevelopEndFact(saveDateDevelopEndFact.getDate());
            }
        }

    }

    public void deleteWork(Long id) {
        workLittleRepository.deleteById(id);
    }

    @Transactional
    public Page<@NonNull Work> findWorks(int page, int size, String name, String sort, Integer stageZiGe, Integer stageZiLe, Long codeSap, String codeZi, String task, Long releaseId, Long projectId) {
        return findAll(page, size, name, sort, stageZiGe, stageZiLe, codeSap, codeZi, task, releaseId, projectId);
    }


    public Page<@NonNull Work> findAll(Integer page, Integer size, String name, String sort, Integer stageZiGe, Integer stageZiLe, Long codeSap, String codeZi, String task, Long releaseId, Long projectId) {
        Specification<@NonNull Work> specification;
        if (sort != null && sort.length() > 8 && sort.startsWith("release.")) {
            specification = Specification.unrestricted();
        } else {
            specification = Specification.where(Specifications.queryDistinctTrue());
        }
        Release release = releaseService.findOptionalById(releaseId).orElse(null);
        specification = Specifications.like(specification, "name", name);
        specification = Specifications.like(specification, "codeZi", codeZi);
        specification = Specifications.like(specification, "task", task);
        specification = Specifications.eq(specification, "release", release);
        specification = Specifications.eq(specification, "codeSap", codeSap);
        specification = Specifications.eq(specification, "projectId", projectId);
        if (stageZiLe != null && stageZiLe.equals(stageZiGe)) {
            specification = Specifications.eq(specification, "stageZi", stageZiLe);

        } else {
            if (stageZiLe != null) {
                specification = Specifications.le(specification, "stageZi", stageZiLe);
            }
            if (stageZiGe != null) {
                specification = Specifications.ge(specification, "stageZi", stageZiGe);
            }
        }


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
        return workPage;
    }

    public Page<@NonNull WorkLittle> findWorkLittle(Integer page, Integer size, String name, String sort, Integer stageZiGe, Integer stageZiLe, Long codeSap, String codeZi, String task, List<Long> releaseIdArray) {
        Specification<@NonNull WorkLittle> specification;
        if (sort != null && sort.length() > 8 && sort.startsWith("release.")) {
            specification = Specification.unrestricted();
        } else {
            specification = Specification.where(Specifications.queryDistinctTrue());
        }
        specification = Specifications.like(specification, "codeZi", codeZi);
        specification = Specifications.like(specification, "name", name);
        specification = Specifications.like(specification, "task", task);
        if (releaseIdArray != null && !releaseIdArray.isEmpty()) {
            List<Object> releases = new ArrayList<>();
            for (Long releaseId : releaseIdArray) {
                Release release = releaseService.findOptionalById(releaseId).orElse(null);
                releases.add(release);

            }
            specification = Specifications.inO(specification, "release", releases);
        }
        specification = Specifications.eq(specification, "codeSap", codeSap);

        if (stageZiLe != null) {
            specification = Specifications.le(specification, "stageZi", stageZiLe);
        }
        if (stageZiGe != null) {
            specification = Specifications.ge(specification, "stageZi", stageZiGe);
        }
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
        return workPage;
    }

    public List<Work> getWorkList(String name, Integer stageZiGe, Integer stageZiLe, Long releaseId, String[] sort) {
        List<Work> works;
        Specification<@NonNull Work> specification = Specification.unrestricted();
        Sort sortWork = null;
        if (sort != null && sort.length > 0) {
            sortWork = Sort.by(sort[0]);
            for (int i = 1; i < sort.length; i++) {
                sortWork = sortWork.and(Sort.by(sort[i]));
            }
        }
        specification = Specifications.like(specification, "name", name);
        Release release = releaseService.findById(releaseId);
        specification = Specifications.eq(specification, "release", release);

        specification = Specifications.ge(specification, "stageZi", stageZiGe);
        specification = Specifications.le(specification, "stageZi", stageZiLe);

        if (sortWork == null) {
            works = workRepository.findAll(specification);
        } else {
            works = workRepository.findAll(specification, sortWork);
        }
        return works;
    }

    public void updWorkPlanTime(WorkPlanTime workPlanTime) {
        WorkStageDto workStageDto = rateServiceIntegration.getTimePlan(workPlanTime.getId());
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
                if (!rights.contains("ZI_EDIT")) {
                    throw new ResourceNotFoundRunTime("У вас нет права на редактирование ZI_EDIT");
                }
                break;
            case "create":
            case "stagecreate":
            case "criteriacreate":
            case "typecreate":
            case "zicreate":
                if (!rights.contains("ZI_CREATE")) {
                    throw new ResourceNotFoundRunTime("У вас нет права на редактирование ZI_CREATE");
                }
                break;
        }
        return true;
    }

    public WorkLittle setRated(String login, long id, Boolean rated) {
        WorkLittle workLittle = workLittleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundRunTime("Не найдена работа с таким Id"));
        if (workLittle.getRated() == null || !workLittle.getRated().equals(rated)) {
            workLittle.setRated(rated);
            workLittle = workLittleRepository.save(workLittle);
            sendInform(login, MessageType.ESTIMATION_WORK, getMesChangRated(login, workLittle));
        }
        return workLittle;

    }

    @Transactional
    public void setReleaseAndStageZi(String login, Long workId, Long releaseId, Integer stageZI) {
        WorkLittle workLittle = workLittleRepository.findById(workId).orElseThrow(() -> new ResourceNotFoundRunTime("Не найдено ЗИ"));
        Release release = releaseService.findOptionalById(releaseId).orElse(null);

        Boolean ratedOld = workLittle.getRated();
        Integer stageOld = workLittle.getStageZi();
        String releaseNameOld = workLittle.getRelease() != null ? workLittle.getRelease().getName() : null;
        workLittle.setStageZi(stageZI);
        workLittle.setRelease(release);

        workLittle = workLittleRepository.save(workLittle);
        changeWork(login, workLittle, stageOld, releaseNameOld, ratedOld);
    }

    private void changeWork(String login, WorkLittleInterface workLittle, Integer stageOld, String releaseNameOld, Boolean ratedOld) {
        String releaseNameNew = workLittle.getRelease() != null ? workLittle.getRelease().getName() : null;
        StringBuilder workEditText = new StringBuilder();
        workEditText.append(ChangeObj("этап ЗИ", stageOld, workLittle.getStageZi()));
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
        if (ratedOld != null && !ratedOld.equals(workLittle.getRated())) {
            sendInform(login, MessageType.ESTIMATION_WORK, getMesChangRated(login, workLittle));
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

    public SaveDateDevelopEndFact checkSetDevelopEndDate(Work work, Timestamp date) {
        SaveDateDevelopEndFact save = new SaveDateDevelopEndFact();
        if (date != null) {
            if ((work.getIssuePrototypeFact() == null || (work.getIssuePrototypeFact().after(date) || work.getIssuePrototypeFact().equals(date))) && work.getAnaliseEndFact() != null && (work.getAnaliseEndFact().equals(date) || work.getAnaliseEndFact().before(date)) && (work.getDevelopEndFact() == null || work.getDevelopEndFact().before(date))) {
                save.setDate(date).setSave(true);
            }
        } else {
            if (work.getIssuePrototypeFact() != null && work.getAnaliseEndFact() != null) {
                try {
                    date = getLastDateWorkBefore(work.getId(), work.getIssuePrototypeFact());
                    if (date == null) {
                        return save;
                    }
                    if (work.getAnaliseEndFact().equals(date) || work.getAnaliseEndFact().before(date)) {
                        save.setSave(true).setDate(date);
                    }
                } catch (ResourceNotFoundException ex) {
                    log.error(ex.getMessage());
                }
            }
        }
        return save;
    }

    public boolean setWorkDate(long id, Timestamp date) {
        Work work = workRepository.findById(id).orElseThrow(() -> new ResourceNotFoundRunTime("Не найдено ЗИ с id = " + id));
        boolean save1 = checkSetDevelopStartDate(work, date);
        if (save1) {
            work.setDevelopStartFact(date);
        }
        SaveDateDevelopEndFact save2 = checkSetDevelopEndDate(work, date);
        if (save2.isSave()) {
            work.setDevelopEndFact(save2.getDate());
        }
        if (save1 || save2.isSave()) {
            workRepository.save(work);
        }

        return save1 || save2.isSave();
    }

    private boolean checkSetDevelopStartDate(Work work, Timestamp date) {
        boolean save = false;
        if (date != null) {
            if (work.getDevelopStartFact() == null || work.getDevelopStartFact().after(date)) {
                save = true;
            }
        }
        return save;
    }

    private Timestamp getLastDateWorkBefore(Long workId, Timestamp date) throws ResourceNotFoundException {
        return taskServiceIntegration.getLastTime(workId, date, null);

    }
}