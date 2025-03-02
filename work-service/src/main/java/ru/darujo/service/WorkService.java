package ru.darujo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.darujo.dto.WorkFactDto;
import ru.darujo.dto.WorkRepDto;
import ru.darujo.integration.TaskServiceIntegration;
import ru.darujo.model.Work;
import ru.darujo.repository.WorkRepository;
import ru.darujo.repository.specifications.WorkSpecifications;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class WorkService {
    private TaskServiceIntegration taskServiceIntegration;

    @Autowired
    public void setWorkTimeServiceIntegration(TaskServiceIntegration taskServiceIntegration) {
        this.taskServiceIntegration = taskServiceIntegration;
    }

    private WorkRepository workRepository;

    @Autowired
    public void setWorkRepository(WorkRepository workRepository) {
        this.workRepository = workRepository;
    }

    private int size = 10;
    private Page<Work> workPage;

    public Optional<Work> findById(long id) {
        return workRepository.findById(id);
    }

    public Work saveWork(Work work) {
        size = -1;
        return workRepository.save(work);
    }

    public void deleteWork(Long id) {
        workRepository.deleteById(id);
        size = -1;
    }

    public Page<Work> findWorks(int page, int size, String name) {
        if (workPage != null && page == 1 && this.size == size && name == null) {
            return workPage;
        }
        Specification<Work> specification = Specification.where(null);

        if (name != null) {
            specification = specification.and(WorkSpecifications.workNameLike(name));
        }
        workPage = workRepository.findAll(specification, PageRequest.of(page - 1, size));
        this.size = size;
        return workPage;
    }

    public List<WorkRepDto> getWorkRep(String userName) {
        List<WorkRepDto> workRepDtos = new ArrayList<>();
        workRepository.findAll().forEach(work ->
                workRepDtos.add(
                        new WorkRepDto(
                                work.getId(),
                                work.getCodeZI(),
                                work.getName(),
                                work.getPlanDateStage0(),
                                work.getFactDateStage0(),
                                work.getStartTaskPlan(),
                                work.getStartTaskFact(),
                                work.getDateStartDevelopPlan(),
                                work.getDateStartDevelop(),
                                work.getLaborDevelop(),
                                work.getDateStartDebugPlan(),
                                work.getDateStartDebug(),
                                work.getDateStartReleasePlan(),
                                work.getDateStartRelease(),
                                work.getLaborDebug(),
                                work.getRelease(),
                                work.getIssuingReleasePlan(),
                                work.getIssuingReleaseFact(),
                                work.getDateStartOPEPlan(),
                                work.getDateStartOPE(),
                                work.getLaborRelease(),
                                work.getDateStartWenderPlan(),
                                work.getDateStartWender(),
                                work.getLaborOPE(),
                                taskServiceIntegration.getTimeWork(
                                        work.getId(),
                                        userName,
                                        null,
                                        work.getDateStartDevelop()),
                                taskServiceIntegration.getTimeWork(work.getId(),
                                        userName,
                                        work.getDateStartDevelop(),
                                        work.getDateStartDebug()),
                                taskServiceIntegration.getTimeWork(work.getId(),
                                        userName,
                                        work.getDateStartDebug(),
                                        work.getDateStartRelease()),
                                taskServiceIntegration.getTimeWork(work.getId(),
                                        userName,
                                        work.getDateStartRelease(),
                                        work.getDateStartOPE()),
                                taskServiceIntegration.getTimeWork(work.getId(),
                                        userName,
                                        work.getDateStartOPE(),
                                        work.getDateStartWender()),
                                taskServiceIntegration.getTimeWork(work.getId(),
                                        userName,
                                        work.getDateStartWender(),
                                        null)
                        )
                )
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
                            workFactDtos.add(
                                    new WorkFactDto(
                                            num.incrementAndGet(),
                                            codeZi,
                                            name,
                                            users.size(),
                                            user,
                                            taskServiceIntegration.getTimeWork(
                                                    work.getId(),
                                                    user,
                                                    null,
                                                    work.getDateStartDevelop()),
                                            taskServiceIntegration.getTimeWork(work.getId(),
                                                    user,
                                                    work.getDateStartDevelop(),
                                                    work.getDateStartDebug()),
                                            taskServiceIntegration.getTimeWork(work.getId(),
                                                    user,
                                                    work.getDateStartDebug(),
                                                    work.getDateStartRelease()),
                                            taskServiceIntegration.getTimeWork(work.getId(),
                                                    user,
                                                    work.getDateStartRelease(),
                                                    work.getDateStartOPE()),
                                            taskServiceIntegration.getTimeWork(work.getId(),
                                                    user,
                                                    work.getDateStartOPE(),
                                                    work.getDateStartWender()),
                                            taskServiceIntegration.getTimeWork(work.getId(),
                                                    user,
                                                    work.getDateStartWender(),
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
