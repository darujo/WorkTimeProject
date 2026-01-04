package ru.darujo.service;

import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.darujo.convertor.RoleConvertor;
import ru.darujo.dto.information.MapUserInfoDto;
import ru.darujo.dto.information.MessageType;
import ru.darujo.dto.user.*;
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.integration.InfoServiceIntegration;
import ru.darujo.model.Right;
import ru.darujo.model.User;
import ru.darujo.repository.UserRepository;
import ru.darujo.specifications.Specifications;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    @Getter
    private static UserService INSTANCE;

    public UserService() {
        INSTANCE = this;
    }

    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private LinkService linkService;

    @Autowired
    public void setLinkService(LinkService linkService) {
        this.linkService = linkService;
    }

    RoleService roleService;

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    private InfoServiceIntegration infoServiceIntegration;

    @Autowired
    public void setInfoServiceIntegration(InfoServiceIntegration infoServiceIntegration) {
        this.infoServiceIntegration = infoServiceIntegration;
    }

    private UserInfoTypeService userInfoTypeService;

    @Autowired
    public void setUserInfoTypeService(UserInfoTypeService userInfoTypeService) {
        this.userInfoTypeService = userInfoTypeService;
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Не найден пользователь c id " + id));
    }

    public Optional<User> findByNikName(String name) {
        return userRepository.findByNikNameIgnoreCase(name);
    }

    public void checkNull(String filed, String text) {
        if (filed == null || filed.isEmpty()) {
            throw new ResourceNotFoundRunTime("Не заполнено поле " + text);
        }
    }

    @Transactional
    public User saveUser(User user) {
        return saveUser(user, null);
    }

    @Transactional
    public User saveUser(User user, String textPassword) {
        checkNull(user.getNikName(), "логин");
        checkNull(user.getFirstName(), "имя");
        checkNull(user.getLastName(), "фамилия");
        if (user.getProjects().isEmpty()) {
            throw new ResourceNotFoundRunTime("У пользователя должен быть хотя бы один проект");
        }
        if (user.getId() != null) {
            if (userRepository.findByNikNameIgnoreCaseAndIdIsNot(user.getNikName(), user.getId()).isPresent()) {
                throw new ResourceNotFoundRunTime("Уже есть пользователь с таким ником");
            }
            User saveUser = userRepository.findById(user.getId()).orElseThrow(() -> new ResourceNotFoundRunTime("Пользователь с id " + user.getId() + " не найден"));
            user.setRights(saveUser.getRights());
            user.setRoles(saveUser.getRoles());
            user.setCurrentProject(saveUser.getCurrentProject());
//            user.setProjects(saveUser.getProjects());
            user.setTelegramId(saveUser.getTelegramId());
        } else {
            if (userRepository.findByNikNameIgnoreCase(user.getNikName()).isPresent()) {
                throw new ResourceNotFoundRunTime("Уже есть пользователь с таким ником");
            }
        }
        if (textPassword != null && !textPassword.isEmpty()) {
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                user.setPassword(hashPassword(textPassword));
            } else {
                if (!checkPassword(textPassword, user.getPassword())) {
                    throw new ResourceNotFoundRunTime("Пароль и хэш не совпадают");
                }
            }
        }
        return userRepository.save(user);
    }

    @Transactional
    public User loadUserByNikName(String nikName) throws UsernameNotFoundException {
        // todo вынести в настройки
        if (nikName.equals("system_user_update")) {
            User user = new User(-1L, nikName, hashPassword(
                    "Приносить пользу миру — это единственный способ стать счастливым."),

                    null, null, null, false, null);
            List<Right> right = new ArrayList<>();
            right.add(new Right(-1L, "STOP_SERVICE", "право на стоп"));
            user.setRights(right);
            return user;
        }
        return findByNikName(nikName).orElseThrow(() -> new UsernameNotFoundException("Не найден пользователь по логину " + nikName));
    }

    @Transactional
    public Page<@NonNull User> getUserList(String role,
                                           Integer page,
                                           Integer size,
                                           String nikName,
                                           String lastName,
                                           String firstName,
                                           String patronymic,
                                           Long telegramId,
                                           Boolean telegramIsNotNull) {
        Specification<@NonNull User> specification = getUserSpecification(role, nikName, lastName, firstName, patronymic, telegramId, telegramIsNotNull);
        Sort sort = Sort.by("lastName")
                .and(Sort.by("firstName"));
        Page<@NonNull User> userPage;
        if (page == null) {
            userPage = new PageImpl<>(userRepository.findAll(specification, sort));
        } else {
            if (size == null || size < 1) {
                size = 10;
            }
            userPage = userRepository.findAll(specification, PageRequest.of(page - 1, size, sort));
        }
        return userPage;
    }

    private Specification<@NonNull User> getUserSpecification(String role, String nikName, String lastName, String firstName, String patronymic, Long telegramId, Boolean telegramIsNotNull) {
        Specification<@NonNull User> specification = Specification.unrestricted();
        if (role != null && !role.isEmpty()) {
            specification = Specifications.in(specification, "nikName", roleService.findByName(role).orElseThrow(() -> new UsernameNotFoundException("Роль не найдена " + role))
                    .getUsers()
                    .stream().map(User::getNikName).collect(Collectors.toList()));
        }
        specification = Specifications.like(specification, "nikName", nikName);
        specification = Specifications.like(specification, "lastName", lastName);
        specification = Specifications.like(specification, "firstName", firstName);
        specification = Specifications.like(specification, "patronymic", patronymic);
        specification = Specifications.eq(specification, "telegramId", telegramId);
        specification = Specifications.isNotNull(specification, "telegramId", telegramIsNotNull);
        return specification;
    }

    @Transactional
    public UserRoleDto getUserRoles(Long userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundRunTime("Пользователь с id " + userId + " не найден"));
        Map<Long, UserRoleActiveDto> roleActiveDtoMap = new HashMap<>();
        roleService.getListRole().forEach(role -> roleActiveDtoMap.put(role.getId(), RoleConvertor.getUserRoleActiveDto(role, Boolean.FALSE)));
        user.getRoles().forEach(role -> roleActiveDtoMap.get(role.getId()).setActive(Boolean.TRUE));
        return new UserRoleDto(user.getId(), user.getNikName(), user.getFirstName(), user.getLastName(), user.getPatronymic(), roleActiveDtoMap.values());
    }

    @Transactional
    public UserRoleDto setUserRoles(UserRoleDto userRole) {
        User user = userRepository.findById(userRole.getId()).orElseThrow(() -> new ResourceNotFoundRunTime("Пользователь с id " + userRole.getId() + " не найден"));
        user.getRoles().clear();
        userRole.getRoles().forEach((roleDto) -> {
            if (roleDto.getActive()) {
                user.getRoles().add(RoleConvertor.getRole(roleDto));
            }
        });
        userRepository.save(user);
        return getUserRoles(user.getId());
    }

    public String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    public boolean checkPassword(String plainTextPassword, String hashPassword) {
        return BCrypt.checkpw(plainTextPassword, hashPassword);
    }

    @Transactional
    public boolean changePassword(String username, String passwordOld, String passwordNew) {
        User user = userRepository.findByNikNameIgnoreCase(username).orElseThrow(() -> new ResourceNotFoundRunTime("Пользователь не найден"));
        if (!checkPassword(passwordOld, user.getPassword())) {
            throw new ResourceNotFoundRunTime("Старый пароль не действителен");
        }

        if (passwordNew == null || passwordNew.isEmpty()) {
            throw new ResourceNotFoundRunTime("Новый пароль не должен быть пустым");
        }
        if (checkPassword(passwordNew, user.getPassword())) {
            throw new ResourceNotFoundRunTime("Новый пароль не должен совпадать со старым");
        }
        user.setPassword(hashPassword(passwordNew));
        user.setPasswordChange(false);
        user = saveUser(user);
        return user != null;
    }

    public MapUserInfoDto getUserMessageDTOs() {
        Map<MessageType, List<UserInfoDto>> messageTypeListMap = new HashMap<>();
        for (MessageType type : MessageType.values()) {
//            List<UserInfoDto> userDTOs = getUserList(null, null, null, null, null, null, null, null, true).getContent().stream().map(UserConvertor::getUserInfoDto).toList();
            List<UserInfoDto> userDTOs = userInfoTypeService
                    .getInfoTypes(type)
                    .stream()
                    .filter(userInfoType ->
                            userInfoType.getUser().getTelegramId() != null
                                    && userInfoType.getIsActive() != null
                                    && userInfoType.getIsActive())
                    .map(
                            userInfoType -> new UserInfoDto(
                                    userInfoType.getUser().getId(),
                                    userInfoType.getUser().getNikName(),
                                    userInfoType.getTelegramId() == null ? userInfoType.getUser().getTelegramId() : userInfoType.getTelegramId(),
                                    userInfoType.getThreadId(),
                                    null)).toList();
            messageTypeListMap.put(type, userDTOs);
        }
        return new MapUserInfoDto(messageTypeListMap);
    }

    public UserInfoTypeDto getUserInfoTypes(Long userId) {
        User user = null;
        if (userId != null) {
            user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundRunTime("Пользователь с id " + userId + " не найден"));
        }
        linkService.clearMapCode(null, null);
        Map<String, UserInfoTypeActiveDto> userInfoActiveDtoMap = new HashMap<>();
        for (MessageType type : MessageType.values()) {
            UserInfoTypeActiveDto userInfoTypeActiveDto = new UserInfoTypeActiveDto(type.toString(), type.getName(), false);
            if (user != null) {
                userInfoTypeActiveDto.setMessage(linkService.getCode(user.getNikName(), type.toString()));
            }
            userInfoActiveDtoMap.put(type.toString(), userInfoTypeActiveDto);

        }
        if (userId != null) {
            userInfoTypeService
                    .getInfoTypes(user)
                    .forEach(userInfoType -> {
                        var userInfo = userInfoActiveDtoMap.get(userInfoType.getCode());
                        userInfo.setActive(userInfoType.getIsActive());
                        userInfo.setTelegramId(userInfoType.getTelegramId());
                        userInfo.setThreadId(userInfoType.getThreadId());

                    });

            return new UserInfoTypeDto(user.getId(), user.getNikName(), user.getFirstName(), user.getLastName(), user.getPatronymic(), userInfoActiveDtoMap.values());
        } else {
            return new UserInfoTypeDto(null, null, null, null, null, userInfoActiveDtoMap.values());
        }

    }

    public UserInfoTypeDto setUserInfoTypes(UserInfoTypeDto userInfoTypeDto) {
        User user = userRepository.findById(userInfoTypeDto.getId()).orElseThrow(() -> new ResourceNotFoundRunTime("Пользователь не найден"));
        userInfoTypeService.setUserInfoTypes(user, userInfoTypeDto.getInfoTypes());
        setMessageTypeListMap();
        return getUserInfoTypes(user.getId());
    }

    public boolean setMessageTypeListMap() {
        try {
            infoServiceIntegration.setMessageTypeListMap(getUserMessageDTOs());
            return true;
        } catch (ResourceNotFoundRunTime ex) {
            log.error(ex.getMessage());
            return false;
        }
    }

    public boolean exists(Long chatId) {
        return userRepository.exists(getUserSpecification(null, null, null, null, null, chatId, null));
    }


}
