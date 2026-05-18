package ru.darujo.service;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.darujo.integration.ServiceIntegration;
import ru.darujo.integration.ServiceType;
import ru.darujo.integration.UserServiceIntegrationImp;
import ru.darujo.object.ServiceIntegrationObject;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

@Slf4j
@Service
public class MonitorService {
    @Getter
    private final PriorityQueue<ServiceIntegrationObject> serviceIntegrations = new PriorityQueue<>(Comparator.comparing(ServiceIntegrationObject::getSort));


    @Autowired
    public void setIntegrationObject(List<ServiceIntegration<ServiceType>> integrationObjectList) {
        integrationObjectList.forEach(this::addServiceIntegration);
    }

    private ServiceStatusService serviceStatusService;

    @Autowired
    public void setServiceStatusService(ServiceStatusService serviceStatusService) {
        this.serviceStatusService = serviceStatusService;
    }


    private void addServiceIntegration(ServiceIntegration<ServiceType> serviceIntegration) {
        log.info(serviceIntegration.getServiceType().toString());
        serviceIntegrations.add(new ServiceIntegrationObject(serviceIntegration));
    }

    @PostConstruct
    public void init() {
//        availService();


    }

    public void availService() {
        Map<ServiceType, Integer> serviceIntegrationsError = getServiceErrorTypes(null, true);
        AtomicBoolean flagError = new AtomicBoolean(false);
        serviceIntegrationsError.forEach((serviceType, count) -> {
            if (count > 2) {
                flagError.set(true);
            }
        });
        if (flagError.get()) {
            serviceStatusService.newServiceStatus(serviceIntegrationsError.keySet());
        }
    }

    public boolean allServiceOk(List<ServiceType> serviceTypeList) {
        Map<ServiceType, Integer> serviceIntegrationsError = getServiceErrorTypes(serviceTypeList, false);

        return serviceIntegrationsError.isEmpty();
    }

    Map<ServiceType, Integer> serviceIntegrationsError = Collections.synchronizedMap(new HashMap<>());

    private @NonNull Map<ServiceType, Integer> getServiceErrorTypes(List<ServiceType> serviceTypeList, boolean addCount) {
        for (ServiceType serviceType : ServiceType.values()) {
            if (serviceTypeList == null || serviceTypeList.contains(serviceType)) {
                serviceIntegrationsError.putIfAbsent(serviceType, 0);
            }
        }
        Set<ServiceType> serviceOk = Collections.synchronizedSet(new HashSet<>());

        availService(serviceIntegrations, serviceTypeList, serviceOk::add);
        serviceOk.forEach(serviceType -> serviceIntegrationsError.remove(serviceType));
        if (addCount) {
            serviceIntegrationsError.forEach((serviceType, count) -> serviceIntegrationsError.put(serviceType, count + 1));
        }
        return serviceIntegrationsError;
    }

    public void availService(PriorityQueue<ServiceIntegrationObject> serviceIntegrations,
                             List<ServiceType> serviceTypeList,
                             Consumer<ServiceType> addService) {

        serviceIntegrations
                .stream()
                .filter(serviceIntegrationObject -> serviceTypeList == null || serviceTypeList.contains(serviceIntegrationObject.getServiceType()))
                .forEach((serviceIntegrationObj) -> {
            try {

                serviceIntegrationObj.getServiceIntegration().test();
                addService.accept(serviceIntegrationObj.getServiceType());
//                log.info("Сервис {} в строю", serviceIntegrationObj.getServiceType());
            } catch (RuntimeException ex) {

//                log.error("Не прошла команда тест {} {}", serviceIntegrationObj.getServiceType(), ex.getMessage());
            }

        });


    }

    String token;

    public void stopServiceAll() {
        stopService(serviceIntegrations);

    }

    public String getToken() {
        try {


            return ((UserServiceIntegrationImp) serviceIntegrations.stream().filter(serviceIntegrationObject -> serviceIntegrationObject.getServiceType().equals(ServiceType.USER)).findAny().orElseThrow(() -> new RuntimeException("")).getServiceIntegration()
        ).getToken("system_user_update", "Приносить пользу миру — это единственный способ стать счастливым.").getToken();
        } catch (RuntimeException ex) {
            return "";
        }
    }

    public void stopService(PriorityQueue<ServiceIntegrationObject> serviceIntegrations) {
        token = getToken();
        serviceIntegrations.forEach((serviceIntegrationObject) -> {
            try {
                serviceIntegrationObject.getServiceIntegration().shutDown(token);
                log.info("Команда остановки успешно отправлена сервису {}", serviceIntegrationObject.getServiceType());
            } catch (RuntimeException ex) {
                log.error("Сервис {} {}", serviceIntegrationObject.getServiceType(), ex.getMessage(), ex);
            }

        });

    }
}
