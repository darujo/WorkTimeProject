package ru.darujo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.darujo.dto.ratestage.WorkStageDto;
import ru.darujo.dto.work.WorkPlanTime;
import ru.darujo.exceptions.ResourceNotFoundException;
import ru.darujo.integration.RateServiceIntegration;
import ru.darujo.model.Release;
import ru.darujo.model.Work;
import ru.darujo.model.WorkLittle;
import ru.darujo.repository.WorkLittleRepository;
import ru.darujo.repository.WorkRepository;
import ru.darujo.repository.specifications.WorkSpecifications;

import javax.transaction.Transactional;
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

    public Work findById(long id) {
        return workRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Задача не найден"));
    }

    public Optional<WorkLittle> findLittleById(long id) {
        return workLittleRepository.findById(id);
    }

    public void checkWork(Work work) {
        Release release;
        if(work.getId() != null) {
            release = workRepository.findById(work.getId()).orElseThrow(() -> new ResourceNotFoundException("ЗИ пропало(((")).getRelease();
            if (release!= null ){
                if(release.getIssuingReleaseFact() != null) {
                    if(work.getRelease() == null || !work.getRelease().getId().equals(release.getId()) ) {
                        throw new ResourceNotFoundException("Нельзя исключать ЗИ из релиза. Релиз выпущен.");
                    }
                } else {
                    if(work.getRelease() != null && !work.getRelease().getId().equals(release.getId()) ) {
                        release = releaseService.findById(work.getRelease().getId());
                        if (release.getIssuingReleaseFact() != null) {
                            throw new ResourceNotFoundException("Нельзя включать ЗИ в выпущеный релиз");
                        }
                    }
                }
            } else {
                if(work.getRelease() != null && work.getRelease().getId() != null){
                    release = releaseService.findById(work.getRelease().getId());
                    if (release.getIssuingReleaseFact() !=  null){
                        throw new ResourceNotFoundException("Нельзя включать ЗИ в выпущеный релиз");
                    }
                }
            }
        } else {
            if(work.getRelease() != null && work.getRelease().getId() != null){
                release = releaseService.findById(work.getRelease().getId());
                if (release.getIssuingReleaseFact() !=  null){
                    throw new ResourceNotFoundException("Нельзя включать ЗИ в выпущеный релиз");
                }
            }
        }
    }
    public Work saveWork(Work work) {
        checkWork(work);
        return workRepository.save(work);
    }

    public void deleteWork(Long id) {
        workLittleRepository.deleteById(id);
    }

    @Transactional
    public Page<Work> findWorks(int page,
                                int size,
                                String name,
                                String sort,
                                Integer stageZiGe,
                                Integer stageZiLe,
                                Long codeSap,
                                String codeZi,
                                String task,
                                Long releaseId
    ) {
        return (Page<Work>) findAll(page, size, name, sort, stageZiGe, stageZiLe, codeSap, codeZi, task, releaseId);
    }


    public Iterable<Work> findAll(Integer page,
                                  Integer size,
                                  String name,
                                  String sort,
                                  Integer stageZiGe,
                                  Integer stageZiLe,
                                  Long codeSap,
                                  String codeZi,
                                  String task,
                                  Long releaseId
    ) {
        Specification<Work> specification;
        if (sort != null && sort.length() > 8 && sort.startsWith("release.")) {
            specification = Specification.where(null);
        } else {
            specification = Specification.where(WorkSpecifications.queryDistinctTrue());
        }
        specification = getWorkSpecificationLike("name", name, specification);
        specification = getWorkSpecificationLike("codeZI", codeZi, specification);
        specification = getWorkSpecificationLike("task", task, specification);
        specification = WorkSpecifications.eq(specification, "release", releaseId);
        specification = WorkSpecifications.eq(specification, "codeSap", codeSap);

        if (stageZiLe != null && stageZiLe.equals(stageZiGe)) {
            specification = specification.and(WorkSpecifications.stageZiEq(stageZiLe));

        } else {
            if (stageZiLe != null) {
                specification = specification.and(WorkSpecifications.stageZiLe(stageZiLe));
            }
            if (stageZiGe != null) {
                specification = specification.and(WorkSpecifications.stageZiGe(stageZiGe));
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

    private Specification<Work> getWorkSpecificationLike(String field, String value, Specification<Work> specification) {
        if (value != null && !value.equals("")) {
            specification = specification.and(WorkSpecifications.like(field, value));
        }
        return specification;
    }

    private Specification<WorkLittle> getWorkLittleSpecificationLike(String field, String value, Specification<WorkLittle> specification) {
        if (value != null && !value.equals("")) {
            specification = specification.and(WorkSpecifications.likeLittle(field, value));
        }
        return specification;
    }

    public Iterable<WorkLittle> findWorkLittle(Integer page,
                                               Integer size,
                                               String name,
                                               String sort,
                                               Integer stageZiGe,
                                               Integer stageZiLe,
                                               Long codeSap,
                                               String codeZi,
                                               String task,
                                               Long releaseId) {
        Specification<WorkLittle> specification;
        if (sort != null && sort.length() > 8 && sort.startsWith("release.")) {
            specification = Specification.where(null);
        } else {
            specification = Specification.where(WorkSpecifications.queryDistinctTrueLittle());
        }
        specification = getWorkLittleSpecificationLike("name", name, specification);
        specification = getWorkLittleSpecificationLike("codeZI", codeZi, specification);
        specification = getWorkLittleSpecificationLike("task", task, specification);
        specification = WorkSpecifications.eqLittle(specification, "release", releaseId);
        specification = WorkSpecifications.eqLittle(specification, "codeSap", codeSap);

        if (stageZiLe != null) {
            specification = specification.and(WorkSpecifications.workLittleStageZiLe(stageZiLe));
        }
        if (stageZiGe != null) {
            specification = specification.and(WorkSpecifications.workLittleStageZiGe(stageZiGe));
        }
        System.out.println("Page = " + page);
        Iterable<WorkLittle> workPage;
        if (page == null) {
            if (sort == null) {
                workPage = workLittleRepository.findAll(specification);
            } else {
                workPage = workLittleRepository.findAll(specification, Sort.by(sort));
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
        specification = getWorkSpecificationLike("name", name, specification);
        specification = WorkSpecifications.eq(specification,"release", release);

        if (stageZiGe != null) {
            specification = specification.and(WorkSpecifications.stageZiGe(stageZiGe));
        }
        if (stageZiLe != null) {
            specification = specification.and(WorkSpecifications.stageZiLe(stageZiLe));
        }

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
                    throw new ResourceNotFoundException("У вас нет права на редактирование ZI_EDIT");
                }
                break;
            case "create":
            case "stagecreate":
            case "criteriacreate":
                if (!rightCreate) {
                    throw new ResourceNotFoundException("У вас нет права на редактирование ZI_CREATE");
                }
                break;
        }
        return true;
    }


}
