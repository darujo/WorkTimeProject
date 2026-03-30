package ru.darujo.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.darujo.model.WorkLittle;
import ru.darujo.model.WorkLittleFull;
import ru.darujo.model.WorkProjectLittle;
import ru.darujo.repository.WorkProjectLittleRepository;
import ru.darujo.specifications.Specifications;

import java.util.List;

@Service
@Slf4j
public class WorkProjectLittleService {
    private WorkProjectLittleRepository workProjectLittleRepository;

    @Autowired
    public void setWorkProjectLittleRepository(WorkProjectLittleRepository workProjectLittleRepository) {
        this.workProjectLittleRepository = workProjectLittleRepository;
    }

    public WorkProjectLittle getWorkProjectOrEmpty(WorkLittle work, @NonNull Long projectId) {
        WorkProjectLittle workProject = getWorkProject(work, projectId);
        if (workProject == null) {
            workProject = new WorkProjectLittle(projectId, work);
        }
        return workProject;
    }

    public WorkProjectLittle getWorkProject(WorkLittle work, Long projectId) {
        if(projectId== null){
            return null;
        }
        return workProjectLittleRepository.findByWorkLittleAndProjectId(work, projectId);
    }

    public WorkProjectLittle save(WorkProjectLittle workProject) {
        return workProjectLittleRepository.save(workProject);
    }

    public List<Long> getListWorkId(String task, Integer stageZiLe, Integer stageZiGe) {
        if (task == null && stageZiLe == null && stageZiGe == null) {
            return null;
        }
        return getListWork(null, null, null, stageZiGe, stageZiLe, task, null, null).stream().map(workProjectLittle -> workProjectLittle.getWorkLittle().getId()).toList();
    }

    public Page<WorkProjectLittle> getListWork(Integer page, Integer size, String sort, Integer stageZiGe, Integer stageZiLe, String task, Long projectId, List<WorkLittle> workLittleList) {
        Specification<WorkProjectLittle> specification = getWorkProjectLittleSpecification(stageZiGe, stageZiLe, task, projectId, workLittleList);
        return Specifications.findAll(workProjectLittleRepository, page, size, specification, sort);
    }

    private static Specification<WorkProjectLittle> getWorkProjectLittleSpecification(Integer stageZiGe, Integer stageZiLe, String task, Long projectId, List<WorkLittle> workLittleList) {
        Specification<WorkProjectLittle> specification = Specification.unrestricted();
        specification = Specifications.like(specification, "task", task);
        specification = Specifications.in(specification, "workLittle", workLittleList);
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
        return specification;
    }

    public Page<@NonNull WorkLittleFull> getWorkFull(Integer page, Integer size, String sort, Integer stageZiGe, Integer stageZiLe, String task, Long projectId, List<WorkLittle> workLittleList) {

        return getListWork(page, size, sort, stageZiGe, stageZiLe, task, projectId, workLittleList)
                .map(workProject -> new WorkLittleFull(workProject.getWorkLittle(), workProject));
    }


}
