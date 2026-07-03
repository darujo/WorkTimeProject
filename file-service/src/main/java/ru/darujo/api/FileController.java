package ru.darujo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;
import ru.darujo.service.FileService;

import java.util.List;

@RestController
@RequestMapping("/v1/file")
public class FileController {
    private FileService fileService;


    @Autowired
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("")
    public List<Long> saveFile(@RequestParam String objectType,
                               @RequestParam String objectId,
                               @RequestPart("file") List<MultipartFile> files,
                               @RequestHeader(required = false) String username) {

        return fileService.saveFiles(username, objectType, objectId, files);

    }

    @GetMapping("/document")
    public DeferredResult<ResponseEntity<Resource>> asyncDownload(@RequestParam(required = false) List<Long> fileId) {
        DeferredResult<ResponseEntity<Resource>> deferredResult = new DeferredResult<>(30000L); // 30-секундный таймаут
        fileService.getFile(fileId, deferredResult);
        return deferredResult;
    }


}
