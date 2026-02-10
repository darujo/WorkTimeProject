package ru.darujo.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.darujo.model.Release;
import ru.darujo.model.WorkLittle;
import ru.darujo.model.WorkLittleFull;
import ru.darujo.model.WorkProjectLittle;
import ru.darujo.repository.WorkProjectLittleRepository;
import ru.darujo.specifications.Specifications;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class WorkProjectLittleService {
    private WorkProjectLittleRepository workProjectLittleRepository;

    @Autowired
    public void setWorkProjectLittleRepository(WorkProjectLittleRepository workProjectLittleRepository) {
        this.workProjectLittleRepository = workProjectLittleRepository;
    }

    private ReleaseService releaseService;

    @Autowired
    public void setReleaseService(ReleaseService releaseService) {
        this.releaseService = releaseService;
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

    public List<Long> getListWorkId(String task, Long releaseId, Integer stageZiLe, Integer stageZiGe, List<Long> releaseIdArray) {
        if (task == null && releaseId == null && stageZiLe == null && stageZiGe == null) {
            return null;
        }
        return getListWork(null, null, null, stageZiGe, stageZiLe, task, releaseIdArray, null, null, null).stream().map(workProjectLittle -> workProjectLittle.getWorkLittle().getId()).toList();
    }

    public Page<WorkProjectLittle> getListWork(Integer page, Integer size, String sort, Integer stageZiGe, Integer stageZiLe, String task, List<Long> releaseIdArray, Long projectId, Long releaseId, List<WorkLittle> workLittleList) {
        Specification<WorkProjectLittle> specification = Specification.unrestricted();
        specification = Specifications.like(specification, "task", task);
        specification = Specifications.in(specification, "work", workLittleList);
        specification = Specifications.eq(specification, "projectId", projectId);

        if (releaseId != null) {
            Release release = releaseService.findOptionalById(releaseId).orElse(null);
            specification = Specifications.eq(specification, "release", release);
        }
        if (releaseIdArray != null && !releaseIdArray.isEmpty()) {
            List<Object> releases = new ArrayList<>();
            for (Long releaseIdIn : releaseIdArray) {
                Release release = releaseService.findOptionalById(releaseIdIn).orElse(null);
                releases.add(release);

            }
            specification = Specifications.inO(specification, "release", releases);
        }
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
        Page<@NonNull WorkProjectLittle> workPage;
        if (sort == null) {
            if (page != null && size != null) {
                workPage = workProjectLittleRepository.findAll(specification, PageRequest.of(page - 1, size));
            } else {
                workPage = new PageImpl<>(workProjectLittleRepository.findAll(specification));
            }

        } else {
            if (page != null && size != null) {
                workPage = workProjectLittleRepository.findAll(specification, PageRequest.of(page - 1, size, Sort.Direction.ASC, sort));
            } else {
                workPage = new PageImpl<>(workProjectLittleRepository.findAll(specification, Sort.by(sort)));
            }
        }
        return workPage;
    }

    public Page<@NonNull WorkLittleFull> getWorkFull(Integer page, Integer size, String sort, Integer stageZiGe, Integer stageZiLe, String task, List<Long> releaseIdArray, Long projectId, Long releaseId, List<WorkLittle> workLittleList) {

        return getListWork(page, size, sort, stageZiGe, stageZiLe, task, releaseIdArray, projectId, releaseId, workLittleList)
                .map(workProject -> new WorkLittleFull(workProject.getWorkLittle(), workProject));
    }


}
