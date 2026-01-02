package ru.darujo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "status_service")
public class ServiceStatus {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "time_create")
    private Timestamp timeCreate;
    @ManyToMany
    @JoinTable(name = "status_service_servicce",
            joinColumns = @JoinColumn(name = "status_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id"))
    private List<ServiceModel> serviceError;

    public ServiceStatus(Set<ServiceModel> serviceError) {
        this.timeCreate = new Timestamp(System.currentTimeMillis());
        this.serviceError = serviceError.stream().toList();
    }
}
