package ru.darujo.service;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.darujo.integration.*;
import ru.darujo.model.ServiceType;
import ru.darujo.object.ServiceIntegrationObject;

import java.util.*;
import java.util.function.Consumer;

@Slf4j
@Service
public class MonitorService {
    @Getter
    private final PriorityQueue<ServiceIntegrationObject> serviceIntegrations = new PriorityQueue<>(Comparator.comparing(ServiceIntegrationObject::getSort));

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
        serviceIntegrations.add(new ServiceIntegrationObject(serviceType, serviceIntegration, serviceType.getPriorityStop()));
    }

    @PostConstruct
    public void init() {
        availService();


    }

    public void availService() {
        Set<ServiceType> serviceIntegrationsError = Collections.synchronizedSet(new HashSet<>());

        availService(serviceIntegrations, serviceIntegrationsError::add);

        serviceStatusService.newServiceStatus(serviceIntegrationsError);
    }

    public void availService(PriorityQueue<ServiceIntegrationObject> serviceIntegrations, Consumer<ServiceType> addService) {

        serviceIntegrations.forEach((serviceIntegrationObj) -> {
            try {

                serviceIntegrationObj.getServiceIntegration().test();
                log.info("Сервис {} в строю", serviceIntegrationObj.getServiceType());
            } catch (RuntimeException ex) {
                addService.accept(serviceIntegrationObj.getServiceType());
                log.error("Не прошла команда тест {} {}", serviceIntegrationObj.getServiceType(), ex.getMessage());
            }

        });

    }

    String token;

    public void stopServiceAll() {
        stopService(serviceIntegrations);

    }

    public String getToken() {
        return ((UserServiceIntegration) serviceIntegrations.stream().filter(serviceIntegrationObject -> serviceIntegrationObject.getServiceType().equals(ServiceType.USER)).findAny().orElseThrow(() -> new RuntimeException("")).getServiceIntegration()
        ).getToken("system_user_update", "Приносить пользу миру — это единственный способ стать счастливым.").getToken();
    }

    public void stopService(PriorityQueue<ServiceIntegrationObject> serviceIntegrations) {
        token = getToken();
        serviceIntegrations.forEach((serviceIntegrationObject) -> {
            try {
                serviceIntegrationObject.getServiceIntegration().shutDown(token);
                log.info("Команда остановки успешно отправлена сервису {}", serviceIntegrationObject.getServiceType());
            } catch (RuntimeException ex) {
                log.error("Сервис {} {}", serviceIntegrationObject.getServiceType(), ex.getMessage());
            }

        });

    }
}
