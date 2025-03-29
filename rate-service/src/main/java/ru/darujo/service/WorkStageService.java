package ru.darujo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.darujo.dto.UserDto;
import ru.darujo.dto.UserFio;
import ru.darujo.exceptions.ResourceNotFoundException;
import ru.darujo.integration.UserServiceIntegration;
import ru.darujo.model.WorkStage;
import ru.darujo.repository.WorkStageRepository;
import ru.darujo.repository.specifications.WorkStageSpecifications;

import java.util.*;

@Service
@Primary
public class WorkStageService {
    UserServiceIntegration userServiceIntegration;

    @Autowired
    public void setUserServiceIntegration(UserServiceIntegration userServiceIntegration) {
        this.userServiceIntegration = userServiceIntegration;
    }
    private WorkStageRepository workStageRepository;
    @Autowired
    public void setWorkStageRepository(WorkStageRepository workStageRepository) {
        this.workStageRepository = workStageRepository;
    }

    public Optional<WorkStage> findById(long id) {
        return workStageRepository.findById(id);
    }
    private void validWorkStage(WorkStage workStage){
        if (workStage.getWorkId() == null) {
            throw new ResourceNotFoundException("Не могу найти привязку к ЗИ");
        }
        if (workStage.getNikName() == null || workStage.getNikName().equals("") )  {
            throw new ResourceNotFoundException("Не заполнено ФИО");
        }
        if (workStage.getRole() == null) {
            throw new ResourceNotFoundException("Не заполнено роль");
        }
        Specification<WorkStage> specification = Specification
                .where(WorkStageSpecifications.workIdEq(workStage.getWorkId()))
                .and(WorkStageSpecifications.nikNameEq(workStage.getNikName()))
                .and(WorkStageSpecifications.roleEq(workStage.getRole()));

        WorkStage workStageFind =workStageRepository.findOne(specification).orElse(null);
        if(workStageFind != null){
            throw new ResourceNotFoundException("Уже есть запись с таким ФИО и Ролью");
        }

    }
    public WorkStage saveWorkStage(WorkStage workStage) {
        validWorkStage(workStage);
        return workStageRepository.save(workStage);
    }

    public void deleteWorkStage(Long id) {
        workStageRepository.deleteById(id);
    }


    public List<WorkStage> findWorkStage(Long workId) {
        Specification<WorkStage> specification = Specification.where(WorkStageSpecifications.workIdEq(workId));


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
        } catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
            userFio.setFirstName("Не найден пользователь с ником " + userFio.getNikName());
        }
    }
}
