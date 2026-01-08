package ru.darujo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.darujo.integration.ServiceIntegration;
import ru.darujo.model.RunnableNotException;
import ru.darujo.model.ServiceType;
import ru.darujo.object.ServiceIntegrationObject;

import java.util.List;

@Slf4j
@Component
public class TaskService {
    private MonitorService monitorService;

    @Autowired
    public void setMonitorService(MonitorService monitorService) {
        this.monitorService = monitorService;
    }

    @Value("${update.run-service-command}")
    private String runFile;
    @Value("${update.save-into}")
    private String pathFile;
    @Value("${update.unpack-command}")
    private String unpack;

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
                        updateOneService(serviceIntegration.getServiceIntegration(), token);
                        // todo надо чтобы сервис уведомлений закрывался последним
                        Thread.sleep(80000);
                    }
                }

                fileNameUpdates.forEach(s ->
                        open(String.format(unpack, s))
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
        String command = String.format(runFile, serviceType.getName(), serviceType.getPort(), pathFile, serviceType.getName());
        if (!open(command)) {
            log.error("Не удалось выполнить команду {}", command);
        }
    }

    private void updateOneService(ServiceIntegration serviceIntegration, String token) {
        serviceIntegration.shutDown(token);
    }

    public static boolean open(String command) {
        try {
            if (OSDetector.isWindows()) {
                Runtime.getRuntime().exec(new String[]
                        {"rundll32", "url.dll,FileProtocolHandler",
                                command});
                return true;
            } else if (OSDetector.isLinux() || OSDetector.isMac()) {
                Runtime.getRuntime().exec(new String[]{"/usr/bin/open",
                        command});
                return true;
            }
//            else {
            // Unknown OS, try with desktop
//                if (Desktop.isDesktopSupported()) {
//                    Desktop.getDesktop().(command);
//                    return true;
//                } else {
//                    return false;
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return false;
        }
        return false;
    }

}
