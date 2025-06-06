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
import ru.darujo.dto.user.UserRoleActiveDto;
import ru.darujo.dto.user.UserRoleDto;
import ru.darujo.exceptions.ResourceNotFoundException;
import ru.darujo.model.User;
import ru.darujo.repository.UserRepository;
import ru.darujo.repository.specification.UserSpecifications;

import javax.transaction.Transactional;
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

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Не наден пользователь c id " + id));
    }

    public Optional<User> findByNikName(String name) {
        return userRepository.findByNikNameIgnoreCase(name);
    }

    public void checkNull(String filed, String text) {
        if (filed == null || filed.isEmpty()) {
            throw new ResourceNotFoundException("Не заполнено поле " + text);
        }
    }

    public User saveUser(User user, String textPassword) {
        checkNull(user.getNikName(), "логин");
        checkNull(user.getFirstName(), "имя");
        checkNull(user.getLastName(), "фамилия");

        if (user.getId() != null) {
            if (userRepository.findByNikNameIgnoreCaseAndIdIsNot(user.getNikName(), user.getId()).isPresent()) {
                throw new ResourceNotFoundException("Уже есть пользователь с таким ником");
            }
            User saveUser = userRepository.findById(user.getId()).orElseThrow(() -> new ResourceNotFoundException("Пользователь с id " + user.getId() + " не найден"));
            user.setRights(saveUser.getRights());
            user.setRoles(saveUser.getRoles());
        } else {
            if (userRepository.findByNikNameIgnoreCase(user.getNikName()).isPresent()) {
                throw new ResourceNotFoundException("Уже есть пользователь с таким ником");
            }
        }
        if(textPassword != null && !textPassword.equals("")){
           if (user.getPassword() == null || user.getPassword().isEmpty()){
               user.setPassword(hashPassword(textPassword));
           } else {
               if (!checkPassword(textPassword,user.getPassword())){
                   throw new ResourceNotFoundException("Пароль и хаш не совпадают");
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
                                  String patronymic) {
        Specification<User> specification = Specification.where(null);
        if (role != null && !role.isEmpty()) {
            specification = UserSpecifications.in(specification, "nikName", roleService.findByName(role).orElseThrow(() -> new UsernameNotFoundException("Роль не найдена " + role))
                    .getUsers()
                    .stream().map(User::getNikName).collect(Collectors.toList()));
        }
        specification = UserSpecifications.like(specification, "nikName", nikName);
        specification = UserSpecifications.like(specification, "lastName", lastName);
        specification = UserSpecifications.like(specification, "firstName", firstName);
        specification = UserSpecifications.like(specification, "patronymic", patronymic);
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

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Пользователь с id " + userId + " не найден"));
        Map<Long, UserRoleActiveDto> roleActiveDtoMap = new HashMap<>();
        roleService.getListRole().forEach(role -> roleActiveDtoMap.put(role.getId(), RoleConvertor.getUserRoleActiveDto(role, Boolean.FALSE)));
        user.getRoles().forEach(role -> roleActiveDtoMap.get(role.getId()).setActive(Boolean.TRUE));
        return new UserRoleDto(user.getId(), user.getNikName(), user.getFirstName(), user.getLastName(), user.getPatronymic(), roleActiveDtoMap.values());
    }

    @Transactional
    public UserRoleDto setUserRoles(UserRoleDto userRole) {
        User user = userRepository.findById(userRole.getId()).orElseThrow(() -> new ResourceNotFoundException("Пользователь с id " + userRole.getId() + " не найден"));
        user.getRoles().clear();
        userRole.getRoles().forEach((roleDto) -> {
            if (roleDto.getActive()) {
                user.getRoles().add(RoleConvertor.getRole(roleDto));
            }
        });
        userRepository.save(user);
        return getUserRoles(user.getId());
    }

    public String hashPassword(String plainTextPassword){
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }
    public boolean checkPassword(String plainTextPassword,String hashPassword){
        return BCrypt.checkpw(plainTextPassword, hashPassword);
    }
}
