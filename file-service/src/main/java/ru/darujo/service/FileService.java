package ru.darujo.service;

import jakarta.activation.MimetypesFileTypeMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.model.FileModel;
import ru.darujo.repository.FileModelRepository;
import ru.darujo.specifications.Specifications;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class FileService {

    @Value("${file.save-into}")
    private String pathSaveReport;
    private FileModelRepository fileModelRepository;

    public List<Long> saveFiles(String username, String objectType, String objectId, List<MultipartFile> multipartFiles) {
        String dir = pathSaveReport + File.separator + objectType + File.separator + objectId;
        File directory = new File(dir);
        boolean created = directory.mkdirs();
        if (created) {
            log.info("Директория успешно создана {}", dir);
        } else {
            log.info("Не удалось создать директорию{}", dir);
        }
        List<Long> fileIdList = new ArrayList<>();
        if (multipartFiles != null) {
            multipartFiles.forEach(multipartFile -> {
                FileModel fileModel = fileModelRepository.save(new FileModel(null, objectType, objectId, "", username, multipartFile.getOriginalFilename(), multipartFile.getSize(), false, LocalDateTime.now()));
                        fileIdList.add(fileModel.getId());
                        fileModel.setFileForDisk(dir + File.separator + fileModel.getId() + ".7z");
                        fileModelRepository.save(fileModel);
                        String fileName = (multipartFile.getOriginalFilename() == null ? Long.toString(fileModel.getId()) : multipartFile.getOriginalFilename());
                        try (SevenZOutputFile out = ArchiveService.createArchive(new File(fileModel.getFileForDisk()))) {
                            ArchiveService.addFileArchive(out, Path.of(fileName), multipartFile.getBytes());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
            );
        }
        return fileIdList;
    }

    public ResponseEntity<Resource> getFiles(List<Long> fileIdList) {
        fileIdList.forEach(fileId -> {
            File fileAdd = getFile(fileId);
            try {
                ArchiveService.unpackArchive(
                        fileAdd,
                        this::getResult);
            } catch (NoSuchFileException ex) {
                log.error(ex.getMessage(), ex);
                deleteLogicalFile(fileId);

            }
        });

        ResultFile res = resultFile.get();
        MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
        String contentType = fileTypeMap.getContentType(res.getFile());
        log.info(contentType);

        try {
            InputStreamResource resource = res.getISR();

            ContentDisposition contentDisposition = ContentDisposition.attachment().filename(res.getFile().getName(), StandardCharsets.UTF_8).build();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(contentDisposition);
            headers.setContentLength(res.getLength());
            headers.setContentType(MediaType.parseMediaType(contentType));
            return ResponseEntity.ok()
//                    .contentType(MediaType.parseMediaType(contentType))

//                    .contentLength(res.getLength())
//                    .header(HttpHeaders.CONTENT_DISPOSITION,
//                            "attachment; filename=\"" + res.getFile().getName() + "\"")
                    .headers(headers)
                    .body(resource);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void getFiles(List<Long> fileIdList, DeferredResult<ResponseEntity<Resource>> deferredResult) {
        deferredResult.setResult(getFiles(fileIdList));
    }

    private void deleteLogicalFile(Long fileId) {
        fileModelRepository.findById(fileId).ifPresent(this::deleteLogicalFile);
    }

    private void deleteLogicalFile(FileModel fileModel) {
        setDeleteFile(fileModel, true);
    }

    private void setDeleteFile(FileModel fileModel, boolean isDelete) {
        fileModel.setDelete(isDelete);
        fileModelRepository.save(fileModel);
    }

    ThreadLocal<ResultFile> resultFile = ThreadLocal.withInitial(ResultFile::new);

    private void getResult(File fileUnpack, byte[] content) {
        resultFile.get().setFile(fileUnpack, content);
    }

    private File getFile(Long fileId) {
        FileModel fileModel = fileModelRepository.findById(fileId).orElseThrow(() -> new ResourceNotFoundRunTime("Не найден файл."));
        return new File(fileModel.getFileForDisk());
    }

    public void delete(String username, List<Long> fileId) {
        if (getDocumentList(null, null, fileId).stream().allMatch(fileModel -> fileModel.getUserName().equals(username))) {
            fileModelRepository.deleteAllById(fileId);
        } else {
            throw new ResourceNotFoundRunTime("Часть файлов принадлежит не вам. В удаление отказано.");
        }
    }

    public List<FileModel> getDocumentList(String objectType, String objectId, List<Long> fileId) {
        Specification<FileModel> sp = Specification.unrestricted();
        sp = Specifications.in(sp, "id", fileId);
        sp = Specifications.eq(sp, "objectType", objectType);
        sp = Specifications.eq(sp, "objectId", objectId);
        List<FileModel> fileModelList = fileModelRepository.findAll(sp);
        fileModelList.parallelStream().forEach(fileModel -> {
            if ((fileModel.getDelete() == null || !fileModel.getDelete()) && !Files.exists(Path.of(fileModel.getFileForDisk()))) {
                deleteLogicalFile(fileModel);
            } else if (fileModel.getDelete() != null && fileModel.getDelete() && Files.exists(Path.of(fileModel.getFileForDisk()))) {
                setDeleteFile(fileModel, false);
            }
        });
        return fileModelList;
    }

    @Autowired
    public void setFileModelRepository(FileModelRepository fileModelRepository) {
        this.fileModelRepository = fileModelRepository;
    }
}
