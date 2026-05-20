package ru.darujo.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.darujo.dto.information.MessageInfoDto;
import ru.darujo.dto.information.SendAdminMessage;
import ru.darujo.integration.AdminInfoService;
import ru.darujo.integration.InfoServiceIntegrationImp;
import ru.darujo.integration.ServiceIntegration;
import ru.darujo.integration.ServiceType;
import ru.darujo.model.RunnableNotException;
import ru.darujo.object.ServiceIntegrationObject;
import ru.darujo.type.MessageType;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TaskService {
    private MonitorService monitorService;
    private InfoServiceIntegrationImp infoServiceIntegration;
    private List<AdminInfoService> adminInfoServiceList;

    @Autowired
    public void setAdminInfoServiceList(List<AdminInfoService> adminInfoServiceList) {
        this.adminInfoServiceList = adminInfoServiceList;
    }
    @Autowired
    public void setInfoServiceIntegration(InfoServiceIntegrationImp infoServiceIntegration) {
        this.infoServiceIntegration = infoServiceIntegration;
    }

    @Autowired
    public void setMonitorService(MonitorService monitorService) {
        this.monitorService = monitorService;
    }

    @Value("${update.run-service-command.command}")
    private String runFile;

    //    @Value("#{'${update.backup.command}'.split(',')}")
    @Value("${update.backup.command}")
    private List<String> backUp;
    @Value("${spring.datasource.password}")
    private String backUpPass;
    @Value("${update.save-into}")
    private String pathFile;
    @Value("${update.unpack-command.command}")
    private String unpack;
    @Value("${update.unpack-command.param}")
    private String unpackParam;
    @Value("${update.save-into}")
    private String pathSave;

    @PostConstruct
    public void init() {
        log.warn(getProcessOutput(backUpService(pathFile + "/backup", String.format("worktime_%s.backup", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm"))))));
    }

    public RunnableNotException getTaskAvailService() {
        return new RunnableNotException(() ->
                monitorService.availService()
        );
    }

    public RunnableNotException getTask(List<File> fileNameUpdates, List<ServiceType> serviceTypeList, String textUpdates) {
        return new RunnableNotException(() ->
        {
            try {
                infoServiceIntegration.addMessage(new MessageInfoDto(MessageType.SYSTEM_INFO, "Запущено обновление. Сервис может быть недоступен. Приносим извинения за предоставленные неудобства"));
            } catch (RuntimeException ex) {
                log.error(ex.getMessage(), ex);
            }
            try {
                String token = monitorService.getToken();
                for (ServiceIntegrationObject serviceIntegration : monitorService.getServiceIntegrations()) {
                    if (serviceTypeList == null || serviceTypeList.contains(serviceIntegration.getServiceType())) {
                        try {
                            shutDownOneService(serviceIntegration.getServiceIntegration(), token);
                            Thread.sleep(20000);
                        } catch (RuntimeException ignored) {

                        }
                    }
                }
                Thread.sleep(80000);
                if (fileNameUpdates != null) {
                    fileNameUpdates.stream().filter(file -> {
                        String fileName = file.getAbsolutePath();
                        int pos = fileName.indexOf(".");
                                if (pos > 0) {
                                    return fileName.substring(pos).equals(".7z") || fileName.substring(pos).equals(".7z.001");
                                }
                                return false;
                            }
                    ).forEach(file ->
                            open(unpack, String.format(unpackParam, file, pathSave))
                    );
                    // ToDo Подумать как удалять только архивы
                    fileNameUpdates.forEach(file -> {
                        if (!file.delete()) {
                            file.deleteOnExit();
                        }
                    });
                }
                for (ServiceIntegrationObject serviceIntegration : monitorService.getServiceIntegrations()) {
                    if (serviceTypeList == null || serviceTypeList.contains(serviceIntegration.getServiceType())) {
                        startOneService(serviceIntegration);
                    }
                }
                boolean flagOk = false;
                while (!flagOk) {
                    flagOk = monitorService.allServiceOk(serviceTypeList);
                }
                try {
                    if (textUpdates != null) {
                        infoServiceIntegration.addMessage(new MessageInfoDto(MessageType.UPDATE_INFO, textUpdates));
                    }
                } catch (RuntimeException ignore) {
                }
                try {
                    infoServiceIntegration.addMessage(new MessageInfoDto(MessageType.SYSTEM_INFO, "Сервис снова доступен."));
                } catch (RuntimeException ignore) {
                }

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
        );
    }

    private void startOneService(ServiceIntegrationObject serviceIntegration) throws InterruptedException {
        boolean flagStart = false;
        serviceIntegration.setProcess(startService(serviceIntegration.getServiceType()));
        int iterate = 0;
        while (iterate < 20 && !flagStart) {
            iterate++;
            Thread.sleep(5000);
            try {
                serviceIntegration.getServiceIntegration().test();
                flagStart = true;
            } catch (RuntimeException ex) {
                log.info("Итерация {} сервис {} не доступен", iterate, serviceIntegration.getServiceType());
            }
        }
    }

    private Process startService(ServiceType serviceType) {
        String param = String.format(runFile, serviceType.getName());
        try {
            return new ProcessBuilder(param).directory(new File(pathFile)).start();
        } catch (IOException e) {
            log.error("Не удалось выполнить команду {} c параметрами {} {}", runFile, param, e.getMessage(), e);
            return null;
        }

    }

    private Process backUpService(String dir, String fileName) {
        List<String> arr = new ArrayList<>(backUp);
        arr.add(fileName);
        log.error(arr.toString());
        ProcessBuilder processBuilder = new ProcessBuilder(arr).directory(new File(dir));
        processBuilder.environment().put("PGPASSWORD", backUpPass);

        try {
            return processBuilder.start();
        } catch (IOException e) {
            log.error("Не удалось выполнить команду {} c параметрами {} {}", runFile, backUp, e.getMessage(), e);
            return null;
        }

    }

    private void shutDownOneService(ServiceIntegration<ServiceType> serviceIntegration, String token) {
        serviceIntegration.shutDown(token);
    }

    private static String getProcessOutput(Process process) {

        try (InputStream stream = process.getInputStream();
             BufferedInputStream inputStream = new BufferedInputStream(stream)
        ) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void open(String command, String param) {

        try {
            log.info(
                    getProcessOutput(
                            Runtime.getRuntime().exec(command + " " + param)
                    )
            );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public RunnableNotException getTaskInfo(LocalDateTime zonedDateTime, String text) {
        return new RunnableNotException(() -> {
            try {
                long minutes = ChronoUnit.MINUTES.between(LocalDateTime.now(), zonedDateTime);
                if (minutes > 0) {
                    infoServiceIntegration.addMessage(new MessageInfoDto(MessageType.SYSTEM_INFO, String.format("Через %s будет %s", minutes, text)));
                }
            } catch (RuntimeException ignore) {
            }
        });
    }

    public RunnableNotException getBackUpTask() {
        return new RunnableNotException(() -> {
            try {
                String fileName = String.format("worktime_%s.backup", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm")));
                log.info(getProcessOutput(backUpService(pathFile + "/backup", fileName)));
                adminInfoServiceList.forEach(adminInfoService -> adminInfoService.sendMessageForAdmin(new SendAdminMessage() {
                    @Override
                    public String getTitle() {
                        return "BackUp " + LocalDateTime.now();
                    }

                    @Override
                    public String getText() {
                        return "BackUp выполнен;";
                    }

                    @Override
                    public String getFileName() {
                        return fileName;
                    }

                    @Override
                    public byte[] getFileBody() {
                        Path file = Path.of(pathFile + "/backup" + "/" + fileName);
                        try {
                            return Files.readAllBytes(file);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public boolean isAttachFile() {
                        return true;
                    }
                }));
            } catch (RuntimeException ignore) {
            }
        });
    }
}
