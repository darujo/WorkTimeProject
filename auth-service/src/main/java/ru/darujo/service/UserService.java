package ru.darujo.service;

import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.darujo.convertor.RoleConvertor;
import ru.darujo.dto.information.MapUserInfoDto;
import ru.darujo.dto.information.MessageInfoDto;
import ru.darujo.dto.user.*;
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.hash.HashService;
import ru.darujo.integration.InfoServiceIntegration;
import ru.darujo.model.Project;
import ru.darujo.model.Right;
import ru.darujo.model.User;
import ru.darujo.repository.UserRepository;
import ru.darujo.specifications.Specifications;
import ru.darujo.type.MessageSenderType;
import ru.darujo.type.MessageType;
import ru.darujo.url.UrlWorkTime;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    @Value("${mail.secret}")
    private String codePas;
    @Getter
    private static UserService INSTANCE;
    private ProjectService projectService;

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

    private RightService rightService;

    @Autowired
    public void setRightService(RightService rightService) {
        this.rightService = rightService;
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
        return saveUser(user, null, null);
    }

    @Transactional
    public User saveUser(User user, String textPassword, Boolean isAdmin) {
        boolean newEmail = false;
        checkNull(user.getNikName(), "логин");
        checkNull(user.getFirstName(), "имя");
        checkNull(user.getLastName(), "фамилия");
        if (user.getProjects() == null || user.getProjects().isEmpty()) {
            throw new ResourceNotFoundRunTime("У пользователя должен быть хотя бы один проект");
        }
        if (user.getCurrentProject() == null
                || !user.getProjects().contains(user.getCurrentProject())) {
            user.setCurrentProject(user.getProjects().get(0));
        }

        if (user.getId() != null) {
            if (userRepository.findByNikNameIgnoreCaseAndIdIsNot(user.getNikName(), user.getId()).isPresent()) {
                throw new ResourceNotFoundRunTime("Уже есть пользователь с таким ником");
            }
            User finalUser = user;
            User saveUser = userRepository.findById(user.getId()).orElseThrow(() -> new ResourceNotFoundRunTime("Пользователь с id " + finalUser.getId() + " не найден"));
            user.setRights(saveUser.getRights());
//            user.setRoles(saveUser.getRoles());
            if (user.getCurrentProject() == null) {
                user.setCurrentProject(saveUser.getCurrentProject());
            }
//            user.setProjects(saveUser.getProjects());
            user.setTelegramId(saveUser.getTelegramId());
            user.setEmail(saveUser.getEmail());
            if (user.getNewEmail() != null && !user.getNewEmail().equals(saveUser.getNewEmail())) {
                setNewEmailCode(user);
                newEmail = true;
            } else {
                user.setCodeEmail(saveUser.getCodeEmail());
                user.setSendCode(saveUser.getSendCode());
                user.setRecovery(saveUser.getRecovery());
            }
        } else {
            if (userRepository.findByNikNameIgnoreCase(user.getNikName()).isPresent()) {
                throw new ResourceNotFoundRunTime("Уже есть пользователь с таким ником");
            }
            setNewEmailCode(user);
            newEmail = true;
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
        if (isAdmin != null) {
            Right right = rightService.getRight("ADMIN_USER");
            if (isAdmin) {
                if (user.getRights() == null) {
                    List<Right> rights = new ArrayList<>();
                    rights.add(right);
                    user.setRights(rights);
                }
                if (!user.getRights().contains(right)) {
                    user.getRights().add(right);
                }
            } else {
                if (user.getRights() != null) {
                    user.getRights().remove(right);
                }
            }
        }
        user = userRepository.save(user);
        if (newEmail) {
            infoServiceIntegration.addMessage(new MessageInfoDto(new UserInfoDto(MessageSenderType.Email.toString(), user.getId(), user.getNikName(), user.getNewEmail(), null, null), "Подтверждение почты", "Для подтверждения почты перейдите по ссылке " + UrlWorkTime.getUrlNewEmail(user.getNikName(), getHash(user))));
        }
        return user;
    }

    @Transactional
    public User loadUserByNikName(String nikName) throws UsernameNotFoundException {
        // todo вынести в настройки
        if (nikName.equals("system_user_update")) {
            User user = new User(-1L, nikName, hashPassword(
                    "Приносить пользу миру — это единственный способ стать счастливым."),

                    null, null, null, false, null, false, null, null, null, null, null);
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
                                           String telegramId,
                                           Boolean telegramIsNotNull,
                                           Long projectId) {
        Specification<@NonNull User> specification = getUserSpecification(role, nikName, lastName, firstName, patronymic, telegramId, telegramIsNotNull, projectId);
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

    private Specification<@NonNull User> getUserSpecification(String role, String nikName, String lastName, String
            firstName, String patronymic, String telegramId, Boolean telegramIsNotNull, Long projectId) {
        Specification<@NonNull User> specification = Specification.unrestricted();
        if (role != null && !role.isEmpty()) {
            specification = Specifications.in(specification, "r", roleService.findByName(projectId, role).orElseThrow(() -> new UsernameNotFoundException("Роль не найдена " + role))
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
    public UserRoleDto getUserRoles(Long userId, Long projectId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundRunTime("Пользователь с id " + userId + " не найден"));
        Map<Long, UserRoleActiveDto> roleActiveDtoMap = new HashMap<>();
        Project project = projectService.findById(projectId);
        roleService.getListRole(project).forEach(role -> roleActiveDtoMap.put(role.getId(), RoleConvertor.getUserRoleActiveDto(role, Boolean.FALSE)));

        roleService.getRoleList(null, null, projectId, user).forEach(role -> {
            UserRoleActiveDto userRoleActiveDto = roleActiveDtoMap.get(role.getId());
            if (userRoleActiveDto != null) {
                userRoleActiveDto.setActive(Boolean.TRUE);
            }
        });

        return new UserRoleDto(user.getId(), user.getNikName(), user.getFirstName(), user.getLastName(), user.getPatronymic(), roleActiveDtoMap.values());
    }

    @Transactional
    public UserRoleDto setUserRoles(Long projectId, UserRoleDto userRole) {
        User user = userRepository.findById(userRole.getId()).orElseThrow(() -> new ResourceNotFoundRunTime("Пользователь с id " + userRole.getId() + " не найден"));
        roleService.setUserRole(user, userRole);

        return getUserRoles(user.getId(), projectId);
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
        Map<MessageType, Map<MessageSenderType, List<UserInfoDto>>> messageTypeListMap = new HashMap<>();
        for (MessageType type : MessageType.values()) {
            Map<MessageSenderType, List<UserInfoDto>> senderTypeListMap = new HashMap<>();
            messageTypeListMap.put(type, senderTypeListMap);

            userInfoTypeService
                    .getInfoTypes(type)
                    .stream()
                    .filter(userInfoType ->
                            userInfoType.getIsActive() != null
                                    && userInfoType.getIsActive())
                    .forEach(
                            userInfoType -> {
                                MessageSenderType senderType = MessageSenderType.valueOf(userInfoType.getSenderType());
                                if (getMainChatId(senderType, userInfoType.getUser()) != null) {
                                    List<UserInfoDto> userInfoDTOs = senderTypeListMap.computeIfAbsent(senderType, k -> new ArrayList<>());
                                    userInfoDTOs.add(new UserInfoDto(
                                            userInfoType.getSenderType(),
                                            userInfoType.getUser().getId(),
                                            userInfoType.getUser().getNikName(),
                                            userInfoType.getProjectId(),
                                            userInfoType.getChatId() == null ? getMainChatId(senderType, userInfoType.getUser()) : userInfoType.getChatId(),
                                            userInfoType.getThreadId(),
                                            null));
                                }
                            }
                    );


        }
        return new MapUserInfoDto(messageTypeListMap);
    }

    private static String getMainChatId(MessageSenderType senderType, User user) {
        if (senderType.equals(MessageSenderType.Telegram)) {
            return user.getTelegramId();
        } else if (senderType.equals(MessageSenderType.Email)) {
            return user.getEmail();
        } else {
            return null;
        }

    }

    public UserInfoTypeDto getUserInfoTypes(Long userId, String senderType) {
        User user;
        if (userId != null) {
            user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundRunTime("Пользователь с id " + userId + " не найден"));
        } else {
            user = null;
        }
        linkService.clearMapCode(null, null);
        Map<String, UserInfoTypeActiveDto> userInfoActiveDtoMap = new HashMap<>();
        for (MessageType type : MessageType.values()) {
            UserInfoTypeActiveDto userInfoTypeActiveDto = new UserInfoTypeActiveDto(null, type.toString(), type.getName(), false);
            if (user != null) {
                userInfoTypeActiveDto.setMessage(linkService.getCode(user.getNikName(), type.toString()));
            }
            userInfoActiveDtoMap.put(type.toString(), userInfoTypeActiveDto);

        }
        if (userId != null) {
            Map<String, UserInfoTypeActiveDto> userInfoActiveCurrentProjectDtoMap = new HashMap<>();
            for (MessageType type : MessageType.values()) {
                if (type.isProject()) {
                    UserInfoTypeActiveDto userInfoTypeActiveDto = new UserInfoTypeActiveDto(user.getCurrentProject().getId(), type.toString(), type.getName(), false);

                    userInfoTypeActiveDto.setMessage(linkService.getCode(user.getNikName(), type.toString()));

                    userInfoActiveCurrentProjectDtoMap.put(type.toString(), userInfoTypeActiveDto);
                }
            }
            userInfoTypeService
                    .getInfoTypes(user, senderType)
                    .stream().filter(userInfoType -> userInfoType.getProjectId() == null || userInfoType.getProjectId().equals(user.getCurrentProject().getId()))
                    .forEach(userInfoType -> {
                        UserInfoTypeActiveDto userInfo;
                        if (userInfoType.getProjectId() == null) {
                            userInfo = userInfoActiveDtoMap.get(userInfoType.getCode());
                        } else {
                            userInfo = userInfoActiveCurrentProjectDtoMap.get(userInfoType.getCode());
                        }
                        userInfo.setActive(userInfoType.getIsActive());
                        userInfo.setTelegramId(userInfoType.getChatId());
                        userInfo.setThreadId(userInfoType.getThreadId());
                    });

            return new UserInfoTypeDto(user.getId(), user.getNikName(), user.getFirstName(), user.getLastName(), user.getPatronymic(), userInfoActiveDtoMap.values(), userInfoActiveCurrentProjectDtoMap.values());
        } else {
            return new UserInfoTypeDto(null, null, null, null, null, userInfoActiveDtoMap.values(), null);
        }

    }

    public UserInfoTypeDto setUserInfoTypes(String senderType, UserInfoTypeDto userInfoTypeDto) {
        User user = userRepository.findById(userInfoTypeDto.getId()).orElseThrow(() -> new ResourceNotFoundRunTime("Пользователь не найден"));
        userInfoTypeService.setUserInfoTypes(user, senderType, userInfoTypeDto.getInfoTypes(), userInfoTypeDto.getInfoProjectTypes());
        setMessageTypeListMap();
        return getUserInfoTypes(user.getId(), senderType);
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

    public boolean exists(String chatId) {
        return userRepository.exists(getUserSpecification(null, null, null, null, null, chatId, null, null));
    }

    private static void setNewEmailCode(User user) {
        int code = (int) ((99999999 * Math.random()));
        user.setCodeEmail(Integer.toString(code));
        user.setSendCode(new Timestamp(System.currentTimeMillis()));
//        user.setEmail(user.getNewEmail());
        user.setRecovery(false);
    }

    @Transactional
    public boolean confirmEmail(String nikName, String code) {
        User user = loadUserByNikName(nikName);
        if (code.equals(getHash(user))) {
            user.setCodeEmail(null);
            user.setEmail(user.getNewEmail());
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Transactional
    public void getRestorePassword(String nikName, String email) {
        User user = loadUserByNikName(nikName);
        if (email != null && email.equals(user.getEmail())) {
            setNewEmailCode(user);
            userRepository.save(user);
            infoServiceIntegration.addMessage(new MessageInfoDto(new UserInfoDto(MessageSenderType.Email.toString(), user.getId(), user.getNikName(), user.getEmail(), null, null), "Восстановление доступа", "Для восстановления пароля перейдите по ссылке " + UrlWorkTime.getUrlRecovery(user.getNikName(), getHash(user))));

        } else {
            throw new ResourceNotFoundRunTime("Почта не совпадает");
        }

    }

    @Transactional
    public boolean restorePassword(String nikName, String code) {
        User user = loadUserByNikName(nikName);
        if (code.equals(getHash(user))) {
            user.setCodeEmail(null);
            user.setPasswordChange(true);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public String getHash(User user) {
        return HashService.getSHA256(user.getCodeEmail() + ":" + codePas);
    }


    @Autowired
    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }

    public List<MessageSenderType> getUserSenderTypes(Long userId) {
        List<MessageSenderType> senderTypes = new ArrayList<>();
        User user = findById(userId);
        for (MessageSenderType value : MessageSenderType.values()) {
            if (getMainChatId(value, user) != null) {
                senderTypes.add(value);
            }
        }
        return senderTypes;
    }
}
