package ru.darujo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.darujo.convertor.RoleConvertor;
import ru.darujo.dto.user.RoleDto;
import ru.darujo.service.RoleService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/roles")
public class RoleController {
    private RoleService roleService;

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("")
    public List<RoleDto> getRoleList(@RequestParam(required = false) String code,
                                     @RequestParam(required = false) String name,
                                     @RequestParam("system_project") Long projectId) {
        return roleService.getRoleList(code, name, projectId).stream().map(RoleConvertor::getRoleDto).collect(Collectors.toList());
    }

}
