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
import ru.darujo.integration.InfoServiceIntegration;
import ru.darujo.model.ServiceType;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

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

    private String textUpdates;

    public boolean loadUpdate(String username, ZonedDateTime timestamp, List<String> types, String description, List<MultipartFile> multipartFiles) {
        log.info("Пользователь {} загрузил обновление с описанием {}", username, description);
        try {
            infoServiceIntegration.addMessage(new MessageInfoDto(MessageType.SYSTEM_INFO, String.format("%s будут проводиться сервисные работы. Сервис может быть. Приносим извинения за предоставленые неудобства", DateHelper.dateTimeToStr(timestamp))));
        } catch (RuntimeException ex) {
            log.error(ex.getMessage());
        }
        boolean flag = loadUpdate(multipartFiles);
        scheduleService.addUpdate(
                timestamp,
                types == null || types.isEmpty() || types.contains(null) ? null : types.stream().map(ServiceType::valueOf).toList(),
                multipartFiles.stream().map(multipartFile -> pathSave + "/" + multipartFile.getOriginalFilename()).toList());
        textUpdates = (textUpdates == null || textUpdates.isEmpty() ? "" : (textUpdates + "\n")) + description;
        return flag;
    }

    public boolean stopAll() {
        monitorService.stopServiceAll();
        return true;
    }

    public void allServiceOk() {
        if (textUpdates != null) {
            try {
                infoServiceIntegration.addMessage(new MessageInfoDto(MessageType.UPDATE_INFO, textUpdates));
            } catch (RuntimeException ignore) {
            }
            textUpdates = null;
            try {
                infoServiceIntegration.addMessage(new MessageInfoDto(MessageType.SYSTEM_INFO, "Сервис снова доступен."));
            } catch (RuntimeException ignore) {
            }
        }
    }
}
