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
        return projectRepository.save(project);
    }

    public void deleteProject(long id) {
        projectRepository.deleteById(id);
    }
}
