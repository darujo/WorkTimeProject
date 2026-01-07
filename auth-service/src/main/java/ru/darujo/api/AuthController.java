package ru.darujo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import ru.darujo.dto.jwt.JwtRequest;
import ru.darujo.dto.jwt.JwtResponse;
import ru.darujo.model.User;
import ru.darujo.service.AuthService;
import ru.darujo.utils.JwtTokenUtils;

@RestController
@CrossOrigin
public class AuthController {
    private AuthService authService;

    @Autowired
    public void setAuthService(AuthService authService) {
        this.authService = authService;
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

    @PostMapping("/auth/")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest jwtRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));
        User user = authService.getUser(jwtRequest.getUsername());
        String token = jwtTokenUtils.generateToken(user);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @GetMapping("/token/new")
    public JwtResponse changeProject(@RequestHeader String username,
                                     @RequestParam(required = false) Long projectId) {
        User user = authService.changeProject(username, projectId);
        String token = jwtTokenUtils.generateToken(user);
        return new JwtResponse(token);
    }

}
