package ru.darujo.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.darujo.dto.MapStringFloat;
import ru.darujo.dto.ratestage.WorkStageDto;
import ru.darujo.dto.user.UserDto;
import ru.darujo.dto.user.UserFio;
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.integration.UserServiceIntegration;
import ru.darujo.integration.WorkServiceIntegration;
import ru.darujo.model.WorkStage;
import ru.darujo.repository.WorkStageRepository;
import ru.darujo.specifications.Specifications;

import javax.transaction.Transactional;
import java.util.*;

@Log4j2
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
        if (workStage.getNikName() == null || workStage.getNikName().equals("")) {
            throw new ResourceNotFoundRunTime("Не заполнено ФИО");
        }
        if (workStage.getRole() == null) {
            throw new ResourceNotFoundRunTime("Не заполнено роль");
        }

        checkAvailUser(workStage);

    }

    private void checkAvailUser(WorkStage workStage) {
        Specification<WorkStage> specification = Specifications.eq(null, "workId", workStage.getWorkId());
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


    public List<WorkStage> findWorkStage(Long workId, Integer role) {
        Specification<WorkStage> specification = Specification.where(Specifications.eq(null, "workId", workId));
        specification = Specifications.eq(specification, "role", role);
        return workStageRepository.findAll(specification);
    }

    private final Map<String, UserDto> userDtoMap = new HashMap<>();

    public void updFio(UserFio userFio) {
        try {
            if (userFio.getNikName() != null) {
                UserDto userDto = userDtoMap.get(userFio.getNikName());
                if (userDto == null) {
                    userDto = userServiceIntegration.getUserDto(null, userFio.getNikName());
                    userDtoMap.put(userFio.getNikName(), userDto);
                }
                userFio.setFirstName(userDto.getFirstName());
                userFio.setLastName(userDto.getLastName());
                userFio.setPatronymic(userDto.getPatronymic());
            }
        } catch (ResourceNotFoundRunTime e) {
            log.error(e.getMessage());
            userFio.setFirstName("Не найден пользователь с ником " + userFio.getNikName());
        }
    }

    private MapStringFloat getWorkTimeAnaliseFact(Long workId) {
        try {
            return workServiceIntegration.getWorkTimeStageFact(workId);
        } catch (RuntimeException ex) {
            return new MapStringFloat();
        }
    }

    public void updWorkStage(Long workId, List<WorkStageDto> workStages) {
        MapStringFloat mapStringFloat = getWorkTimeAnaliseFact(workId);
        if (mapStringFloat.getList().size() != 0) {
            Map<String, WorkStageDto> workStageMap = new HashMap<>();
            workStages.forEach(workStage -> workStageMap.put(workStage.getNikName(), workStage));
            mapStringFloat.getList().forEach((nikName, time) -> {
                WorkStageDto workStage = workStageMap.get(nikName);
                if (workStage != null) {
                    workStage.setStage0Fact(time);
                } else {
                    workStage = new WorkStageDto(-1L, nikName, -1, null, null, null, null, null, workId);
                    workStage.setStage0Fact(time);
                    updFio(workStage);
                    workStages.add(workStage);
                }
            });
        }
    }

    public List<WorkStage> findWorkStage(Long workId) {
        return findWorkStage(workId, null);
    }
}
