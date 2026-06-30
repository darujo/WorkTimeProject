package ru.darujo.object;

import lombok.Getter;
import lombok.Setter;
import ru.darujo.integration.ServiceIntegration;
import ru.darujo.integration.ServiceType;


@Getter
public class ServiceIntegrationObject {
    private final ServiceIntegration<ServiceType> serviceIntegration;
    @Setter
    private Process process;

    public ServiceIntegrationObject(ServiceIntegration<ServiceType> serviceIntegration) {
        this.serviceIntegration = serviceIntegration;
    }

    public int getSort() {
        return serviceIntegration.getServiceType().getPriorityStop();
    }

    public ServiceType getServiceType() {
        return serviceIntegration.getServiceType();
    }
}
