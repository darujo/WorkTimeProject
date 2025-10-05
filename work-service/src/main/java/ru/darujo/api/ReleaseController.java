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
    public ReleaseDto WorkEdit(@PathVariable long id) {
        return ReleaseConvertor.getReleaseDto(releaseService.findById(id));
    }

    @PostMapping("")
    public ReleaseDto WorkSave(@RequestBody ReleaseDto releaseDto,
                            @RequestHeader(defaultValue = "false", name = "ZI_EDIT") boolean right) {
        if (!right) {
            throw new ResourceNotFoundRunTime("У вас нет права ZI_EDIT");
        }
        return ReleaseConvertor.getReleaseDto(releaseService.saveRelease(ReleaseConvertor.getRelease(releaseDto)));
    }

    @DeleteMapping("/{id}")
    public void deleteRelease(@PathVariable long id) {
        releaseService.deleteRelease(id);
    }

    @GetMapping("")
    public List<ReleaseDto> WorkPage() {
        return releaseService.findAll().stream().map(ReleaseConvertor::getReleaseDto).collect(Collectors.toList());
    }
}
