package ru.darujo.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.darujo.integration.*;
import ru.darujo.model.ServiceType;

import java.util.*;
import java.util.function.Consumer;

@Slf4j
@Service
public class MonitorService {
    private final Map<ServiceType, ServiceIntegration> serviceIntegrationMapFirst = new HashMap<>();
    private final Map<ServiceType, ServiceIntegration> serviceIntegrationMapSecond = new HashMap<>();
    private ServiceStatusService serviceStatusService;

    @Autowired
    public void setServiceStatusService(ServiceStatusService serviceStatusService) {
        this.serviceStatusService = serviceStatusService;
    }

    @Autowired
    public void setCalendarServiceIntegration(CalendarServiceIntegration calendarServiceIntegration) {
        addServiceIntegration(ServiceType.CALENDAR, calendarServiceIntegration);
    }

    @Autowired
    public void setInfoServiceIntegration(InfoServiceIntegration infoServiceIntegration) {
        addServiceIntegration(ServiceType.INFORMATION, infoServiceIntegration);
    }

    @Autowired
    public void setRateServiceIntegration(RateServiceIntegration rateServiceIntegration) {
        addServiceIntegration(ServiceType.RATE, rateServiceIntegration);
    }

    @Autowired
    public void setTaskServiceIntegration(TaskServiceIntegration taskServiceIntegration) {
        addServiceIntegration(ServiceType.TASK, taskServiceIntegration);
    }

    @Autowired
    public void setTelegramServiceIntegration(TelegramServiceIntegration telegramServiceIntegration) {
        addServiceIntegration(ServiceType.TELEGRAM, telegramServiceIntegration);
    }

    @Autowired
    public void setUserServiceIntegration(UserServiceIntegration userServiceIntegration) {
        addServiceIntegration(ServiceType.USER, userServiceIntegration);
    }

    @Autowired
    public void setWorkServiceIntegration(WorkServiceIntegration workServiceIntegration) {
        addServiceIntegration(ServiceType.WORK, workServiceIntegration);
    }

    @Autowired
    public void setWorkTimeServiceIntegration(WorkTimeServiceIntegration workTimeServiceIntegration) {
        addServiceIntegration(ServiceType.WORK_TIME, workTimeServiceIntegration);
    }

    @Autowired
    public void setFrontServiceIntegration(FrontServiceIntegration frontServiceIntegration) {
        addServiceIntegration(ServiceType.FRONT, frontServiceIntegration);
    }

    @Autowired
    public void setGateWayServiceIntegration(GateWayServiceIntegration gateWayServiceIntegration) {
        addServiceIntegration(ServiceType.GATE_WAY, gateWayServiceIntegration);
    }

    private void addServiceIntegration(ServiceType serviceType, ServiceIntegration serviceIntegration) {
        log.info(serviceType.toString());
        serviceIntegrationMapFirst.put(serviceType, serviceIntegration);
    }

    @PostConstruct
    public void init() {
        availService();
//        stopServiceFirst();
//        stopServiceSecond();


    }

    public void availService() {
        Set<ServiceType> serviceIntegrationsError = Collections.synchronizedSet(new HashSet<>());

        availService(serviceIntegrationMapFirst, serviceIntegrationsError::add);
        availService(serviceIntegrationMapSecond, serviceIntegrationsError::add);
        serviceStatusService.newServiceStatus(serviceIntegrationsError);
    }

    public void availService(Map<ServiceType, ServiceIntegration> serviceIntegrations, Consumer<ServiceType> addService) {

        serviceIntegrations.forEach((serviceType, serviceIntegration) -> {
            try {

                serviceIntegration.test();
                log.info("Сервис {} в строю", serviceType);
            } catch (RuntimeException ex) {
                addService.accept(serviceType);
                log.error("Не прошла команда тест {} {}", serviceType, ex.getMessage());
            }

        });

    }

    String token;

    public void stopServiceFirst() {
        stopService(serviceIntegrationMapFirst);

    }

    public void stopServiceSecond() {
        stopService(serviceIntegrationMapSecond);

    }

    public void stopService(Map<ServiceType, ServiceIntegration> serviceIntegrations) {
        token = ((UserServiceIntegration) serviceIntegrations.get(ServiceType.USER)).getToken("system_user_update", "Приносить пользу миру — это единственный способ стать счастливым.").getToken();
        serviceIntegrationMapFirst.forEach((name, serviceIntegration) -> {
            try {

                serviceIntegration.shutDown(token);
                log.info("Команда остановки успешно отправлена сервису {}", name);
            } catch (RuntimeException ex) {
                log.error("Сервис {} {}", name, ex.getMessage());
            }

        });

    }
}
