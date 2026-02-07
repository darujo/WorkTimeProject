package ru.darujo.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.darujo.model.WorkLittle;
import ru.darujo.model.WorkProjectLittle;
import ru.darujo.repository.WorkProjectLittleRepository;

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

    public WorkProjectLittle getWorkProject(WorkLittle work, @NonNull Long projectId) {
        return workProjectLittleRepository.findByWorkLittleAndProjectId(work, projectId);
    }

    public WorkProjectLittle save(WorkProjectLittle workProject) {
        return workProjectLittleRepository.save(workProject);
    }

}
