package ru.darujo.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.darujo.dto.user.UserInfoTypeActiveDto;
import ru.darujo.model.User;
import ru.darujo.model.UserInfoType;
import ru.darujo.repository.UserInfoTypeRepository;
import ru.darujo.specifications.Specifications;
import ru.darujo.type.MessageType;

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
    public void setUserInfoTypes(User user, String senderType, Collection<UserInfoTypeActiveDto> userInfoTypeDto, Collection<UserInfoTypeActiveDto> userInfoTypeProjectDto) {
        setUserInfoTypes(user, senderType, userInfoTypeDto);
        setUserInfoTypes(user, senderType, userInfoTypeProjectDto);
    }


    private void setUserInfoTypes(User user, String senderType, Collection<UserInfoTypeActiveDto> userInfoTypeDto) {
        getInfoTypes(user, senderType).forEach(userInfoType ->
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
                    userInfoType.setSenderType(senderType);

                }
                userInfoType.setIsActive(true);
                save(userInfoType);
            }

        });
    }

    public List<UserInfoType> getInfoTypes(User user, String senderType) {

        return getInfoTypes(user, senderType, null, null, null);
    }

    public List<UserInfoType> getInfoTypes(MessageType messageType) {
        return getInfoTypes(null, null, null, null, messageType.toString());
    }

    public List<UserInfoType> getInfoTypes(User user, String senderType, String telegramId, Integer threadId, String messageType) {
        Specification<@NonNull UserInfoType> specification = getUserInfoTypeSpecification(user, senderType, telegramId, threadId, messageType);
        return userInfoTypeRepository.findAll(specification);
    }

    public Optional<UserInfoType> getInfoTypeForUser(User user, String senderType, Long projectId, String telegramId, Integer threadId, String messageType) {
        Specification<@NonNull UserInfoType> specification = getUserInfoTypeSpecification(user, senderType, telegramId, threadId, messageType);
        if (projectId == null) {
            specification = Specifications.isNull(specification, "projectId");
        } else {
            specification = Specifications.eq(specification, "projectId", projectId);
        }
        return userInfoTypeRepository.findOne(specification);
    }

    private static Specification<@NonNull UserInfoType> getUserInfoTypeSpecification(User user, String senderType, String telegramId, Integer threadId, String messageType) {
        Specification<@NonNull UserInfoType> specification = Specification.unrestricted();
        specification = Specifications.eq(specification, "user", user);
        specification = Specifications.eq(specification, "senderType", senderType);
        specification = Specifications.eq(specification, "telegramId", telegramId);
        specification = Specifications.eq(specification, "threadId", threadId);
        specification = Specifications.eq(specification, "code", messageType);
        return specification;
    }

    public void save(UserInfoType userInfoType) {
        if ((userInfoType.getIsActive() != null && userInfoType.getIsActive())
                || userInfoType.getChatId() != null
                || userInfoType.getThreadId() != null) {
            userInfoTypeRepository.save(userInfoType);
        } else {
            if (userInfoType.getId() != null) {
                userInfoTypeRepository.delete(userInfoType);
            }
        }
        log.info(userInfoType.toString());
    }

    public boolean exists(String senderType, String telegramId) {
        Specification<@NonNull UserInfoType> specification = getUserInfoTypeSpecification(null, senderType, telegramId, null, null);
        return userInfoTypeRepository.exists(specification);

    }
}
