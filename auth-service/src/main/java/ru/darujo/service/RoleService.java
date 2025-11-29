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
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.model.Role;
import ru.darujo.repository.RoleRepository;
import ru.darujo.specifications.Specifications;

import java.util.*;

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

    public Optional<Role> findByName(String role) {
        return roleRepository.findByNameIgnoreCase(role);
    }

    public Iterable<Role> getListRole(){
        return  roleRepository.findAll();
    }

    public Role findById(long id) {
        return roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundRunTime("Роль не найдена"));
    }
    public void checkNull(String filed, String text) {
        if (filed == null || filed.isEmpty()) {
            throw new ResourceNotFoundRunTime("Не заполнено поле " + text);
        }
    }

    public Role saveRole(Role role) {
        checkNull(role.getName(), "код");
        checkNull(role.getLabel(), "Наименование");

        if (role.getId() != null) {
            if (roleRepository.findByNameIgnoreCaseAndIdIsNot(role.getName(), role.getId()).isPresent()) {
                throw new ResourceNotFoundRunTime("Уже есть группа с таким кодом");
            }
            Role saveRole = roleRepository.findById(role.getId()).orElseThrow(() -> new ResourceNotFoundRunTime("Группа с id " + role.getId() + " не найден"));
            role.setUsers(saveRole.getUsers());
            role.setRights(saveRole.getRights());
        } else {
            if (roleRepository.findByNameIgnoreCase(role.getName()).isPresent()) {
                throw new ResourceNotFoundRunTime("Уже есть группа с таким кодом");
            }
        }
        return roleRepository.save(role);
    }

    @Transactional
    public RoleRightDto getRoleRight(Long roleId) {

        Role rights = roleRepository.findById(roleId).orElseThrow(() -> new ResourceNotFoundRunTime("Группа с id " + roleId + " не найден"));
        Map<Long, RoleRightActiveDto> rightActiveDtoHashMap = new HashMap<>();
        rightService.getListRight().forEach(right -> rightActiveDtoHashMap.put(right.getId(), RightConvertor.getRoleRightActiveDto(right,Boolean.FALSE)));
        rights.getRights().forEach(right -> rightActiveDtoHashMap.get(right.getId()).setActive(Boolean.TRUE));
        return new RoleRightDto(rights.getId(), rights.getName(), rights.getLabel(), rightActiveDtoHashMap.values());
    }
    @Transactional
    public RoleRightDto setRoleRight(RoleRightDto roleRightDto) {
        Role role = roleRepository.findById(roleRightDto.getId()).orElseThrow(() -> new ResourceNotFoundRunTime("Группа с id " + roleRightDto.getId() + " не найден"));
        role.getRights().clear();
        roleRightDto.getRights().forEach((rightActiveDto) -> {
            if(rightActiveDto.getActive()){
                role.getRights().add(RightConvertor.getRight(rightActiveDto));
            }
        });
        roleRepository.save(role);
        return getRoleRight(role.getId());
    }

    public Collection<Role> getRoleList(String code, String name) {
        Specification<@NonNull Role> specification = Specification.unrestricted();
        specification = Specifications.like(specification,"code",code);
        specification = Specifications.like(specification,"name",name);
        return new ArrayList<>(roleRepository.findAll(specification, Sort.by("name")));
    }

    @Transactional
    public void deleteRole(long id) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundRunTime("Группа не найдена"));
        roleRepository.delete(role);
    }
}
