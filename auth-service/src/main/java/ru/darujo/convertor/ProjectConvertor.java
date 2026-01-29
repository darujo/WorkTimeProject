package ru.darujo.convertor;

import ru.darujo.dto.project.ProjectDto;
import ru.darujo.model.Project;


public class ProjectConvertor {

    public static ProjectDto getProjectDto(Project project) {
        return new ProjectDto(project.getId(), project.getCode(), project.getName());
    }

    public static Project getProject(ProjectDto projectDto) {
        return new Project(projectDto.getId(), projectDto.getCode(), projectDto.getName());
    }
}
