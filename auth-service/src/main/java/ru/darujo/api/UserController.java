package ru.darujo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import ru.darujo.convertor.UserConvertor;

import ru.darujo.dto.ResultMes;
import ru.darujo.dto.user.UserDto;
import ru.darujo.dto.user.UserPasswordChangeDto;
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
    public Page<UserDto> getUserList(@RequestParam(required = false) Integer page,
                                     @RequestParam(required = false) Integer size,
                                     @RequestParam(required = false) String nikName,
                                     @RequestParam(required = false) String lastName,
                                     @RequestParam(required = false) String firstName,
                                     @RequestParam(required = false) String patronymic) {
        String role = null;
        if (nikName != null) {
            if (nikName.equals("All")) {
                nikName = null;
            } else if (nikName.length() > 5 && nikName.substring(0, 5).equalsIgnoreCase("role_")) {
                role = nikName.substring(5);
                nikName = null;
            }
        }
        return userService.getUserList(role, page, size, nikName, lastName, firstName, patronymic, null).map(UserConvertor::getUserDto);


    }

    @PostMapping("/user/password/change")
    public boolean changePassword(@RequestBody UserPasswordChangeDto userPasswordChangeDto,
                                  @RequestHeader String username) {
        return userService.changePassword(username, userPasswordChangeDto.getPasswordOld(), userPasswordChangeDto.getPasswordNew());

    }

    @GetMapping("/user/telegram/get")
    public String getGenSingleCode(@RequestHeader String username) {
        return userService.getGenSingleCode(username);
    }

    @GetMapping("/user/telegram/link")
    public ResultMes linkSingleCode(@RequestParam(required = false) Integer code,
                                    @RequestParam(required = false) Long telegramId
    ) {
        return userService.linkCodeTelegram(code, telegramId);
    }

    @GetMapping("/user/telegram/delete")
    public void linkDeleteTelegram(@RequestParam(required = false) Long telegramId
    ) {
        userService.linkDeleteTelegram(telegramId);
    }
}
