package ru.darujo.object;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.darujo.integration.ServiceIntegration;
import ru.darujo.model.ServiceType;

@AllArgsConstructor
@Getter
public class ServiceIntegrationObject {
    private ServiceType serviceType;
    private ServiceIntegration serviceIntegration;
    private Integer sort;
    @Setter
    private Process process;
}
