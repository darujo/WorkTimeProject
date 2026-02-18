package ru.darujo.service;

import jakarta.transaction.Transactional;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.darujo.convertor.RightConvertor;
import ru.darujo.dto.user.RoleRightActiveDto;
import ru.darujo.dto.user.RoleRightDto;
import ru.darujo.dto.user.UserRoleDto;
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.model.Project;
import ru.darujo.model.Role;
import ru.darujo.model.User;
import ru.darujo.repository.RoleRepository;
import ru.darujo.specifications.Specifications;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RoleService {

    private RoleRepository roleRepository;

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    private RightService rightService;

    @Autowired
    public void setRightService(RightService rightService) {
        this.rightService = rightService;
    }

    public Optional<Role> findByName(Long projectId, String role) {
        return findByName(ProjectService.getInstance().findById(projectId), role);
    }

    public Optional<Role> findByName(Project project, String role) {
        return roleRepository.findByNameIgnoreCaseAndProject(role, project);
    }

    public List<Role> getListRole(Project project) {
        Specification<Role> specification = Specification.unrestricted();
        specification = Specifications.eq(specification, "project", project);
        return roleRepository.findAll(specification);
    }

    public Role findById(long id) {
        return roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundRunTime("Роль не найдена"));
    }

    public void checkNull(String filed, String text) {
        if (filed == null || filed.isEmpty()) {
            throw new ResourceNotFoundRunTime("Не заполнено поле " + text);
        }
    }

    public Role saveRole(Long projectId, Role role) {
        Project project = ProjectService.getInstance().findById(projectId);
        if (role.getProject() == null) {
            role.setProject(project);
        }
        checkNull(role.getName(), "код");
        checkNull(role.getLabel(), "Наименование");

        if (role.getId() != null) {
            if (roleRepository.findByNameIgnoreCaseAndIdIsNotAndProject(role.getName(), role.getId(), project).isPresent()) {
                throw new ResourceNotFoundRunTime("Уже есть группа с таким кодом");
            }
            Role saveRole = roleRepository.findById(role.getId()).orElseThrow(() -> new ResourceNotFoundRunTime("Группа с id " + role.getId() + " не найден"));
            if (!saveRole.getProject().equals(role.getProject())) {
                throw new ResourceNotFoundRunTime("Нельзя переносить роль в другой проект");
            }
            role.setUsers(saveRole.getUsers());
            role.setRights(saveRole.getRights());
        } else {
            if (roleRepository.findByNameIgnoreCaseAndProject(role.getName(), project).isPresent()) {
                throw new ResourceNotFoundRunTime("Уже есть группа с таким кодом");
            }
        }
        return roleRepository.save(role);
    }

    @Transactional
    public RoleRightDto getRoleRight(Long roleId) {

        Role role = roleRepository.findById(roleId).orElseThrow(() -> new ResourceNotFoundRunTime("Группа с id " + roleId + " не найден"));
        Map<Long, RoleRightActiveDto> rightActiveDtoHashMap = new HashMap<>();
        rightService
                .getListRight()
                .forEach(right -> {
                    if (!right.getName().startsWith("ADMIN_")) {
                        rightActiveDtoHashMap.put(right.getId(), RightConvertor.getRoleRightActiveDto(right, Boolean.FALSE));
                    }
                });
        role.getRights().forEach(right -> rightActiveDtoHashMap.get(right.getId()).setActive(Boolean.TRUE));
        return new RoleRightDto(role.getId(), role.getName(), role.getLabel(), rightActiveDtoHashMap.values().stream().toList());
    }

    @Transactional
    public RoleRightDto setRoleRight(RoleRightDto roleRightDto) {
        Role role = roleRepository.findById(roleRightDto.getId()).orElseThrow(() -> new ResourceNotFoundRunTime("Группа с id " + roleRightDto.getId() + " не найден"));
        role.getRights().clear();
        roleRightDto.getRights().forEach((rightActiveDto) -> {
            if (rightActiveDto.getActive()) {
                role.getRights().add(RightConvertor.getRight(rightActiveDto));
            }
        });
        roleRepository.save(role);
        return getRoleRight(role.getId());
    }

    public List<Role> getRoleList(String code, String name, Long projectId, User user) {
        Specification<@NonNull Role> specification = Specification.unrestricted();
        specification = Specifications.like(specification, "code", code);
        specification = Specifications.like(specification, "name", name);
        specification = Specifications.eq(specification, "project", ProjectService.getInstance().findById(projectId));
        List<Role> roles = roleRepository.findAll(specification, Sort.by("name"));
        if (user != null) {
            roles = roles.stream().filter(role -> role.getUsers() != null && role.getUsers().contains(user)).toList();
        }
        return roles;
    }

    @Transactional
    public void deleteRole(long id) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundRunTime("Группа не найдена"));
        roleRepository.delete(role);
    }

    @Transactional
    public void setUserRole(User user, UserRoleDto userRole) {
//        user.getRoles().removeIf(role -> role.getProject().getId().equals(projectId));
        userRole.getRoles().forEach((roleDto) -> {
            Role role = findById(roleDto.getId());
            if (roleDto.getActive()) {
                if (!role.getUsers().contains(user)) {
                    role.getUsers().add(user);
                    roleRepository.save(role);
                }
            } else {
                if (role.getUsers().contains(user)) {
                    role.getUsers().remove(user);
                    roleRepository.save(role);
                }
            }
        });
//        userRepository.save(user);
    }
}
