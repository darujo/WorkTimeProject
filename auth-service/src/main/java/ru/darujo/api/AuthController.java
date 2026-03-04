package ru.darujo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import ru.darujo.dto.jwt.JwtRequest;
import ru.darujo.dto.jwt.JwtResponse;
import ru.darujo.service.AuthService;

@RestController
@CrossOrigin
public class AuthController {
    private AuthService authService;
    private AuthenticationManager authenticationManager;

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/auth/")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest jwtRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));

        String token = authService.createAuthToken(jwtRequest.getUsername());
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @GetMapping("/token/new")
    public JwtResponse changeProject(@RequestHeader String username,
                                     @RequestParam(required = false) Long projectId) {
        String token = authService.changeProject(username, projectId);
        return new JwtResponse(token);
    }

}
