package ru.darujo.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.darujo.dto.information.MessageType;
import ru.darujo.dto.user.UserInfoTypeActiveDto;
import ru.darujo.model.User;
import ru.darujo.model.UserInfoType;
import ru.darujo.repository.UserInfoTypeRepository;
import ru.darujo.specifications.Specifications;


import java.util.Collection;
import java.util.List;

@Service
public class UserInfoTypeService {
    private UserInfoTypeRepository userInfoTypeRepository;

    @Autowired
    public void setUserInfoTypeRepository(UserInfoTypeRepository userInfoTypeRepository) {
        this.userInfoTypeRepository = userInfoTypeRepository;
    }

    @Transactional
    public void setUserInfoTypes(User user, Collection<UserInfoTypeActiveDto> userInfoTypeDto) {

        getInfoTypes(user.getId()).forEach(userInfoType ->
        {
            if (userInfoTypeDto
                    .stream()
                    .anyMatch(userInfoTypeActiveDto ->
                            !userInfoTypeActiveDto.getActive()
                                    && userInfoType.getCode().equals(userInfoTypeActiveDto.getCode()))){
                userInfoTypeRepository.delete(userInfoType);
            }
        });

        userInfoTypeDto.forEach(userInfoTypeActiveDto -> {
            if (userInfoTypeActiveDto.getActive()){
                UserInfoType userInfoType = userInfoTypeRepository.findFirstByCodeAndUser(userInfoTypeActiveDto.getCode(),user).orElse(null);
                if (userInfoType == null){
                    userInfoTypeRepository.save(new UserInfoType(userInfoTypeActiveDto.getCode(),
                            user));
                }
            }

        });

    }

    public List<UserInfoType> getInfoTypes(Long userId) {
        return userInfoTypeRepository.findAll(Specifications.eq(null,"user",userId));
    }
    public List<UserInfoType> getInfoTypes(MessageType messageType) {
        return userInfoTypeRepository.findAll(Specifications.eq(null,"code",messageType.toString()));
    }
}
