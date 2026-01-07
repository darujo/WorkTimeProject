package ru.darujo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.darujo.convertor.ReleaseConvertor;
import ru.darujo.dto.work.ReleaseDto;
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.service.ReleaseService;

import java.util.List;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("/v1/release")
public class ReleaseController {
    private ReleaseService releaseService;

    @Autowired
    public void setReleaseService(ReleaseService releaseService) {
        this.releaseService = releaseService;
    }

    @GetMapping("/{id}")
    public ReleaseDto releaseEdit(@PathVariable long id) {
        return ReleaseConvertor.getReleaseDto(releaseService.findById(id));
    }

    @PostMapping("")
    public ReleaseDto releaseSave(@RequestBody ReleaseDto releaseDto,
                               @RequestParam("system_right") List<String> system_right,
//                               @RequestHeader(defaultValue = "false", name = "ZI_EDIT") boolean right,
                               @RequestParam(name = "system_project") Long projectId) {
        if (system_right.contains("ZI_EDIT")) {
            throw new ResourceNotFoundRunTime("У вас нет права ZI_EDIT");
        }
        return ReleaseConvertor.getReleaseDto(releaseService.saveRelease(ReleaseConvertor.getRelease(projectId, releaseDto)));
    }

    @DeleteMapping("/{id}")
    public void deleteRelease(@PathVariable long id
    ) {
        releaseService.deleteRelease(id);
    }

    @GetMapping("")
    public List<ReleaseDto> releasePage(
            @RequestParam(name = "system_project", required = false) Long projectId
    ) {
        return releaseService.findAll(null, projectId).stream().map(ReleaseConvertor::getReleaseDto).collect(Collectors.toList());
    }
}
