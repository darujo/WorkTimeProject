package ru.darujo.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.darujo.exceptions.ResourceNotFoundException;
import ru.darujo.integration.TaskServiceIntegration;
import ru.darujo.model.SaveDateDevelopEndFact;
import ru.darujo.model.Work;
import ru.darujo.model.WorkProject;
import ru.darujo.repository.WorkProjectRepository;

import java.sql.Timestamp;

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

}
