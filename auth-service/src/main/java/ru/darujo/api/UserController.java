package ru.darujo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import ru.darujo.convertor.UserConvertor;

import ru.darujo.dto.user.UserDto;
import ru.darujo.service.UserService;


@RestController
@CrossOrigin
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserDto(@RequestParam(required = false) String nikName) {
        try {
            return ResponseEntity.ok(UserConvertor.getUserDto(userService.loadUserByNikName(nikName)));

        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex);
        }
    }

    @GetMapping("")
    public Page<UserDto> getUserList(@RequestParam(required = false) String role,
                                     @RequestParam(required = false) Integer page,
                                     @RequestParam(required = false) Integer size,
                                     @RequestParam(required = false) String nikName,
                                     @RequestParam(required = false) String lastName,
                                     @RequestParam(required = false) String firstName,
                                     @RequestParam(required = false) String patronymic) {
        return  userService.getUserList(role, page, size, nikName, lastName, firstName, patronymic).map(UserConvertor::getUserDto);


    }

}
