package ru.darujo.service;

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
import ru.darujo.convertor.UserConvertor;
import ru.darujo.dto.information.CodeTelegramMes;
import ru.darujo.dto.information.MapUserInfoDto;
import ru.darujo.dto.information.MessageType;
import ru.darujo.dto.information.ResultMes;
import ru.darujo.dto.user.UserRoleActiveDto;
import ru.darujo.dto.user.UserRoleDto;
import ru.darujo.dto.user.UserInfoDto;
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.integration.InfoServiceIntegration;

import ru.darujo.model.User;
import ru.darujo.repository.UserRepository;
import ru.darujo.specifications.Specifications;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
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

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Не наден пользователь c id " + id));
    }

    public Optional<User> findByNikName(String name) {
        return userRepository.findByNikNameIgnoreCase(name);
    }

    public void checkNull(String filed, String text) {
        if (filed == null || filed.isEmpty()) {
            throw new ResourceNotFoundRunTime("Не заполнено поле " + text);
        }
    }

    public User saveUser(User user) {
        return saveUser(user, null);
    }

    @Transactional
    public User saveUser(User user, String textPassword) {
        checkNull(user.getNikName(), "логин");
        checkNull(user.getFirstName(), "имя");
        checkNull(user.getLastName(), "фамилия");

        if (user.getId() != null) {
            if (userRepository.findByNikNameIgnoreCaseAndIdIsNot(user.getNikName(), user.getId()).isPresent()) {
                throw new ResourceNotFoundRunTime("Уже есть пользователь с таким ником");
            }
            User saveUser = userRepository.findById(user.getId()).orElseThrow(() -> new ResourceNotFoundRunTime("Пользователь с id " + user.getId() + " не найден"));
            user.setRights(saveUser.getRights());
            user.setRoles(saveUser.getRoles());
            user.setTelegramId(saveUser.getTelegramId());
        } else {
            if (userRepository.findByNikNameIgnoreCase(user.getNikName()).isPresent()) {
                throw new ResourceNotFoundRunTime("Уже есть пользователь с таким ником");
            }
        }
        if (textPassword != null && !textPassword.equals("")) {
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
        return findByNikName(nikName).orElseThrow(() -> new UsernameNotFoundException("Не наден пользователь по логину " + nikName));
    }

    @Transactional
    public Page<User> getUserList(String role,
                                  Integer page,
                                  Integer size,
                                  String nikName,
                                  String lastName,
                                  String firstName,
                                  String patronymic,
                                  Long telegramId,
                                  Boolean telegramIsNotNull) {
        Specification<User> specification = Specification.where(null);
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
        Sort sort = Sort.by("lastName")
                .and(Sort.by("firstName"));
        Page<User> userPage;
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

    public boolean changePassword(String username, String passwordOld, String passwordNew) {
        User user = userRepository.findByNikNameIgnoreCase(username).orElseThrow(() -> new ResourceNotFoundRunTime("Пользователь не найден"));
        if (!checkPassword(passwordOld, user.getPassword())) {
            throw new ResourceNotFoundRunTime("Старый пароль не действителен");
        }

        if (passwordNew == null || passwordNew.equals("")) {
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
            List<UserInfoDto> userDTOs = getUserList(null, null, null, null, null, null, null, null,true).getContent().stream().map(UserConvertor::getUserInfoDto).toList();
            messageTypeListMap.put(type, userDTOs);
        }
        return new MapUserInfoDto(messageTypeListMap);
    }

    private class SingleCode {
        private final String login;
        private final Timestamp timestamp;

        public SingleCode(String login, Timestamp timestamp) {
            this.login = login;
            this.timestamp = timestamp;
        }

    }

    private final Map<Integer, SingleCode> mapCode = new HashMap<>();
    private final Integer TIME_CODE = 10;

    public CodeTelegramMes getGenSingleCode(String login) {
        if (login == null) {
            throw new ResourceNotFoundRunTime("пройдите авторизацию");
        }
        findByNikName(login).orElseThrow(() -> new ResourceNotFoundRunTime("Нет пользователя с логином"));
        clearMapCode(login);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis() + TIME_CODE * 60 * 1000);
        int code = (int) ((99999999 * Math.random()));
        SingleCode singleCode = new SingleCode(login, timestamp);
        mapCode.put(code, singleCode);
        return new CodeTelegramMes(true,"t.me/DaruWorkBot", code, TIME_CODE);

    }

    public void clearMapCode(String login) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis() + TIME_CODE * 60 * 1000);
        for (Map.Entry<Integer, SingleCode> entry : mapCode.entrySet()) {
            if (entry.getValue().timestamp.after(timestamp) || (entry.getValue().login.equals(login))) {
                mapCode.remove(entry.getKey());
            }
        }
    }

    public ResultMes linkCodeTelegram(Integer code, Long idTelegram) {
        clearMapCode(null);
        SingleCode singleCode = mapCode.get(code);
        if (singleCode == null) {
            return new ResultMes(false, "Не такого кода авторизации или он просрочен ");
        }

        User user = findByNikName(singleCode.login).orElse(null);
        if (user == null) {
            return new ResultMes(false, "Пользовательне найден.");
        }
        user.setTelegramId(idTelegram);
        saveUser(user);
        mapCode.remove(code);
        try {
            infoServiceIntegration.setMessageTypeListMap(getUserMessageDTOs());
            return new ResultMes(true, "");
        } catch (ResourceNotFoundRunTime ex) {
            return new ResultMes(false, "Пользователь добавлен, но что-то не так и уведомления будут приходить, после перезапуска сервиса уведомлений, Обратитесь к администратору или ждите");
        }
    }

    @Transactional
    public void linkDeleteTelegram(Long telegramId) {
        getUserList(null, null, null, null, null, null, null, telegramId, null)
                .forEach(user -> {
                    user.setTelegramId(null);
                    saveUser(user);
                });
    }

}
