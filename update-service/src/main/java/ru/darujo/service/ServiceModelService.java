package ru.darujo.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.model.ServiceModel;
import ru.darujo.model.ServiceType;
import ru.darujo.repository.ServiceModelRepository;

@Service
public class ServiceModelService {
    private ServiceModelRepository serviceModelRepository;

    @Autowired
    public void setServiceModelRepository(ServiceModelRepository serviceModelRepository) {
        this.serviceModelRepository = serviceModelRepository;
    }

    @PostConstruct
    public void init() {
        for (ServiceType serviceType : ServiceType.values()) {
            if (serviceModelRepository.findByNameIgnoreCase(serviceType.name()).orElse(
                    null) == null) {
                serviceModelRepository.save(new ServiceModel(null, serviceType.name()));
            }
        }
    }

    public ServiceModel getServiceModel(String name) {
        return serviceModelRepository.findByNameIgnoreCase(name).orElseThrow(() -> new ResourceNotFoundRunTime("Беда сервиса нет"));
    }
}
