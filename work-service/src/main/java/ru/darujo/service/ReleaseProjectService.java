package ru.darujo.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.darujo.model.Release;
import ru.darujo.model.ReleaseProject;
import ru.darujo.repository.ReleaseProjectRepository;

@Service
public class ReleaseProjectService {

    @Getter
    private static ReleaseProjectService INSTANCE;

    public ReleaseProjectService() {
        INSTANCE = this;

    }

    private ReleaseProjectRepository releaseProjectRepository;

    @Autowired
    public void setReleaseProjectRepository(ReleaseProjectRepository releaseProjectRepository) {
        this.releaseProjectRepository = releaseProjectRepository;
    }

    public ReleaseProject save(ReleaseProject releaseProject) {
        ReleaseProject save = findReleaseProject(releaseProject.getRelease(), releaseProject.getProjectId());
        if (save != null) {
            releaseProject.setId(save.getId());
        }
        return releaseProjectRepository.save(releaseProject);
    }

    public ReleaseProject findReleaseProject(Release release, Long projectId) {
        return releaseProjectRepository.findByReleaseAndProjectId(release, projectId);
    }
}
