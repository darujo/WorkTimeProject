package ru.darujo.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.darujo.service.UpdateService;

import java.time.ZonedDateTime;
import java.util.List;

@RestController()
@RequestMapping("/v1/update")
@Slf4j
public class UpdateController {
    private UpdateService updateService;

    @Autowired
    public void setUpdateService(UpdateService updateService) {
        this.updateService = updateService;
    }

    @PostMapping("")
    public String updateFile(@RequestHeader String username,
                             @RequestParam("dateUpdate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateZoneUpdate,
                             @RequestParam(name = "type", required = false) List<String> types,
                             @RequestPart(required = false, name = "description") String description,
                             @RequestPart("file") List<MultipartFile> multipartFiles) {
        return updateService.loadUpdate(username, dateZoneUpdate, types, description, multipartFiles) ? "Success!" : "Failed!";

    }

    @GetMapping("/stop")
    public String updateFile() {

        return updateService.stopAll() ? "Success!" : "Failed!";
    }


}