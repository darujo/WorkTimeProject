package ru.darujo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import ru.darujo.convertor.UserConvertor;
import ru.darujo.dto.JwtRequest;
import ru.darujo.dto.JwtResponse;
import ru.darujo.service.UserService;
import ru.darujo.utils.JwtTokenUtils;

@RestController
@CrossOrigin
public class AuthController {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    public void setJwtTokenUtils(JwtTokenUtils jwtTokenUtils) {
        this.jwtTokenUtils = jwtTokenUtils;
    }

    private AuthenticationManager authenticationManager;

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/auth")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest jwtRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));
        UserDetails userDetails = userService.loadUserByUsername(jwtRequest.getUsername());
        String token = jwtTokenUtils.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @GetMapping("/users/user")
    public ResponseEntity<?> getUserDto(@RequestParam(required = false) String nikName) {
        try {
            return ResponseEntity.ok(UserConvertor.getUserDto(userService.loadUserByNikName(nikName)));

        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex);
        }
    }
    @GetMapping("/users")
    public ResponseEntity<?> getUserList() {
        try {

            return ResponseEntity.ok(userService.getUserList().stream().map(UserConvertor::getUserDto));

        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex);
        }
    }
}
