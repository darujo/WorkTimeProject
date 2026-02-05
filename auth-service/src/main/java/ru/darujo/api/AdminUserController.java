package ru.darujo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import ru.darujo.convertor.UserConvertor;
import ru.darujo.dto.ratestage.AttrDto;
import ru.darujo.dto.user.UserEditDto;
import ru.darujo.dto.user.UserRoleDto;
import ru.darujo.service.UserService;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/admin/users")
public class AdminUserController {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/edit/user/{id}")
    public ResponseEntity<?> getUserEditDto(@PathVariable long id) {
        try {
            return ResponseEntity.ok(UserConvertor.getUserEditDto(userService.findById(id)));

        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex);
        }
    }

    @PostMapping("/edit/user")
    public UserEditDto setUserEditDto(@RequestBody UserEditDto user,
                                      @RequestParam("system_right") List<String> right) {
        return UserConvertor.getUserEditDto(
                userService.saveUser(
                        UserConvertor.getUser(user),
                        user.getTextPassword(),
                        right.contains("ADMIN_USER") && user.isAdmin() != null ? user.isAdmin() : false));
    }

    @GetMapping("/user/roles/{userId}")
    public UserRoleDto getUserRoles(@PathVariable Long userId,
                                    @RequestParam("system_project") Long project) {
        return userService.getUserRoles(userId, project);

    }
    @PostMapping("/user/roles")
    public UserRoleDto getUserRoles(@RequestParam("system_project") Long project,
                                    @RequestBody UserRoleDto userRoleDto) {
        return userService.setUserRoles(project, userRoleDto);

    }
    @GetMapping("/user/password/hash")
    public AttrDto<?> getPasswordHash(@RequestParam String textPassword) {
        return new AttrDto<>("passwordHash", userService.hashPassword(textPassword));

    }
    @GetMapping("/user/password/check")
    public Boolean getPasswordCheck(@RequestParam String passwordText,
                                    @RequestParam String passwordHash) {
        return userService.checkPassword(passwordText,passwordHash);


    }
}
