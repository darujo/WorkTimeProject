package ru.darujo.service;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.darujo.dto.MapStringFloat;
import ru.darujo.dto.ratestage.WorkStageDto;
import ru.darujo.dto.user.UserFio;
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.integration.UserServiceIntegration;
import ru.darujo.integration.WorkServiceIntegration;
import ru.darujo.model.WorkStage;
import ru.darujo.repository.WorkStageRepository;
import ru.darujo.specifications.Specifications;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@Primary
public class WorkStageService {
    UserServiceIntegration userServiceIntegration;

    @Autowired
    public void setUserServiceIntegration(UserServiceIntegration userServiceIntegration) {
        this.userServiceIntegration = userServiceIntegration;
    }

    WorkServiceIntegration workServiceIntegration;

    @Autowired
    public void setWorkServiceIntegration(WorkServiceIntegration workServiceIntegration) {
        this.workServiceIntegration = workServiceIntegration;
    }

    private WorkStageRepository workStageRepository;

    @Autowired
    public void setWorkStageRepository(WorkStageRepository workStageRepository) {
        this.workStageRepository = workStageRepository;
    }

    public Optional<WorkStage> findById(long id) {
        return workStageRepository.findById(id);
    }

    private void validWorkStage(WorkStage workStage) {
        if (workStage.getWorkId() == null) {
            throw new ResourceNotFoundRunTime("Не могу найти привязку к ЗИ");
        }
        if (workStage.getNikName() == null || workStage.getNikName().isEmpty()) {
            throw new ResourceNotFoundRunTime("Не заполнено ФИО");
        }
        if (workStage.getRole() == null) {
            throw new ResourceNotFoundRunTime("Не заполнено роль");
        }

        checkAvailUser(workStage);

    }

    private void checkAvailUser(WorkStage workStage) {
        Specification<@NonNull WorkStage> specification = Specifications.eq(null, "workId", workStage.getWorkId());
        specification = Specifications.eq(specification, "nikName", workStage.getNikName());
        specification = Specifications.eq(specification, "role", workStage.getRole());
        specification = Specifications.ne(specification, "id", workStage.getId());
        WorkStage workStageFind = workStageRepository.findOne(specification).orElse(null);
        if (workStageFind != null) {
            throw new ResourceNotFoundRunTime("Уже есть запись с таким ФИО и ролью");
        }
    }

    @Transactional
    public WorkStage saveWorkStage(WorkStage workStage) {
        validWorkStage(workStage);
        return workStageRepository.save(workStage);
    }

    public void deleteWorkStage(Long id) {
        workStageRepository.deleteById(id);
    }


    public List<WorkStage> findWorkStage(Long workId, Integer role, Long projectId) {
        Specification<@NonNull WorkStage> specification = Specification.where(Specifications.eq(null, "workId", workId));
        specification = Specifications.eq(specification, "role", role);
        specification = Specifications.eq(specification, "projectId", projectId);
        return workStageRepository.findAll(specification);
    }


    public void updFio(UserFio userFio) {
        userServiceIntegration.updFio(userFio);
    }

    private MapStringFloat getWorkTimeFact(Long workId, Long projectId, Integer stage) {
        try {
            return workServiceIntegration.getWorkTimeStageFact(workId, projectId, stage);
        } catch (RuntimeException ex) {
            return null;
        }
    }

    public void updWorkStage(Long workId, List<WorkStageDto> workStages, Long projectId) {
        for (int stage = 0; stage < 6; stage++) {
            MapStringFloat mapStringFloat = getWorkTimeFact(workId, projectId, stage);
            if (mapStringFloat != null && !mapStringFloat.getList().isEmpty()) {
                Map<String, WorkStageDto> workStageMap = new HashMap<>();
                workStages.forEach(workStage -> workStageMap.put(workStage.getNikName(), workStage));
                int finalStage = stage;
                mapStringFloat.getList().forEach((nikName, time) -> {
                    WorkStageDto workStage = workStageMap.get(nikName);
                    if (workStage == null) {
                        workStage = new WorkStageDto(-1L, nikName, -1, null, null, null, null, null, workId, projectId);
                        updFio(workStage);
                        workStages.add(workStage);
                    }
                    workStage.setStageFact(finalStage, time);
                });
            }
        }
    }

    public List<WorkStage> findWorkStage(Long workId, Long projectId) {
        return findWorkStage(workId, null, projectId);
    }
}
