package ru.darujo.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.darujo.dto.information.MessageType;
import ru.darujo.dto.user.UserInfoTypeActiveDto;
import ru.darujo.model.User;
import ru.darujo.model.UserInfoType;
import ru.darujo.repository.UserInfoTypeRepository;
import ru.darujo.specifications.Specifications;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserInfoTypeService {
    private UserInfoTypeRepository userInfoTypeRepository;

    @Autowired
    public void setUserInfoTypeRepository(UserInfoTypeRepository userInfoTypeRepository) {
        this.userInfoTypeRepository = userInfoTypeRepository;
    }

    @Transactional
    public void setUserInfoTypes(User user, Collection<UserInfoTypeActiveDto> userInfoTypeDto, Collection<UserInfoTypeActiveDto> userInfoTypeProjectDto) {
        setUserInfoTypes(user, userInfoTypeDto);
        setUserInfoTypes(user, userInfoTypeProjectDto);
    }


    private void setUserInfoTypes(User user, Collection<UserInfoTypeActiveDto> userInfoTypeDto) {
        getInfoTypes(user).forEach(userInfoType ->
        {
            if (userInfoTypeDto
                    .stream()
                    .anyMatch(userInfoTypeActiveDto ->
                            !userInfoTypeActiveDto.getActive()
                                    && userInfoType.getCode().equals(userInfoTypeActiveDto.getCode())
                                    && ((userInfoType.getProjectId() == null && userInfoTypeActiveDto.getProjectId() == null) ||
                                    (userInfoType.getProjectId() != null && userInfoType.getProjectId().equals(userInfoTypeActiveDto.getProjectId())))
                    )) {
                userInfoType.setIsActive(false);
                save(userInfoType);
            }
        });

        userInfoTypeDto.forEach(userInfoTypeActiveDto -> {
            if (userInfoTypeActiveDto.getActive()) {
                UserInfoType userInfoType;
                if (userInfoTypeActiveDto.getProjectId() == null) {
                    userInfoType = userInfoTypeRepository.findFirstByCodeAndUserAndProjectIdIsNull(userInfoTypeActiveDto.getCode(), user).orElse(null);
                } else {
                    userInfoType = userInfoTypeRepository.findFirstByCodeAndUserAndProjectId(userInfoTypeActiveDto.getCode(), user, userInfoTypeActiveDto.getProjectId()).orElse(null);
                }
                if (userInfoType == null) {
                    userInfoType = new UserInfoType(userInfoTypeActiveDto.getProjectId(),
                            userInfoTypeActiveDto.getCode(),
                            user);

                }
                userInfoType.setIsActive(true);
                save(userInfoType);
            }

        });
    }

    public List<UserInfoType> getInfoTypes(User user) {

        return getInfoTypes(user, null, null, null);
    }

    public List<UserInfoType> getInfoTypes(MessageType messageType) {
        return getInfoTypes(null, null, null, messageType.toString());
    }

    public List<UserInfoType> getInfoTypes(User user, Long telegramId, Integer threadId, String messageType) {
        Specification<@NonNull UserInfoType> specification = getUserInfoTypeSpecification(user, telegramId, threadId, messageType);
        return userInfoTypeRepository.findAll(specification);
    }

    public Optional<UserInfoType> getInfoTypeForUser(User user, Long projectId, Long telegramId, Integer threadId, String messageType) {
        Specification<@NonNull UserInfoType> specification = getUserInfoTypeSpecification(user, telegramId, threadId, messageType);
        if (projectId == null) {
            specification = Specifications.isNull(specification, "projectId");
        } else {
            specification = Specifications.eq(specification, "projectId", projectId);
        }
        return userInfoTypeRepository.findOne(specification);
    }

    private static Specification<@NonNull UserInfoType> getUserInfoTypeSpecification(User user, Long telegramId, Integer threadId, String messageType) {
        Specification<@NonNull UserInfoType> specification = Specification.unrestricted();
        specification = Specifications.eq(specification, "user", user);
        specification = Specifications.eq(specification, "telegramId", telegramId);
        specification = Specifications.eq(specification, "threadId", threadId);
        specification = Specifications.eq(specification, "code", messageType);
        return specification;
    }

    public void save(UserInfoType userInfoType) {
        if ((userInfoType.getIsActive() != null && userInfoType.getIsActive())
                || userInfoType.getTelegramId() != null
                || userInfoType.getThreadId() != null) {
            userInfoTypeRepository.save(userInfoType);
        } else {
            if (userInfoType.getId() != null) {
                userInfoTypeRepository.delete(userInfoType);
            }
        }
        log.info(userInfoType.toString());
    }

    public boolean exists(Long telegramId) {
        Specification<@NonNull UserInfoType> specification = getUserInfoTypeSpecification(null, telegramId, null, null);
        return userInfoTypeRepository.exists(specification);

    }
}
