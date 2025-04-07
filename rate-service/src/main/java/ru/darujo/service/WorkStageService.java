package ru.darujo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.darujo.dto.MapStringFloat;
import ru.darujo.dto.UserDto;
import ru.darujo.dto.UserFio;
import ru.darujo.exceptions.ResourceNotFoundException;
import ru.darujo.integration.UserServiceIntegration;
import ru.darujo.integration.WorkServiceIntegration;
import ru.darujo.model.WorkStage;
import ru.darujo.repository.WorkStageRepository;
import ru.darujo.repository.specifications.WorkStageSpecifications;

import javax.transaction.Transactional;
import java.util.*;

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
            throw new ResourceNotFoundException("Не могу найти привязку к ЗИ");
        }
        if (workStage.getNikName() == null || workStage.getNikName().equals("")) {
            throw new ResourceNotFoundException("Не заполнено ФИО");
        }
        if (workStage.getRole() == null) {
            throw new ResourceNotFoundException("Не заполнено роль");
        }

        if (workStage.getId() == null) {
            checkAvailUser(workStage.getWorkId(), workStage.getNikName());
        } else {
            WorkStage workStageOld = workStageRepository.findById(workStage.getId()).orElseThrow(() -> new ResourceNotFoundException("Кто-то удалил запись"));
            if (!workStage.getNikName().equals(workStageOld.getNikName())) {
                checkAvailUser(workStage.getWorkId(), workStage.getNikName());
            }
        }
    }

    private void checkAvailUser(Long workId, String nikName) {
        Specification<WorkStage> specification = Specification.where(WorkStageSpecifications.workIdEq(workId)).and(WorkStageSpecifications.nikNameEq(nikName))
//                    .and(WorkStageSpecifications.roleEq(workStage.getRole()))
                ;

        WorkStage workStageFind = workStageRepository.findOne(specification).orElse(null);
        if (workStageFind != null) {
            throw new ResourceNotFoundException("Уже есть запись с таким ФИО");
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


    public List<WorkStage> findWorkStage(Long workId, Integer role, boolean loadFact) {
        Specification<WorkStage> specification = Specification.where(WorkStageSpecifications.workIdEq(workId));
        if (role != null) {
            specification = specification.and(WorkStageSpecifications.roleEq(role));
        }

        List<WorkStage> workStages = workStageRepository.findAll(specification);
        if(loadFact) {
            updWorkStage(workId, workStages);
        }
        return workStages;
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
        } catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
            userFio.setFirstName("Не найден пользователь с ником " + userFio.getNikName());
        }
    }

//    private Float getWorkTimeAnaliseFact(Long workId, String nikName) {
//        try {
//            MapStringFloat mapStringFloat = workServiceIntegration.getWorkTimeStageFact(workId, 0, nikName);
//            if (mapStringFloat.getList().size() == 0) {
//                return 0f;
//            }
//            return mapStringFloat.getList().get(nikName);
//        } catch (RuntimeException ex) {
//            return 0f;
//        }
//    }

    private MapStringFloat getWorkTimeAnaliseFact(Long workId) {
        try {
            return workServiceIntegration.getWorkTimeStageFact(workId, null);
        } catch (RuntimeException ex) {
            return new MapStringFloat();
        }
    }

    private void updWorkStage(Long workId, List<WorkStage> workStages) {
        MapStringFloat mapStringFloat = getWorkTimeAnaliseFact(workId);
        if (mapStringFloat.getList().size() != 0) {
            Map<String, WorkStage> workStageMap = new HashMap<>();
            workStages.forEach(workStage -> workStageMap.put(workStage.getNikName(), workStage));
            mapStringFloat.getList().forEach((nikName, time) -> {
                WorkStage workStage = workStageMap.get(nikName);
                if (workStage != null) {
                    if (workStage.getStage0() == null || time > workStage.getStage0()) {
                        workStage.setStage0(time);
                    }
                } else {
                    workStage = new WorkStage(-1L, nikName, -1, time, 0f, 0f, 0f, 0f, workId);
                    workStages.add(workStage);
                }
            });
        }

    }

//    private void updWorkStage(WorkStage workStage) {
//        Float time = getWorkTimeAnaliseFact(workStage.getWorkId(), workStage.getNikName());
//        if (workStage.getStage0() == null || time > workStage.getStage0()) {
//            workStage.setStage0(time);
//        }
//    }

    public List<WorkStage> findWorkStage(Long workId,boolean loadFact) {
        return findWorkStage(workId, null, loadFact);
    }
}
