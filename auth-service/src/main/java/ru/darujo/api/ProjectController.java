package ru.darujo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.darujo.convertor.ProjectConvertor;
import ru.darujo.dto.project.ProjectDto;
import ru.darujo.service.ProjectService;


@RestController
@CrossOrigin
@RequestMapping("/project")
public class ProjectController {
    private ProjectService projectService;

    @Autowired
    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/edit/{id}")
    public ProjectDto getProjectEditDto(@PathVariable long id) {
        return ProjectConvertor.getProjectDto(projectService.findById(id));
    }

    @PostMapping("/save")
    public ProjectDto setProjectDto(@RequestBody ProjectDto projectDto) {
        return ProjectConvertor.getProjectDto(
                projectService.saveProject(ProjectConvertor.getProject(projectDto)));
    }

    @DeleteMapping("/delete/{id}")
    public void delRoleEditDto(@PathVariable long id) {
        projectService.deleteProject(id);
    }


    @GetMapping("")
    public Page<ProjectDto> getProjects(@RequestParam(required = false) String code,
                                        @RequestParam(required = false) String name) {
        return projectService.findAll(code, name).map(ProjectConvertor::getProjectDto);

    }
}
