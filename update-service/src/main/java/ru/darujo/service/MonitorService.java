package ru.darujo.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.darujo.integration.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class MonitorService {
    private final Map<String, ServiceIntegration> serviceIntegrationMap = new HashMap<>();

    @Autowired
    public void setCalendarServiceIntegration(CalendarServiceIntegration calendarServiceIntegration) {
        addServiceIntegration("calendar", calendarServiceIntegration);
    }

    @Autowired
    public void setInfoServiceIntegration(InfoServiceIntegration infoServiceIntegration) {
        addServiceIntegration("info", infoServiceIntegration);
    }

    @Autowired
    public void setRateServiceIntegration(RateServiceIntegration rateServiceIntegration) {
        addServiceIntegration("rate", rateServiceIntegration);
    }

    @Autowired
    public void setTaskServiceIntegration(TaskServiceIntegration taskServiceIntegration) {
        addServiceIntegration("task", taskServiceIntegration);
    }

    @Autowired
    public void setTelegramServiceIntegration(TelegramServiceIntegration telegramServiceIntegration) {
        addServiceIntegration("telegram", telegramServiceIntegration);
    }

    @Autowired
    public void setUserServiceIntegration(UserServiceIntegration userServiceIntegration) {
        addServiceIntegration("user", userServiceIntegration);
    }

    @Autowired
    public void setWorkServiceIntegration(WorkServiceIntegration workServiceIntegration) {
        addServiceIntegration("work", workServiceIntegration);
    }

    @Autowired
    public void setWorkTimeServiceIntegration(WorkTimeServiceIntegration workTimeServiceIntegration) {
        addServiceIntegration("workTime", workTimeServiceIntegration);
    }

    private void addServiceIntegration(String name, ServiceIntegration serviceIntegration) {
        log.info(name);
        serviceIntegrationMap.put(name, serviceIntegration);
    }

    @PostConstruct
    public void availService() {
        serviceIntegrationMap.forEach((name, serviceIntegration) -> {
            try {

                serviceIntegration.test();
                log.info("Сервис {} в строю", name);
                serviceIntegration.shutDown();
                log.info("Команда остановкиуспешно отправлена сервису {}", name);
            } catch (RuntimeException ex) {
                log.error("Сервис {} {}", name, ex.getMessage());
            }

        });

    }
}
