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
import ru.darujo.exceptions.ResourceNotFoundException;
import ru.darujo.integration.TaskServiceIntegration;
import ru.darujo.model.*;
import ru.darujo.repository.WorkProjectRepository;
import ru.darujo.specifications.Specifications;

import java.sql.Timestamp;
import java.util.List;

@Service
@Slf4j
public class WorkProjectService {
    private WorkProjectRepository workProjectRepository;

    @Autowired
    public void setWorkProjectRepository(WorkProjectRepository workProjectRepository) {
        this.workProjectRepository = workProjectRepository;
    }

    TaskServiceIntegration taskServiceIntegration;

    @Autowired
    public void setTaskServiceIntegration(TaskServiceIntegration taskServiceIntegration) {
        this.taskServiceIntegration = taskServiceIntegration;
    }

    private ReleaseService releaseService;

    @Autowired
    public void setReleaseService(ReleaseService releaseService) {
        this.releaseService = releaseService;
    }

    public WorkProject getWorkProjectOrEmpty(Work work, @NonNull Long projectId) {
        WorkProject workProject = workProjectRepository.findByWorkAndProjectId(work, projectId);
        if (workProject == null) {
            workProject = new WorkProject(projectId, work);
        }
        return workProject;
    }

    public WorkProject getWorkProject(Work work, @NonNull Long projectId) {
        return workProjectRepository.findByWorkAndProjectId(work, projectId);
    }

    public void updateWorkLastDevelop(WorkProject workProject) {
        if (workProject.getId() == null) {
            return;
        }

        WorkProject workSave = workProjectRepository.findById(workProject.getId()).orElse(null);
        if (workSave == null) {
            return;
        }
        if (((workProject.getAnaliseEndFact() != null
                && !workProject.getAnaliseEndFact().equals(workSave.getAnaliseEndFact()))
                ||
                (workProject.getIssuePrototypeFact() == null
                        || !workProject.getIssuePrototypeFact().equals(workSave.getIssuePrototypeFact()))
        )
                ||
                (workProject.getDevelopEndFact() == null
                        || workProject.getDevelopEndFact().after(workProject.getIssuePrototypeFact()))
        ) {
            SaveDateDevelopEndFact saveDateDevelopEndFact = checkSetDevelopEndDate(workProject, null);
            if (saveDateDevelopEndFact.isSave()) {
                if (workProject.getDevelopEndFact().before(saveDateDevelopEndFact.getDate())) {
                    saveDateDevelopEndFact.setDate(workProject.getDevelopEndFact());
                }

                workProject.setDevelopEndFact(saveDateDevelopEndFact.getDate());
            }
        }

    }

    public boolean setWorkDate(Work work, long projectId, Timestamp date) {
        WorkProject workProject = getWorkProject(work, projectId);
        boolean save1 = checkSetDevelopStartDate(workProject, date);
        if (save1) {
            workProject.setDevelopStartFact(date);
        }
        SaveDateDevelopEndFact save2 = checkSetDevelopEndDate(workProject, date);
        if (save2.isSave()) {
            workProject.setDevelopEndFact(save2.getDate());
        }
        if (save1 || save2.isSave()) {
            workProjectRepository.save(workProject);
        }

        return save1 || save2.isSave();
    }

    private boolean checkSetDevelopStartDate(WorkProject workProject, Timestamp date) {
        boolean save = false;
        if (date != null) {
            if (workProject.getDevelopStartFact() == null || workProject.getDevelopStartFact().after(date)) {
                save = true;
            }
        }
        return save;
    }

    public SaveDateDevelopEndFact checkSetDevelopEndDate(WorkProject work, Timestamp date) {
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

    private Timestamp getLastDateWorkBefore(Long workId, Timestamp date) throws ResourceNotFoundException {
        return taskServiceIntegration.getLastTime(workId, date, null);

    }

    public void save(WorkProject workProject) {
        workProjectRepository.save(workProject);
    }


    public Page<@NonNull WorkFull> getWorkFull(Integer page, Integer size, String sort, Integer stageZiGe, Integer stageZiLe, String task, Long releaseId, Long projectId, List<Work> workList) {
        Specification<@NonNull WorkProject> specification = Specification.unrestricted();
        specification = Specifications.in(specification, "work", workList);
        specification = Specifications.like(specification, "task", task);
        specification = Specifications.eq(specification, "projectId", projectId);
        if (stageZiLe != null && stageZiLe.equals(stageZiGe)) {
            specification = Specifications.eq(specification, "stageZi", stageZiLe);

        } else {
            specification = Specifications.le(specification, "stageZi", stageZiLe);
            specification = Specifications.ge(specification, "stageZi", stageZiGe);
        }
        if (releaseId != null) {
            Release release = releaseService.findOptionalById(releaseId).orElse(null);
            specification = Specifications.eq(specification, "release", release);
        }

        Page<@NonNull WorkProject> workPage;
        if (sort == null) {
            if (page != null && size != null) {
                workPage = workProjectRepository.findAll(specification, PageRequest.of(page - 1, size));
            } else {
                workPage = new PageImpl<>(workProjectRepository.findAll(specification));
            }

        } else {
            if (page != null && size != null) {
                workPage = workProjectRepository.findAll(specification, PageRequest.of(page - 1, size, Sort.Direction.ASC, sort));
            } else {
                workPage = new PageImpl<>(workProjectRepository.findAll(specification, Sort.by(sort)));
            }
        }
        return workPage.map(workProject -> new WorkFull(workProject.getWork(), workProject));
    }

    public List<WorkProject> getWorkProjectList(Long releaseId, Integer stageZiGe, Integer stageZiLe) {
        Specification<WorkProject> specification = Specification.unrestricted();
        if (stageZiLe != null && stageZiLe.equals(stageZiGe)) {
            specification = Specifications.eq(specification, "stageZi", stageZiLe);

        } else {
            specification = Specifications.le(specification, "stageZi", stageZiLe);
            specification = Specifications.ge(specification, "stageZi", stageZiGe);
        }
        if (releaseId != null) {
            Release release = releaseService.findOptionalById(releaseId).orElse(null);
            specification = Specifications.eq(specification, "release", release);
        }
        return workProjectRepository.findAll(specification);
    }

    public List<Long> getWorkIdList(Long releaseId, Integer stageZiGe, Integer stageZiLe) {
        return getWorkProjectList(releaseId, stageZiGe, stageZiLe).stream().map(workProject -> workProject.getWork().getId()).toList();
    }
}
