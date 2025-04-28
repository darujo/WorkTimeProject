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
import ru.darujo.model.Work;
import ru.darujo.model.WorkLittle;
import ru.darujo.repository.WorkLittleRepository;
import ru.darujo.repository.WorkRepository;
import ru.darujo.repository.specifications.WorkSpecifications;

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

    public Optional<Work> findById(long id) {
        return workRepository.findById(id);
    }

    public Optional<WorkLittle> findLittleById(long id) {
        return workLittleRepository.findById(id);
    }

    public Work saveWork(Work work) {
        return workRepository.save(work);
    }

    public void deleteWork(Long id) {
        workLittleRepository.deleteById(id);
    }

    public Page<Work> findWorks(int page,
                                int size,
                                String name,
                                String sort,
                                Integer stageZiGe,
                                Integer stageZiLe,
                                Long codeSap,
                                String codeZi,
                                String task,
                                String release
    ) {
        return (Page<Work>) findAll(page, size, name, sort, stageZiGe, stageZiLe, codeSap, codeZi, task, release);
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
                                  String release
    ) {
        Specification<Work> specification = Specification.where(WorkSpecifications.queryDistinctTrue());
        specification = getWorkSpecificationLike("name", name, specification);
        specification = getWorkSpecificationLike("codeZI", codeZi, specification);
        specification = getWorkSpecificationLike("task", task, specification);
        specification = getWorkSpecificationLike("release", release, specification);
        if (codeSap != null) {
            specification = specification.and(WorkSpecifications.codeSapEq(codeSap));
        }

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
                                               String release) {
        Specification<WorkLittle> specification = Specification.where(WorkSpecifications.queryDistinctTrueLittle());
        specification = getWorkLittleSpecificationLike("name", name, specification);
        specification = getWorkLittleSpecificationLike("codeZI", codeZi, specification);
        specification = getWorkLittleSpecificationLike("task", task, specification);
        specification = getWorkLittleSpecificationLike("release", release, specification);

        if (codeSap != null) {
            specification = specification.and(WorkSpecifications.codeSapEqLittle(codeSap));
        }

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

    public List<Work> getWorkList(String name, Integer stageZiGe, Integer stageZiLe, String release, String[] sort) {
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
        specification = getWorkSpecificationLike("release", release, specification);

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
