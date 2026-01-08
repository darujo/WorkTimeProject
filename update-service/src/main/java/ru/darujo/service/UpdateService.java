package ru.darujo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@Slf4j
public class UpdateService {
    private ScheduleService scheduleService;

    @Autowired
    public void setScheduleService(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @Value("${update.save-into}")
    private String pathSave;

    public boolean loadUpdate(List<MultipartFile> multipartFiles) {
        AtomicBoolean flag = new AtomicBoolean(true);
        multipartFiles.forEach(multipartFile -> {
                    try (FileOutputStream fileOutputStream = new FileOutputStream(pathSave + "/" + (multipartFile.getOriginalFilename() == null ? "update" : multipartFile.getOriginalFilename()))) {
                        fileOutputStream.write(multipartFile.getBytes());

                    } catch (IOException e) {
                        log.error(Arrays.toString(e.getStackTrace()));
                        flag.set(false);
                    }
                }
        );
        return flag.get();
    }

    public boolean loadUpdate(String username, String description, List<MultipartFile> multipartFiles) {
        log.info("Пользователь {} загрузил обновление с описанием {}", username, description);
        boolean flag = loadUpdate(multipartFiles);
        scheduleService.addUpdate(multipartFiles.stream().map(MultipartFile::getOriginalFilename).toList());
        return flag;
    }


}
