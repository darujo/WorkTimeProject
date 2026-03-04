package ru.darujo.service;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.model.Project;
import ru.darujo.repository.ProjectRepository;
import ru.darujo.specifications.Specifications;

@Service
public class ProjectService {
    @Getter
    private static ProjectService Instance;

    @PostConstruct
    public void setInstance() {
        Instance = this;
    }

    private ProjectRepository projectRepository;

    @Autowired
    public void setProjectRepository(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project findById(Long id) {
        return projectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundRunTime("Не найден проект по id " + id));
    }

    public Page<Project> findAll(String code, String name) {
        Specification<Project> specification = Specification.unrestricted();
        specification = Specifications.eqIgnoreCase(specification, "code", code);
        specification = Specifications.eqIgnoreCase(specification, "name", name);

        return new PageImpl<>(projectRepository.findAll(specification));
    }

    public Project saveProject(Project project) {
        if (!project.getStage4Name().isBlank()) {
            project.setStageEnd(5);
        } else if (!project.getStage3Name().isBlank()) {
            project.setStageEnd(4);
        } else if (!project.getStage2Name().isBlank()) {
            project.setStageEnd(3);
        } else if (!project.getStage1Name().isBlank()) {
            project.setStageEnd(2);
        } else if (!project.getStage0Name().isBlank()) {
            project.setStageEnd(1);
        }

        if (project.getStageEnd() == null) {
            project.setStageEnd(5);
        } else if (project.getStageEnd() < 1) {
            project.setStageEnd(1);
        } else if (project.getStageEnd() > 5) {
            project.setStageEnd(5);
        }

        if (project.getStageEnd() > 0 && project.getStage0Name().isBlank()) {
            project.setStage0Name("Анализ");
        }
        if (project.getStageEnd() > 1 && project.getStage1Name().isBlank()) {
            project.setStage1Name("Разработка прототипа");
        }
        if (project.getStageEnd() > 2 && project.getStage2Name().isBlank()) {
            project.setStage2Name("Стабилизация прототипа");
        }
        if (project.getStageEnd() > 3 && project.getStage3Name().isBlank()) {
            project.setStage3Name("Стабилизация релиза");
        }
        if (project.getStageEnd() > 4 && project.getStage4Name().isBlank()) {
            project.setStage4Name("ОПЭ");
        }

        return projectRepository.save(project);
    }

    public void deleteProject(long id) {
        try {
            projectRepository.deleteById(id);
        } catch (RuntimeException ex) {
            throw new ResourceNotFoundRunTime("Не удалось удалить проект скорее всего есть зависимые объекты: " + ex.getMessage());
        }

    }
}
