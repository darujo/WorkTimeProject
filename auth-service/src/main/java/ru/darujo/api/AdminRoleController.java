package ru.darujo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.darujo.convertor.RoleConvertor;
import ru.darujo.dto.user.RoleDto;
import ru.darujo.dto.user.RoleRightDto;
import ru.darujo.service.RoleService;


@RestController
@CrossOrigin
@RequestMapping("/admin/roles")
public class AdminRoleController {
    private RoleService roleService;

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/edit/role/{id}")
    public RoleDto getRoleEditDto(@PathVariable long id) {
       return RoleConvertor.getRoleDto(roleService.findById(id));
    }

    @PostMapping("/edit/role")
    public RoleDto setRoleEditDto(
            @RequestBody RoleDto roleDto) {
        return RoleConvertor.getRoleDto(
                roleService.saveRole(RoleConvertor.getRole(roleDto)));
    }

    @DeleteMapping("/edit/role/{id}")
    public void delRoleEditDto(@PathVariable long id) {
        roleService.deleteRole(id);
    }

    @GetMapping("/role/rights/{roleId}")
    public RoleRightDto getRoleRight(@PathVariable Long roleId) {
        return roleService.getRoleRight(roleId);

    }
    @PostMapping("/role/rights")
    public RoleRightDto getRoleRight(@RequestBody RoleRightDto userRoleDto) {
        return roleService.setRoleRight(userRoleDto);

    }
}
