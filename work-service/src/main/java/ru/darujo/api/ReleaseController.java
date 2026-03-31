package ru.darujo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.darujo.convertor.ReleaseConvertor;
import ru.darujo.dto.work.ReleaseEditDto;
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.model.Release;
import ru.darujo.model.ReleaseFull;
import ru.darujo.model.ReleaseProject;
import ru.darujo.service.ReleaseProjectService;
import ru.darujo.service.ReleaseService;

import java.util.List;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("/v1/release")
public class ReleaseController {
    private ReleaseService releaseService;
    private ReleaseProjectService releaseProjectService;

    @Autowired
    public void setReleaseService(ReleaseService releaseService) {
        this.releaseService = releaseService;
    }

    @GetMapping("/{id}")
    public ReleaseEditDto releaseEdit(@PathVariable long id,
                                      @RequestParam(name = "system_project") Long projectId
    ) {
        return ReleaseConvertor.getReleaseDto(releaseService.getReleaseFull(id, projectId));
    }

    @PostMapping("")
    public ReleaseEditDto releaseSave(@RequestBody ReleaseEditDto releaseEditDto,
                                      @RequestParam(name = "system_right", required = false) List<String> system_right,
//                               @RequestHeader(defaultValue = "false", name = "ZI_EDIT") boolean right,
                                      @RequestParam(name = "system_project") Long projectId) {
        if (system_right == null || !system_right.contains("ZI_EDIT")) {
            throw new ResourceNotFoundRunTime("У вас нет права ZI_EDIT");
        }
        ReleaseProject releaseProject = ReleaseConvertor.getRelease(projectId, releaseEditDto);
        Release release = releaseService.saveRelease(releaseProject.getRelease());
        releaseProject = releaseProjectService.save(releaseProject);
        ReleaseFull releaseFull = new ReleaseFull(release, releaseProject);
        return ReleaseConvertor.getReleaseDto(releaseFull);
    }

    @DeleteMapping("/{id}")
    public void deleteRelease(@PathVariable long id
    ) {
        releaseService.deleteRelease(id);
    }

    @GetMapping("")
    public List<ReleaseEditDto> releasePage(
            @RequestParam(name = "system_project", required = false) Long projectId
    ) {
        return releaseService.findAll(null, projectId).stream().map(ReleaseConvertor::getReleaseDto).collect(Collectors.toList());
    }

    @Autowired
    public void setReleaseProjectService(ReleaseProjectService releaseProjectService) {
        this.releaseProjectService = releaseProjectService;
    }
}
