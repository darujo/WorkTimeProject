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
import ru.darujo.repository.WorkRepository;
import ru.darujo.repository.specifications.WorkSpecifications;

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

    public Optional<Work> findById(long id) {
        return workRepository.findById(id);
    }

    public Work saveWork(Work work) {
        return workRepository.save(work);
    }

    public void deleteWork(Long id) {
        workRepository.deleteById(id);
    }

    public Page<Work> findWorks(int page, int size, String name, String sort,Integer stageZiLt) {
        Specification<Work> specification = Specification.where(null);

        if (name != null) {
            specification = specification.and(WorkSpecifications.workNameLike(name));
        }
        if (stageZiLt != null) {
            specification = specification.and(WorkSpecifications.stageZiLt(stageZiLt));
        }
        System.out.println("Page = " + page);
        Page<Work> workPage;
        if(sort == null) {
            workPage = workRepository.findAll(specification, PageRequest.of(page - 1, size));
        }
        else {
            workPage = workRepository.findAll(specification, PageRequest.of(page - 1, size, Sort.by(sort)));
        }
        return workPage;
    }

    public List<WorkRepDto> getWorkRep(String nikName) {
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
                                        nikName,
                                        null,
                                        work.getAnaliseEndFact()),
                                taskServiceIntegration.getTimeWork(
                                        work.getId(),
                                        nikName,
                                        work.getAnaliseEndFact(),
                                        work.getDevelopEndFact()),
                                taskServiceIntegration.getTimeWork(work.getId(),
                                        nikName,
                                        work.getDevelopEndFact(),
                                        work.getDebugEndFact()),
                                taskServiceIntegration.getTimeWork(work.getId(),
                                        nikName,
                                        work.getDebugEndFact(),
                                        work.getReleaseEndFact()),
                                taskServiceIntegration.getTimeWork(work.getId(),
                                        nikName,
                                        work.getReleaseEndFact(),
                                        work.getOpeEndFact()),
                                taskServiceIntegration.getTimeWork(work.getId(),
                                        nikName,
                                        work.getOpeEndFact(),
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
                            UserDto userDto;
                            try {
                                userDto = userServiceIntegration.getUserDto(null, user);
                            }
                            catch (ResourceNotFoundException ex){
                                userDto = new UserDto(-1L,"","логином", "Не найден пользователь с",user);
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
                                                    work.getDevelopEndFact()),
                                            taskServiceIntegration.getTimeWork(work.getId(),
                                                    user,
                                                    work.getDevelopEndFact(),
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
