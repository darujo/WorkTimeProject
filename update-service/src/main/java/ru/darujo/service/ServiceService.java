package ru.darujo.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.darujo.model.ServiceModel;
import ru.darujo.model.ServiceType;
import ru.darujo.repository.ServiceModelRepository;

@Service
public class ServiceService {
    private ServiceModelRepository serviceModelRepository;

    @Autowired
    public void setServiceRepository(ServiceModelRepository serviceModelRepository) {
        this.serviceModelRepository = serviceModelRepository;
    }

    @PostConstruct
    public void init() {
        for (ServiceType serviceType : ServiceType.values()) {
            if (serviceModelRepository.findByNameIgnoreCase(serviceType.name()).isEmpty()) {
                serviceModelRepository.save(new ServiceModel(null, serviceType.name()));
            }
        }
    }
}
