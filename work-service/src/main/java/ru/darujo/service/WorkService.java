package ru.darujo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.darujo.dto.ratestage.WorkStageDto;
import ru.darujo.dto.work.WorkPlanTime;
import ru.darujo.exceptions.ResourceNotFoundException;
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.integration.RateServiceIntegration;
import ru.darujo.integration.TaskServiceIntegration;
import ru.darujo.model.Release;
import ru.darujo.model.Work;
import ru.darujo.model.WorkLittle;
import ru.darujo.repository.WorkLittleRepository;
import ru.darujo.repository.WorkRepository;
import ru.darujo.specifications.Specifications;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.*;

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
                            throw new ResourceNotFoundRunTime("Нельзя включать ЗИ в выпущеный релиз");
                        }
                    }
                }
            } else {
                if (work.getRelease() != null && work.getRelease().getId() != null) {
                    release = releaseService.findById(work.getRelease().getId());
                    if (release.getIssuingReleaseFact() != null) {
                        throw new ResourceNotFoundRunTime("Нельзя включать ЗИ в выпущеный релиз");
                    }
                }
            }
        } else {
            if (work.getRelease() != null && work.getRelease().getId() != null) {
                release = releaseService.findById(work.getRelease().getId());
                if (release.getIssuingReleaseFact() != null) {
                    throw new ResourceNotFoundRunTime("Нельзя включать ЗИ в выпущеный релиз");
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

    public Work saveWork(Work work) {
        checkWork(work);
        updateWorkLastDevelop(work);
        return workRepository.save(work);

    }

    public void updateWorkLastDevelop(Work work) {
        if (work.getId() == null) {
            return;
        }
        try {
            Timestamp date = getLastDateWorkBefore(work.getId(), work.getIssuePrototypeFact());
            work.setDevelopEndFact(date);
        } catch (ResourceNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void deleteWork(Long id) {
        workLittleRepository.deleteById(id);
    }

    @Transactional
    public Page<Work> findWorks(int page, int size, String name, String sort, Integer stageZiGe, Integer stageZiLe, Long codeSap, String codeZi, String task, Long releaseId) {
        return (Page<Work>) findAll(page, size, name, sort, stageZiGe, stageZiLe, codeSap, codeZi, task, releaseId);
    }


    public Iterable<Work> findAll(Integer page, Integer size, String name, String sort, Integer stageZiGe, Integer stageZiLe, Long codeSap, String codeZi, String task, Long releaseId) {
        Specification<Work> specification;
        if (sort != null && sort.length() > 8 && sort.startsWith("release.")) {
            specification = Specification.where(null);
        } else {
            specification = Specification.where(Specifications.queryDistinctTrue());
        }
        specification = Specifications.like(specification, "name", name);
        specification = Specifications.like(specification, "codeZI", codeZi);
        specification = Specifications.like(specification, "task", task);
        specification = Specifications.eq(specification, "release", releaseId);
        specification = Specifications.eq(specification, "codeSap", codeSap);

        if (stageZiLe != null && stageZiLe.equals(stageZiGe)) {
            specification = Specifications.eq(specification, "stageZI", stageZiLe);

        } else {
            if (stageZiLe != null) {
                specification = Specifications.le(specification, "stageZI", stageZiLe);
            }
            if (stageZiGe != null) {
                specification = Specifications.ge(specification, "stageZI", stageZiGe);
            }
        }


        System.out.println("Page = " + page);
        Iterable<Work> workPage;
        if (sort == null) {
            if (page != null && size != null) {
                workPage = workRepository.findAll(specification, PageRequest.of(page - 1, size));
            } else {
                workPage = workRepository.findAll(specification);
            }

        } else {
            if (page != null && size != null) {
                workPage = workRepository.findAll(specification, PageRequest.of(page - 1, size, Sort.Direction.ASC, sort));
            } else {
                workPage = workRepository.findAll(specification, Sort.by(sort));
            }
        }
        return workPage;
    }

    public Page<WorkLittle> findWorkLittle(Integer page, Integer size, String name, String sort, Integer stageZiGe, Integer stageZiLe, Long codeSap, String codeZi, String task, Long releaseId) {
        Specification<WorkLittle> specification;
        if (sort != null && sort.length() > 8 && sort.startsWith("release.")) {
            specification = Specification.where(null);
        } else {
            specification = Specification.where(Specifications.queryDistinctTrue());
        }
        specification = Specifications.like(specification, "codeZI", codeZi);
        specification = Specifications.like(specification, "name", name);
        specification = Specifications.like(specification, "task", task);
        specification = Specifications.eq(specification, "release", releaseId);
        specification = Specifications.eq(specification, "codeSap", codeSap);

        if (stageZiLe != null) {
            specification = Specifications.le(specification, "stageZI", stageZiLe);
        }
        if (stageZiGe != null) {
            specification = Specifications.ge(specification, "stageZI", stageZiGe);
        }
        System.out.println("Page = " + page);
        Page<WorkLittle> workPage;
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

    public List<Work> getWorkList(String name, Integer stageZiGe, Integer stageZiLe, Long release, String[] sort) {
        List<Work> works;
        Specification<Work> specification = Specification.where(null);
        Sort sortWork = null;
        if (sort != null && sort.length > 0) {
            sortWork = Sort.by(sort[0]);
            for (int i = 1; i < sort.length; i++) {
                sortWork = sortWork.and(Sort.by(sort[i]));
            }
        }
        specification = Specifications.like(specification, "name", name);
        specification = Specifications.eq(specification, "release", release);

        specification = Specifications.ge(specification, "stageZI", stageZiGe);
        specification = Specifications.le(specification, "stageZI", stageZiLe);

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

    public boolean checkRight(String right, boolean rightEdit, boolean rightCreate) {
        right = right.toLowerCase();
        switch (right) {
            case "edit":
            case "stageedit":
            case "criteriaedit":
                if (!rightEdit) {
                    throw new ResourceNotFoundRunTime("У вас нет права на редактирование ZI_EDIT");
                }
                break;
            case "create":
            case "stagecreate":
            case "criteriacreate":
                if (!rightCreate) {
                    throw new ResourceNotFoundRunTime("У вас нет права на редактирование ZI_CREATE");
                }
                break;
        }
        return true;
    }


    public boolean setWorkDate(long id, Timestamp date) {
        Work work = workRepository.findById(id).orElseThrow(() -> new ResourceNotFoundRunTime("Не найдено ЗИ с id = " + id));
        boolean save = false;
        if (date != null) {
            if (work.getDevelopStartFact() == null || work.getDevelopStartFact().after(date)) {
                save = true;
            }
            if ((work.getIssuePrototypeFact() == null
                    || work.getIssuePrototypeFact().after(date))
                    && work.getAnaliseEndFact() != null
                    && work.getAnaliseEndFact().before(date)
                    && (work.getDevelopEndFact() == null || work.getDevelopEndFact().before(date))) {
                save = true;
            }
        } else {
            if (work.getIssuePrototypeFact() != null
                    && work.getAnaliseEndFact() != null) {
                try {
                    date = getLastDateWorkBefore(work.getId(), work.getIssuePrototypeFact());
                    if (work.getAnaliseEndFact().before(date)) {
                        save = true;
                    }
                } catch (ResourceNotFoundException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }

        if (save) {
            work.setDevelopStartFact(date);
            workRepository.save(work);
        }
        return save;
    }

    private Timestamp getLastDateWorkBefore(Long workId, Timestamp date) throws ResourceNotFoundException {
        return taskServiceIntegration.getLastTime(workId, date);

    }
}