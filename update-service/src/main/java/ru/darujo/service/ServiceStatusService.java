package ru.darujo.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.integration.TelegramServiceIntegration;
import ru.darujo.model.ServiceModel;
import ru.darujo.model.ServiceStatus;
import ru.darujo.model.ServiceType;
import ru.darujo.repository.ServiceStatusRepository;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class ServiceStatusService {
    private ServiceStatusRepository serviceStatusRepository;
    private ServiceModelService serviceModelService;
    private TelegramServiceIntegration telegramServiceIntegration;

    @Autowired
    public void setTelegramServiceIntegration(TelegramServiceIntegration telegramServiceIntegration) {
        this.telegramServiceIntegration = telegramServiceIntegration;
    }

    @Autowired
    public void setServiceStatusRepository(ServiceStatusRepository serviceStatusRepository) {
        this.serviceStatusRepository = serviceStatusRepository;
    }

    @Autowired
    public void setServiceModelService(ServiceModelService serviceModelService) {
        this.serviceModelService = serviceModelService;
    }

    @Transactional
    public void newServiceStatus(Set<ServiceType> serviceTypeList) {
        ServiceStatus serviceStatus = serviceStatusRepository.findDistinctTopByOrderByIdDesc().orElse(null);

        if (serviceStatus == null) {
            addServiceStatus(serviceTypeList);
        } else {
//              serviceStatus.getServiceError();
            if (serviceTypeList.size() != serviceStatus.getServiceError().size()) {
                addServiceStatus(serviceTypeList);
            } else {
                if (serviceStatus.getServiceError()
                        .stream()
                        .anyMatch(serviceModel ->
                                !serviceTypeList.contains(ServiceType.valueOf(serviceModel.getName())))
                ) {
                    addServiceStatus(serviceTypeList);
                }

            }

        }

    }

    private void addServiceStatus(Set<ServiceType> serviceTypeList) {
        boolean allServiceOk = serviceTypeList.isEmpty();
        Set<ServiceModel> serviceModels = new HashSet<>();
        serviceTypeList.forEach(serviceType -> serviceModels.add(serviceModelService.getServiceModel(serviceType.name())));
        try {
            telegramServiceIntegration.sendMessageForAdmin(allServiceOk ? "Все сервисы доступны" :
                    String.format("Не доступные сервисы %s", serviceTypeList));
        } catch (ResourceNotFoundRunTime ex) {
            log.error(ex.getMessage());
        }
        if (allServiceOk) {
            UpdateService.getINSTANCE().allServiceOk();
        }
        serviceStatusRepository.save(new ServiceStatus(serviceModels));
    }


}
