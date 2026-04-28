package ru.darujo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.darujo.TestAndShutdownController;
import ru.darujo.dto.jwt.JwtResponse;
import ru.darujo.service.AuthService;
import ru.darujo.service.UserService;


@RestController()
@RequestMapping("/users")
public class SystemController extends TestAndShutdownController {
    private UserService userService;
    private AuthService authService;

    @GetMapping("/system/user/password/request")
    public void getPasswordRecovery(@RequestParam String nikName,
                                    @RequestParam String email) {
        userService.getRestorePassword(nikName, email);
    }

    @GetMapping("/system/user/password/restore")
    public JwtResponse getPasswordRestore(@RequestParam String nikName,
                                          @RequestParam String code) {
        return authService.restorePassword(nikName, code);
    }

    @GetMapping("/system/user/email/confirm")
    public boolean confirmEmail(String nikName, String code) {
        return userService.confirmEmail(nikName, code);
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }


}
