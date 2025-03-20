package ru.darujo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
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
                                String task
    ) {
        Specification<Work> specification = Specification.where(null);

        if (name != null && !name.equals("")) {
            specification = specification.and(WorkSpecifications.workNameLike(name));
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
        if (codeSap != null) {
            specification = specification.and(WorkSpecifications.codeSapEq(codeSap));
        }
        if (codeZi != null && !codeZi.equals("")) {
            specification = specification.and(WorkSpecifications.codeZiLike(codeZi));
        }
        if (task != null && !task.equals("")) {
            specification = specification.and(WorkSpecifications.taskLike(task));
        }


        System.out.println("Page = " + page);
        Page<Work> workPage;
        if (sort == null) {
            workPage = workRepository.findAll(specification, PageRequest.of(page - 1, size));
        } else {
            workPage = workRepository.findAll(specification, PageRequest.of(page - 1, size, Sort.by(sort)));
        }
        return workPage;
    }

    public Page<WorkLittle> findWorkLittle(int page, int size, String name, String sort, Integer stageZiGe, Integer stageZiLe) {
        Specification<WorkLittle> specification = Specification.where(null);

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


    public List<WorkRepDto> getWorkRep(String name) {
        Specification<Work> specification = Specification.where(null);

        if (name != null) {
            specification = specification.and(WorkSpecifications.workNameLike(name));
        }

        List<WorkRepDto> workRepDtos = new ArrayList<>();
        workRepository.findAll(specification).forEach(work ->
                {
                    Timestamp timestampDevolop;
                    if (work.getAnaliseEndFact() == null
                            && work.getDevelopEndFact() == null
                            && work.getDebugEndFact() == null
                            && work.getReleaseEndFact() == null
                            && work.getOpeEndFact() == null) {
                        timestampDevolop = new Timestamp(new Date().getTime());
                    } else {
                        timestampDevolop = work.getDevelopEndFact();
                    }
                    workRepDtos.add(
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
                                    taskServiceIntegration.getTimeWork(work.getId(),
                                            null,
                                            null,
                                            work.getAnaliseEndFact()),
                                    taskServiceIntegration.getTimeWork(
                                            work.getId(),
                                            null,
                                            work.getAnaliseEndFact(),
                                            timestampDevolop),
                                    taskServiceIntegration.getTimeWork(work.getId(),
                                            null,
                                            timestampDevolop,
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
        );
        return workRepDtos;
    }

    public List<WorkFactDto> getWorkFactRep(String userName) {
        AtomicInteger num = new AtomicInteger();
        List<WorkFactDto> workFactDtos = new ArrayList<>();
        workRepository.findAll().forEach(work -> {
                    Set<String> users = taskServiceIntegration.getListUser(work.getId()).getList();
                    if (userName != null) {
                        if (users.contains(userName)) {
                            users = new HashSet<>();
                            users.add(userName);
                        } else {
                            users = null;
                        }
                    }
                    if (users != null) {
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
                            Timestamp timestampDevolop;
                            if (work.getAnaliseEndFact() == null
                                    && work.getDevelopEndFact() == null
                                    && work.getDebugEndFact() == null
                                    && work.getReleaseEndFact() == null
                                    && work.getOpeEndFact() == null) {
                                timestampDevolop = new Timestamp(new Date().getTime());
                            } else {
                                timestampDevolop = work.getDevelopEndFact();
                            }
                            workFactDtos.add(
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
                                                    work.getAnaliseEndFact()),
                                            taskServiceIntegration.getTimeWork(
                                                    work.getId(),
                                                    user,
                                                    work.getAnaliseEndFact(),
                                                    timestampDevolop),
                                            taskServiceIntegration.getTimeWork(work.getId(),
                                                    user,
                                                    timestampDevolop,
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
                    }
                }
        );
        return workFactDtos;
    }


}
