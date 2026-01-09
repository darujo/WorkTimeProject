package ru.darujo.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.darujo.integration.ServiceIntegration;
import ru.darujo.model.RunnableNotException;
import ru.darujo.model.ServiceType;
import ru.darujo.object.ServiceIntegrationObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Component
public class TaskService {
    private MonitorService monitorService;

    @Autowired
    public void setMonitorService(MonitorService monitorService) {
        this.monitorService = monitorService;
    }

    @Value("${update.run-service-command.command}")
    private String runFile;
    @Value("${update.run-service-command.param}")
    private String runFileParam;
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
        try {
            new ProcessBuilder(runFile, "-jar", String.format(runFileParam, ServiceType.TASK.getName())).directory(new File(pathFile)).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public RunnableNotException getTaskAvailService() {
        return new RunnableNotException(() ->
                monitorService.availService()
        );
    }

    public RunnableNotException getTask(List<String> fileNameUpdates, List<ServiceType> serviceTypeList) {
        return new RunnableNotException(() ->
        {
            try {
                String token = monitorService.getToken();
                for (ServiceIntegrationObject serviceIntegration : monitorService.getServiceIntegrations()) {
                    if (serviceTypeList == null || serviceTypeList.contains(serviceIntegration.getServiceType())) {
                        try {
                            updateOneService(serviceIntegration.getServiceIntegration(), token);
                            // todo надо чтобы сервис уведомлений закрывался последним
                            Thread.sleep(20000);
                        } catch (RuntimeException ignored) {

                        }
                    }
                }
                Thread.sleep(80000);
                fileNameUpdates.forEach(s ->
                        open(unpack, String.format(unpackParam, s, pathSave))
                );
                for (ServiceIntegrationObject serviceIntegration : monitorService.getServiceIntegrations()) {
                    if (serviceTypeList == null || serviceTypeList.contains(serviceIntegration.getServiceType())) {
                        startService(serviceIntegration.getServiceType());
                        int iterate = 0;
                        boolean flagStart = false;
                        while (iterate < 5 && !flagStart) {
                            iterate++;
                            Thread.sleep(10000);
                            try {
                                serviceIntegration.getServiceIntegration().test();
                                flagStart = true;
                            } catch (RuntimeException ex) {
                                log.info("Итерация {} сервис {} не доступен", iterate, serviceIntegration.getServiceType());
                            }
                        }
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
        );
    }

    private void startService(ServiceType serviceType) {
//        String param = String.format(runFileParam, pathFile, serviceType.getName());
//        if (!open(runFile, param)) {
//            log.error("Не удалось выполнить команду {} c параметрами {}", runFile, param);
//        }
        String param = String.format(runFileParam, serviceType.getName());
        try {

            new ProcessBuilder(runFile, "-jar", param).directory(new File(pathFile)).start();
        } catch (IOException e) {
            log.error("Не удалось выполнить команду {} c параметрами {}", runFile, param);
        }
    }

    private void updateOneService(ServiceIntegration serviceIntegration, String token) {
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

    public static boolean open(String command, String param) {

        try {
            Runtime.getRuntime().exec(command + " " + param);
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        try {
//            if (OSDetector.isWindows()) {
//                Runtime.getRuntime().exec(new String[]
//                        {"rundll32", "url.dll,FileProtocolHandler",
//                                command});
//                return true;
//            } else if (OSDetector.isLinux() || OSDetector.isMac()) {
//                Runtime.getRuntime().exec(new String[]{"/usr/bin/open",
//                        command});
//                return true;
//            }
//            else {
        // Unknown OS, try with desktop
//                if (Desktop.isDesktopSupported()) {
//                    Desktop.getDesktop().(command);
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace(System.err);
//            return false;
//        }
//        return false;
    }

}
