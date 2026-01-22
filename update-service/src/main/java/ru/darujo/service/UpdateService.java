package ru.darujo.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.darujo.assistant.helper.DateHelper;
import ru.darujo.dto.information.MessageInfoDto;
import ru.darujo.dto.information.MessageType;
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.integration.InfoServiceIntegration;
import ru.darujo.model.ServiceType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class UpdateService {
    private ScheduleService scheduleService;
    private MonitorService monitorService;
    private InfoServiceIntegration infoServiceIntegration;
    @Getter
    private static UpdateService INSTANCE;

    public UpdateService() {
        INSTANCE = this;
    }

    @Autowired
    public void setInfoServiceIntegration(InfoServiceIntegration infoServiceIntegration) {
        this.infoServiceIntegration = infoServiceIntegration;
    }

    @Autowired
    public void setMonitorService(MonitorService monitorService) {
        this.monitorService = monitorService;
    }

    @Autowired
    public void setScheduleService(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @Value("${update.save-into}")
    private String pathSave;

    public List<File> loadUpdate(List<MultipartFile> multipartFiles) {
        List<File> files = new ArrayList<>();

        AtomicInteger num = new AtomicInteger();
        if (multipartFiles != null) {
            multipartFiles.forEach(multipartFile -> {
                if (multipartFile.getOriginalFilename() == null) {
                    num.getAndIncrement();
                }
                String fileName = pathSave + "/" + (multipartFile.getOriginalFilename() == null ? "update" + num : multipartFile.getOriginalFilename());
                try (FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
                    fileOutputStream.write(multipartFile.getBytes());
                    File file = new File(fileName);
                    files.add(file);
                        } catch (IOException e) {
                            log.error(Arrays.toString(e.getStackTrace()));

                        }
                    }
            );
        }
        return files;
    }


    public boolean loadUpdate(String username, ZonedDateTime timestamp, List<String> types, String description, List<MultipartFile> multipartFiles) {
        log.info("Пользователь {} загрузил обновление с описанием {}", username, description);
        if (multipartFiles != null && !multipartFiles.isEmpty() && (description == null || description.isBlank())) {
            throw new ResourceNotFoundRunTime("Должно быть заполнено описание");
        }
        List<File> files = loadUpdate(multipartFiles);
        boolean flag = multipartFiles == null || multipartFiles.size() == files.size();
        if (!flag) {
            files.forEach(File::deleteOnExit);
            throw new ResourceNotFoundRunTime("Все файлы удалось сохранить.");
        }
        scheduleService.addUpdate(
                timestamp,
                types == null || types.isEmpty() || types.contains(null) ? null : types.stream().map(ServiceType::valueOf).toList(),
                files,
                description);
        try {
            if (ChronoUnit.MINUTES.between(ZonedDateTime.now(), timestamp) > 0) {
                infoServiceIntegration.addMessage(new MessageInfoDto(MessageType.SYSTEM_INFO, String.format("%s будут проводиться сервисные работы. Сервис может быть недоступен. Приносим извинения за предоставленые неудобства.", DateHelper.dateTimeToStr(timestamp))));
            }
        } catch (RuntimeException ex) {
            log.error(ex.getMessage());
        }
        return true;
    }

    public boolean stopAll() {
        monitorService.stopServiceAll();
        return true;
    }

}
