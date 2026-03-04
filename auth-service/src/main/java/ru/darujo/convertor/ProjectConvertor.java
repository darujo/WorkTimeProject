package ru.darujo.convertor;

import ru.darujo.dto.project.ProjectDto;
import ru.darujo.model.Project;


public class ProjectConvertor {

    public static ProjectDto getProjectDto(Project project) {
        return new ProjectDto(
                project.getId(),
                project.getCode(),
                project.getName(),
                project.getStageEnd(),
                project.getStage0Name(),
                project.getStage1Name(),
                project.getStage2Name(),
                project.getStage3Name(),
                project.getStage4Name());
    }

    public static Project getProject(ProjectDto projectDto) {
        return new Project(
                projectDto.getId(),
                projectDto.getCode(),
                projectDto.getName(),
                projectDto.getStageEnd(),
                projectDto.getStage0Name(),
                projectDto.getStage1Name(),
                projectDto.getStage2Name(),
                projectDto.getStage3Name(),
                projectDto.getStage4Name());
    }
}
