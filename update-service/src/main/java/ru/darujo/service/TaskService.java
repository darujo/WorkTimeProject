package ru.darujo.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.darujo.dto.information.MessageInfoDto;
import ru.darujo.dto.information.MessageType;
import ru.darujo.integration.InfoServiceIntegration;
import ru.darujo.integration.ServiceIntegration;
import ru.darujo.model.RunnableNotException;
import ru.darujo.model.ServiceType;
import ru.darujo.object.ServiceIntegrationObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Component
public class TaskService {
    private MonitorService monitorService;
    private InfoServiceIntegration infoServiceIntegration;

    @Autowired
    public void setInfoServiceIntegration(InfoServiceIntegration infoServiceIntegration) {
        this.infoServiceIntegration = infoServiceIntegration;
    }

    @Autowired
    public void setMonitorService(MonitorService monitorService) {
        this.monitorService = monitorService;
    }

    @Value("${update.run-service-command.command}")
    private String runFile;
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

    }

    public RunnableNotException getTaskAvailService() {
        return new RunnableNotException(() ->
                monitorService.availService()
        );
    }

    public RunnableNotException getTask(List<String> fileNameUpdates, List<ServiceType> serviceTypeList, String textUpdates) {
        return new RunnableNotException(() ->
        {
            try {
                infoServiceIntegration.addMessage(new MessageInfoDto(MessageType.SYSTEM_INFO, "Запущено обновление. Сервис может быть недоступен. Приносим извинения за предоставленые неудобства"));
            } catch (RuntimeException ex) {
                log.error(ex.getMessage());
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
                    fileNameUpdates.stream().filter(s -> {
                                int pos = s.indexOf(".");
                                if (pos > 0) {
                                    return s.substring(pos).equals(".7z") || s.substring(pos).equals(".7z.001");
                                }
                                return false;
                            }
                    ).forEach(s ->
                            open(unpack, String.format(unpackParam, s, pathSave))
                    );
                }
                for (ServiceIntegrationObject serviceIntegration : monitorService.getServiceIntegrations()) {
                    if (serviceTypeList == null || serviceTypeList.contains(serviceIntegration.getServiceType())) {
                        startOneService(serviceIntegration);
                    }
                }
                boolean flagOk = false;
                while (!flagOk) {
                    flagOk = monitorService.allServiceOk();
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
            return new ProcessBuilder(param)
                    .directory(new File(pathFile))
                    .start();
        } catch (IOException e) {
            log.error("Не удалось выполнить команду {} c параметрами {}", runFile, param);
            return null;
        }

    }

    private void shutDownOneService(ServiceIntegration serviceIntegration, String token) {
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

    public RunnableNotException getTaskInfo(ZonedDateTime zonedDateTime, String text) {
        return new RunnableNotException(() -> {
            try {
                long minutes = ChronoUnit.MINUTES.between(ZonedDateTime.now(), zonedDateTime);
                if (minutes > 0) {
                    infoServiceIntegration.addMessage(new MessageInfoDto(MessageType.SYSTEM_INFO, String.format("Через %s будет %s", minutes, text)));
                }
            } catch (RuntimeException ignore) {
            }
        });
    }
}
