package ru.darujo.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.darujo.model.ServiceModel;
import ru.darujo.model.ServiceStatus;
import ru.darujo.model.ServiceType;
import ru.darujo.repository.ServiceStatusRepository;

import java.util.HashSet;
import java.util.Set;

@Service
public class ServiceStatusService {
    private ServiceStatusRepository serviceStatusRepository;
    private ServiceModelService serviceModelService;

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
        Set<ServiceModel> serviceModels = new HashSet<>();
        serviceTypeList.forEach(serviceType -> serviceModels.add(serviceModelService.getServiceModel(serviceType.name())));
        serviceStatusRepository.save(new ServiceStatus(serviceModels));
    }


}
