package ru.darujo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.darujo.dto.PageDto;
import ru.darujo.dto.UserDto;
import ru.darujo.dto.WorkFactDto;
import ru.darujo.dto.WorkRepDto;
import ru.darujo.exceptions.ResourceNotFoundException;
import ru.darujo.integration.TaskServiceIntegration;
import ru.darujo.integration.UserServiceIntegration;
import ru.darujo.model.Work;
import ru.darujo.model.WorkLittle;
import ru.darujo.repository.WorkLittleRepository;
import ru.darujo.repository.WorkRepository;
import ru.darujo.repository.specifications.WorkSpecifications;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class WorkService {
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

    public List<Work> findWorks(String name,
                                String sort,
                                Integer stageZiGe,
                                Integer stageZiLe,
                                Long codeSap,
                                String codeZi,
                                String task,
                                String release
    ) {
        return (List<Work>) findAll(null, null, name, sort, stageZiGe, stageZiLe, codeSap, codeZi, task, release);
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
        specification = getWorkSpecificationLike("name",name,specification);
        specification = getWorkSpecificationLike("codeZi",codeZi,specification);
        specification = getWorkSpecificationLike("task",task,specification);
        specification = getWorkSpecificationLike("release",release,specification);
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
    public Page<WorkLittle> findWorkLittle(int page, int size, String name, String sort, Integer stageZiGe, Integer stageZiLe) {
        Specification<WorkLittle> specification = Specification.where(WorkSpecifications.queryDistinctTrueLittle());
        specification = getWorkLittleSpecificationLike("name",name,specification);

        if (name != null) {
            specification = specification.and(WorkSpecifications.workLittleNameLike(name));
        }
        if (stageZiLe != null) {
            specification = specification.and(WorkSpecifications.workLittleStageZiLe(stageZiLe));
        }
        if (stageZiGe != null) {
            specification = specification.and(WorkSpecifications.workLittleStageZiGe(stageZiGe));
        }
        System.out.println("Page = " + page);
        Page<WorkLittle> workPage;
        if (sort == null) {
            workPage = workLittleRepository.findAll(specification, PageRequest.of(page - 1, size));
        } else {
            workPage = workLittleRepository.findAll(specification, PageRequest.of(page - 1, size, Sort.by(sort)));
        }
        return workPage;
    }


    public List<WorkRepDto> getWorkRep(String name, Boolean availWork, Integer stageZiGe, Integer stageZiLe, String release, String[] sort) {
        Specification<Work> specification = Specification.where(null);
        Sort sortWork = null;
        if (sort != null && sort.length > 0) {
            sortWork = Sort.by(sort[0]);
            for (int i = 1; i < sort.length; i++) {
                sortWork = sortWork.and(Sort.by(sort[i]));
            }
        }
        specification = getWorkSpecificationLike("name",name,specification);
        specification = getWorkSpecificationLike("release",release,specification);

        if (stageZiGe != null) {
            specification = specification.and(WorkSpecifications.stageZiGe(stageZiGe));
        }
        if (stageZiLe != null) {
            specification = specification.and(WorkSpecifications.stageZiLe(stageZiLe));
        }
        List<WorkRepDto> workRepDTOs = new ArrayList<>();
        List<Work> works;
        if (sortWork == null) {
            works = workRepository.findAll(specification);
        } else {
            works = workRepository.findAll(specification, sortWork);
        }
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
        Page<Work> workPage = findWorks(page, size, nameZi, sort, stageZiGe, stageZiLe, codeSap, codeZiSearch, task, release);
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
